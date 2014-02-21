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
public class TwoDimensionalEntityCanvas extends Canvas implements EntityCanvas {
		
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
	
	@Override
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
		
	private void doPaint(Graphics g) {
		super.paint(g);
		
		// Paint entities				
		g.setColor(Color.BLACK);
		
		
		/* 
		 * DEFAULT_EYE_Z_DISTANCE is the assumed default distance of the eye from the monitor
		 * zRatio is the ratio of the camera z position to that default distance, this is used
		 * so that if the camera is moved closer or farther away from the entities, that they are
		 * displayed larger or smaller, and closer to or farther away from the screen.
		 */
		double zRatio = DEFAULT_EYE_Z_DISTANCE / camera.getZ();

		
		for (BasePhysicalEntity entity : entities) {

			// Now, for every difference from z
			// Let's see, the greater the camera z is, that means, lets see, the camera is farther back
			// The greater the zCamera difference, then the more we subtract the x and y value.
			// TODO: This is not using the distance of the entity from the camera to change the position of the
			// entity on the screen. Which, at the moment, makes sense because this is a 'TwoDimensionalEntityCanvas'
			int zCameraDifference = (int) (camera.getZ() - entity.getZ());
			if (zCameraDifference < 0) continue; // In this case the object is behind the camera
			
			// Ok... zooming is a transformation, wherein... hrm... given the coordinate system here is inverted from
			// cartesian things...
			// anyways, for each point... we get how far away its x value and y value is from the camera.
			int xCameraDifference = (int) (entity.getX() - camera.getX());
			int yCameraDifference = (int) (entity.getY() - camera.getY());
						
			
			
			// I want to calculate things as 0, 0 is in the center. However, in the world of java display
			// 0, 0 is the upper left, so doing this translation.
			int xDisplay = (int) (xCameraDifference * zRatio) + (getWidth() / 2);
			int yDisplay = (int) (yCameraDifference * zRatio) + (getHeight() / 2);
			

			double radius = entity.getRadius();
			
			g.fillOval(xDisplay, yDisplay, (int) (radius * zRatio), (int) (radius * zRatio));
		}
	}
		
}
