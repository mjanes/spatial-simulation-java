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
			// TODO: Perform check that entity is on screen
			g.fillOval((int) (entity.getX() - camera.getX()), (int) (entity.getY() - camera.getY()), (int) radius, (int) radius);
		}
	}
		
}
