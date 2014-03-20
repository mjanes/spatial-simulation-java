package display;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Collection;

import camera.ThreeDimensionalViewCamera;
import entity.BasePhysicalEntity;

/**
 * Canvas for displaying Entity objects in three dimensional space. 
 * 
 * The canvas is the projection plane, as seen from the ThreeDimensionalViewCamera object.
 * The projection plane is assumed to be EYE_Z_DISTANCE units from the camera, as, I assumed that
 * was how far the an eye would be from the monitor, though as it is currently 400, it is not exactly 
 * accurate.
 * 
 * NOTE: I am not using a standard three dimensional coordinate system: 
 * https://en.wikipedia.org/wiki/Cartesian_coordinate_system
 * I am having the x dimension be the left and right, y dimension being up and down, and z dimension 
 * being in and out.
 * Why on earth the standard three dimensional Cartesian coordinate system didn't take the two dimensional 
 * coordinate system and append a z axis to it, I have no idea, but that's the way I'm doing it for
 * now. May revise later, when I get fully away from the two dimensional entity canvas.
 * 
 * 
 * @author mjanes
 *
 */
public class ThreeDimensionalEntityCanvas extends Canvas {

	private static final long serialVersionUID = 1L;

	ArrayList<BasePhysicalEntity> entities = new ArrayList<BasePhysicalEntity>();

	private BufferStrategy strategy;
	
	private ThreeDimensionalViewCamera camera;
	
	public static final double EYE_DISTANCE = 3000;
	
	
	/*******************************************************************************************************
	 * Constructors
	 *******************************************************************************************************/
	
	public ThreeDimensionalEntityCanvas(int width, int height, ThreeDimensionalViewCamera camera) {
		super();
		setPreferredSize(new Dimension(width, height));		
		this.camera = camera;
	}
	
	
	/*******************************************************************************************************
	 * Utilities
	 *******************************************************************************************************/	

	public void setEntities(Collection<BasePhysicalEntity> entities) {
		this.entities = new ArrayList<BasePhysicalEntity>(entities);
	}

	/**
	 * This must be in a separate method, and not the constructor, because this component must be laid out
	 * before creating the buffer strategy.
	 * 
	 * I would like it if there were different lifecyle methods, akin to Android's onLayout, onCreateView, etc
	 * that this could be put in, but, well, not sure those exist with swing and awt.
	 */
	public void initBuffer() {
		createBufferStrategy(2);
		strategy = getBufferStrategy();
	}
	

	/********************************************************************************************************
	 * Graphics
	 ********************************************************************************************************/	

	public void updateGraphics() {
		Graphics g = strategy.getDrawGraphics();
		
		doPaint(g);
		g.dispose();
		strategy.show();
	}	
	
	
	/**
	 * Now this is probably going to be a good bit more complex than the TwoDimensionalEntityCanvas.
	 * 
	 * In order to get this running at some degree of efficiency, it looks like it's going to involve
	 * a good deal of matrix math.
	 * 
	 * Referencing: 
	 * 	https://en.wikipedia.org/wiki/3D_projection
	 *  https://en.wikipedia.org/wiki/3D_projection#Perspective_projection
	 *  https://en.wikipedia.org/wiki/Camera_matrix
	 *  http://ogldev.atspace.co.uk/www/tutorial12/tutorial12.html
	 *  https://en.wikipedia.org/wiki/Pinhole_camera_model
	 *  https://en.wikipedia.org/wiki/Rotation_(mathematics)
	 *  http://www.gamedev.net/topic/286454-simple-2d-point-rotation/
	 *  
	 * @param g
	 */
	private void doPaint(Graphics g) {
		super.paint(g);

		// Entity color		
		g.setColor(Color.BLACK);
		
		// View plane height, width, and aspect ratio.
		double width = getWidth();
		double height = getHeight();
		
		// Distance from camera to view plane is DEFAULT_EYE_Z_DISTANCE
		// Misc variables used for calculating rotations and projections
		double xP;
		double yP;
		double zP;
		double angle;
		double cos;
		double sin;
		double tempX;		
		double tempY;
		double tempZ;
		double distanceRatio;

		
		for (BasePhysicalEntity entity : entities) {
			/* Starting offset from camera
			 * This is to set the camera at the center of things
			 * 0, 0 is now the location of the camera.
			 * 
			 * Bear in mind that we are still using the coordinate system of the display,
			 * so something at 1, 1 would not be in the upper right quadrant, but would 
			 * be in the lower right quadrant. 1, -1 would be in the upper right.
			 * May want to undo that later...
			 */
			xP = entity.getX() - camera.getX();
			yP = entity.getY() - camera.getY(); 	
			zP = entity.getZ() - camera.getZ();
			
			
			// Perform the rotations on the various axes			
			
			// Z axis rotation first
			// If zAngle is 0, then there should be no rotation, and xP and xY should
			// be the same going out as coming in.
			angle = Math.toRadians(camera.getZAngle());
			cos = Math.cos(angle);
			sin = Math.sin(angle);
			tempX = xP * cos - yP * sin;
			tempY = xP * sin + yP * cos; 
			xP = tempX;
			yP = tempY;
			
			// Y axis rotation
			angle = Math.toRadians(camera.getYAngle());
			cos = Math.cos(angle);
			sin = Math.sin(angle);
			tempX = xP * cos - zP * sin;
			tempZ = xP * sin + zP * cos;
			xP = tempX;
			zP = tempZ;
			
			
			// X axis rotation
			angle = Math.toRadians(camera.getXAngle());
			cos = Math.cos(angle);
			sin = Math.sin(angle);
			tempY = yP * cos - zP * sin;
			tempZ = yP * sin + zP * cos;
			yP = tempY;
			zP = tempZ;
			
			// Rotation is complete
			
			// Objects with a negative zP will not be displayed.
			// Objects with a 0 zP are assumed to be on the camera, covering the screen essentially			
			if (zP < 0) continue;
						
			// Project onto viewing plane, ie the further away it is, the more it will appear towards the center
			distanceRatio = EYE_DISTANCE / zP;
			xP = xP * distanceRatio;
			yP = yP * distanceRatio;
			
			
			// Adding width / 2 and height / 2 to the x and y projections, so that 0,0 appears in the middle of the screen
			// Resizing the radius, so that if an object's zP is equal to EYE_DISTANCE, it is shown at its default
			// radius, otherwise smaller if further away, larger if closer.
			double radius = (int) (entity.getRadius() * distanceRatio); 
			// Also subtracting half the radius from the projection point, as that is needed to have xP, yP be the center
			// of the circle.
			xP += (width / 2) - radius / 2;
			yP += (height / 2) - radius / 2;
			
			g.fillOval((int) xP, (int) yP, (int) radius, (int) radius);
			//g.drawString(entity.getLabel(), (int) xP + 5, (int) yP - 20);
			//g.drawString("xP: " + xP + ", yP: " + yP, (int) xP + 5, (int) yP - 10);
		}		
	
	}
}
