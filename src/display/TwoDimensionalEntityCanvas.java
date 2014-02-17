package display;

import java.util.ArrayList;
import java.util.Collection;

import entity.BasePhysicalEntity;

import java.awt.*;
import java.awt.image.BufferStrategy;

import camera.TwoDimensionalViewCamera;


/**
 * Initial display class. A canvas drawing the universe state. The class containing this component will be 
 * in charge of controlling the ui.
 * 
 * @author mjanes
 */
public class TwoDimensionalEntityCanvas extends Canvas {
		
	private static final long serialVersionUID = 1L;	
	
	ArrayList<BasePhysicalEntity> entities = new ArrayList<BasePhysicalEntity>();
	
	private BufferStrategy strategy;
	
	private TwoDimensionalViewCamera camera;
	
	public static final int DEFAULT_EYE_Z_DISTANCE = 400;
	
	
	/*******************************************************************************************************
	 * Constructors
	 *******************************************************************************************************/
	
	public TwoDimensionalEntityCanvas(int width, int height, TwoDimensionalViewCamera camera) {
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
		
	private void doPaint(Graphics g) {
		super.paint(g);
		
		// Paint entities				
		g.setColor(Color.BLACK);
		double radius;
		for (BasePhysicalEntity entity : entities) {

			// Now, for every difference from z
			// Let's see, the greater the camera z is, that means, lets see, the camera is farther back
			// So... the greater the zCamera difference, then the more we subtract the x and y value.
			int zCameraDifference = (int) (camera.getZ() - entity.getZ());
			if (zCameraDifference < 0) continue; // In this case the object is behind the camera
			
			// Ok... zooming is a transformation, wherein... hrm... given the coordinate system here is inverted from
			// cartesian things...
			// anyways, for each point... we get how far away its x value and y value is from the camera.
			int xCameraDifference = (int) (entity.getX() - camera.getX());
			int yCameraDifference = (int) (entity.getY() - camera.getY());
			
			// Now... we have to have some trigonometry to do!
			// Also, first question, hmm... let's see, we're already putting a positive z on the camera. This is to
			// represent how far the eyes are away from the monitor.
			
			double zRatio = DEFAULT_EYE_Z_DISTANCE / camera.getZ();
			
			
			// I want to calculate things as 0, 0 is in the center. However, in the world of java display
			// 0, 0 is the upper left, so doing this translation.
			int xDisplay = (int) (xCameraDifference * zRatio) + (getWidth() / 2);
			int yDisplay = (int) (yCameraDifference * zRatio) + (getHeight() / 2);
			

			radius = entity.getRadius();
			
			g.fillOval(xDisplay, yDisplay, (int) (radius * zRatio), (int) (radius * zRatio));
		}
	}
		
}
