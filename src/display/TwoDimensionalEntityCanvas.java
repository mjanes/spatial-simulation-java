package display;

import java.util.ArrayList;
import java.util.Collection;

import entity.BasePhysicalEntity;

import java.awt.*;
import java.awt.image.VolatileImage;

/**
 * Initial display class. Just going to be a rectangle drawing circles that can orbit around eachother.
 * 
 * Questions:
 * 1) What sort of interface should be built? Are there qualities of a display class that will be shared between a 2d and 3d display?
 * 2) Should I make it so a 2d display can handle 3d entities? Hrm... Well, that might largely depend on the physics engine, so I can ignore it 
 * for now.
 * 
 * Basically, all this class will do, for the time being, is take an array of entities of some sort, get their x-y coordinates and get their display
 * value, and then paint them. Now... I'm not sure how I want the display value to be done. By the objects themselves, or am I going to have the
 * objects just spit out a radius and color? Hrm...
 * 
 * And... yeah, guess this is using swing. It's been years. I should really do this in javascript or something instead.
 * 
 * TODO: We may wish to have this extend JComponent as opposed to JPanel. Look into that.
 * 
 * @author mjanes
 */
public class TwoDimensionalEntityCanvas extends Canvas {
		
	private static final long serialVersionUID = 1L;	
	
	ArrayList<BasePhysicalEntity> entities = new ArrayList<BasePhysicalEntity>();
	private VolatileImage volatileImg;
	
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
	
	
	/********************************************************************************************************
	 * Graphics
	 ********************************************************************************************************/	

	@Override
	public void update(Graphics g) {
		paint(g);
	}	
	
	@Override
	public void paint(Graphics g) {
		createBackBuffer();
		
		do {
			GraphicsConfiguration gc = getGraphicsConfiguration();
			int valCode = volatileImg.validate(gc);
			
			if (valCode==VolatileImage.IMAGE_INCOMPATIBLE) {
				createBackBuffer();
			}
			
			Graphics offscreenGraphics = volatileImg.getGraphics();
			doPaint(offscreenGraphics);
			
			g.drawImage(volatileImg,  0,  0,  this);
		} while (volatileImg.contentsLost());
	}
	
	private void createBackBuffer() {
		GraphicsConfiguration gc = getGraphicsConfiguration();
		volatileImg = gc.createCompatibleVolatileImage(getWidth(), getHeight());
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
