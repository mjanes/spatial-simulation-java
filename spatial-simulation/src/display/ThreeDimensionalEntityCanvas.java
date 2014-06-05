package display;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Collection;

import camera.Camera;
import entity.Entity;

/**
 * Canvas for displaying Entity objects in three dimensional space. 
 * 
 * The canvas is the projection plane, as seen from the Camera object.
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

	ArrayList<Entity> mEntities = new ArrayList<>();

	private BufferStrategy mStrategy;
	
	private Camera mCamera;
	
	public static final double EYE_DISTANCE = 3000;
	
	
	/*******************************************************************************************************
	 * Constructors
	 *******************************************************************************************************/
	
	public ThreeDimensionalEntityCanvas(int width, int height, Camera camera) {
		super();
		setPreferredSize(new Dimension(width, height));		
	    mCamera = camera;
	}
	
	
	/*******************************************************************************************************
	 * Utilities
	 *******************************************************************************************************/	

	public void setEntities(Collection<Entity> entities) {
		mEntities = new ArrayList<>(entities);
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
		mStrategy = getBufferStrategy();
	}
	

	/********************************************************************************************************
	 * Graphics
	 ********************************************************************************************************/	

	public void updateGraphics() {
		Graphics g = mStrategy.getDrawGraphics();
		
		doPaint(g);
		g.dispose();
		mStrategy.show();
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
	 *  https://stackoverflow.com/questions/7576263/simple-3d-projection-and-orientation-handling
	 *  http://www.epixea.com/research/multi-view-coding-thesisse8.html
	 *  http://www.csc.villanova.edu/~mdamian/Past/graphicsF10/notes/Projection.pdf
	 *  https://en.wikipedia.org/wiki/Rotation_matrix
	 *  https://en.wikipedia.org/wiki/Rotation_formalisms_in_three_dimensions
	 *  https://en.wikipedia.org/wiki/Euclidean_vector
	 *  
	 * @param g
	 */
	private void doPaint(Graphics g) {
		super.paint(g);

		// Canvas width and height
		double canvasWidth = getWidth();
		double canvasHeight = getHeight();

        for (Entity entity : mEntities) {
			paintEntity(g, mCamera, entity, canvasWidth, canvasHeight);
		}		
	}

    private void paintEntity(Graphics g, Camera camera, Entity entity, double canvasWidth, double canvasHeight) {
        // Distance from camera to view plane is DEFAULT_EYE_Z_DISTANCE
        // Optimization, continue if the entity is too small and far away from the camera
        // so as to avoid all the expensive trig operations.
        int radius = (int) (entity.getRadius() * EYE_DISTANCE / camera.getDistance(entity));
        if (radius < 1) return;

        Point2D.Double point = getCanvasLocation(camera, canvasWidth, canvasHeight, entity.getX(), entity.getY(), entity.getZ());
        if (point == null) return;

        double xP = point.getX();
        double yP = point.getY();

        // Entity color
        g.setColor(Color.BLACK);
        // Subtract half the radius from the projection point, because g.fillOval does not surround the center point
        g.fillOval((int) xP - radius / 2, (int) yP - radius / 2, radius, radius);
        //g.drawString(entity.getLabel(), (int) xP + 5, (int) yP - 20);
        //g.drawString("xP: " + xP + ", yP: " + yP, (int) xP + 5, (int) yP - 10);

        // Paint previous location, in order to get a sense of motion
        Point2D.Double previousPoint = getCanvasLocation(camera, canvasWidth, canvasHeight, entity.getPrevX(), entity.getPrevY(), entity.getPrevZ());
        if (previousPoint == null) return;

        g.setColor(Color.RED);
        g.drawLine((int) xP, (int) yP, (int) previousPoint.getX(), (int) previousPoint.getY());
    }

    /**
     * Looking into doing this all with matrix math for speed improvement.
     *
     * http://www.matrix44.net/cms/notes/opengl-3d-graphics/basic-3d-math-matrices
     *
     * @param camera
     * @param canvasWidth
     * @param canvasHeight
     * @param x
     * @param y
     * @param z
     * @return
     */
    private static Point2D.Double getCanvasLocation(Camera camera, double canvasWidth, double canvasHeight, double x, double y, double z) {
        // Misc variables used for calculating rotations and projections
        double xP;
        double yP;
        double zP;
        double cos;
        double sin;
        double tempX;
        double tempY;
        double tempZ;
        double distanceRatio;

        /* Starting offset from camera
         * This is to set the camera at the center of things
         * 0, 0 is now the location of the camera.
         *
         * Bear in mind that we are still using the coordinate system of the display,
         * so something at 1, 1 would not be in the upper right quadrant, but would
         * be in the lower right quadrant. 1, -1 would be in the upper right.
         * May want to undo that later...
         */
        xP = x - camera.getX();
        yP = y - camera.getY();
        zP = z - camera.getZ();


        // Perform the rotations on the various axes
        // Note: Apparently order matters here, which I am somewhat confused by.

        // X axis rotation
        double xAngle = Math.toRadians(camera.getXAngle());
        cos = Math.cos(xAngle);
        sin = Math.sin(xAngle);
        tempY = yP * cos - zP * sin;
        tempZ = yP * sin + zP * cos;
        yP = tempY;
        zP = tempZ;

        // Y axis rotation
        double yAngle = Math.toRadians(camera.getYAngle());
        cos = Math.cos(yAngle);
        sin = Math.sin(yAngle);
        tempX = xP * cos - zP * sin;
        tempZ = xP * sin + zP * cos;
        xP = tempX;
        zP = tempZ;

        // Z axis rotation
        double zAngle = Math.toRadians(camera.getZAngle());
        cos = Math.cos(zAngle);
        sin = Math.sin(zAngle);
        tempX = xP * cos - yP * sin;
        tempY = xP * sin + yP * cos;
        xP = tempX;
        yP = tempY;


        // Rotation is complete

        // Objects with a negative zP will not be displayed.
        // Objects with a 0 zP are assumed to be on the camera, covering the screen essentially
        if (zP < 0) return null;

        // Project onto viewing plane, ie the further away it is, the more it will appear towards the center
        distanceRatio = EYE_DISTANCE / zP;
        xP = xP * distanceRatio;
        yP = yP * distanceRatio;

        // Adding width / 2 and height / 2 to the x and y projections, so that 0,0 appears in the middle of the screen
        // Resizing the radius, so that if an object's zP is equal to EYE_DISTANCE, it is shown at its default
        // radius, otherwise smaller if further away, larger if closer.
        xP += (canvasWidth / 2);
        yP += (canvasHeight / 2);

        return new Point2D.Double(xP, yP);
    }
}
