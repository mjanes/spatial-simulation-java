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
public class ThreeDimensionalEntityCanvas extends Canvas implements IEntityCanvas {

	private static final long serialVersionUID = 1L;

	ArrayList<BasePhysicalEntity> entities = new ArrayList<BasePhysicalEntity>();

	private BufferStrategy strategy;
	
	private ThreeDimensionalViewCamera camera;
	
	public static final double EYE_DISTANCE = 400;
	
	
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

	@Override
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
		
		
		for (BasePhysicalEntity entity : entities) {
			double distance = camera.getDistance(entity);
			double distanceRatio = EYE_DISTANCE / distance; // The ratio of that distance to default distance. Used for resizing objects.
			
			// Getting offset from camera
			double xOffset = entity.getX() - camera.getX();
			double yOffset = entity.getY() - camera.getY();
			double zOffset = camera.getZ() - entity.getZ();
			
			// Perform the rotation
			//double cZAngle = Math.toDegrees(Math.atan(yOffset / xOffset));
			//double nZAngle = cZAngle + camera.getZAngle();
			double beta = Math.toRadians(camera.getZAngle());
			double cosBeta = Math.cos(beta);
			double sinBeta = Math.sin(beta);
			double xAfterZRotation = xOffset * cosBeta - yOffset * sinBeta;
			double yAfterZRotation = xOffset * sinBeta + yOffset * cosBeta; 
			
			
			int xProjection = (int) ((xAfterZRotation / distance) * EYE_DISTANCE);
			int yProjection = (int) ((yAfterZRotation / distance) * EYE_DISTANCE);
			
			// Adding width / 2 and height / 2 to the x and y projects, so that 0,0 appears in the middle of the screen
			xProjection += (width / 2);
			yProjection += (height / 2);
			
			g.fillOval(xProjection, yProjection, (int) (entity.getRadius() * distanceRatio), (int) (entity.getRadius() * distanceRatio));
		}
		
		// Temp: For debugging the 3d stuff, going to draw a red x in the center of the field of view
		//g.setColor(Color.RED);
		//g.drawLine(0, 0, (int) width,  (int) height);
		//g.drawLine(0, (int) height, (int) width, 0);
	
	}
}
