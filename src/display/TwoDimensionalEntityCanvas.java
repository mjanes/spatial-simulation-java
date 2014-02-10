package display;

import java.util.ArrayList;
import java.util.Collection;

import entity.BasePhysicalEntity;

import java.awt.*;
import java.awt.image.BufferStrategy;


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
	
	/*******************************************************************************************************
	 * Constructors
	 *******************************************************************************************************/
	
	public TwoDimensionalEntityCanvas(int width, int height) {
		super();
		setPreferredSize(new Dimension(width, height));
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
			g.fillOval((int) entity.getX(), (int) entity.getY(), (int) radius, (int) radius);
		}
	}
}
