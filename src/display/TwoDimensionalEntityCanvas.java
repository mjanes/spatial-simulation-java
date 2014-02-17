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
			radius = entity.getRadius();
			// TODO: handle zooming in and out
			// Ok... zooming is a transformation, wherein... hrm... given the coordinate system here is inverted from
			// cartesian things...
			// anyways, for each point... we get how far away its x value and y value is from the camera.
			int xCameraDifference = (int) (entity.getX() - camera.getX());
			int yCameraDifference = (int) (entity.getY() - camera.getY());
			
			// I want to calculate things as 0, 0 is in the center. However, in the world of java display
			// 0, 0 is the upper left, so doing this translation.
			int xDisplay = xCameraDifference + (getWidth() / 2);
			int yDisplay = yCameraDifference + (getHeight() / 2);
			
			g.fillOval(xDisplay, yDisplay, (int) radius, (int) radius);
		}
	}
		
}
