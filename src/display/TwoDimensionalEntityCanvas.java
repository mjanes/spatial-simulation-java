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
	
	protected int xOffset = 0;
	protected int yOffset = 0;
	
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
			g.fillOval((int) entity.getX() + xOffset, (int) entity.getY() + yOffset, (int) radius, (int) radius);
		}
	}
	
	
	/*********************************************************************************************************
	 * Offset manipulation
	 * 
	 * I am in the process of getting rid of something as simple as an offset, in order to be able to do
	 * zooming and such. For that, I intend to use a camera object, that can move in 3d space. However,
	 * given that this is a two dimensional entity canvas... hmm... well, with zooming the camera should 
	 * effectively be moving in a z-axis, and that kind of makes me want to abandon the idea of doing two 
	 * dimensional stuff at all, and switch to doing everything in 3d.. Though... I suppose there is still some
	 * benefits to keeping the objects as two. I suppose we can have a 2d and 3d camera object, even if the 
	 * 2d camera object has a location in 3d space. Just dosen't rotate. 
	 *********************************************************************************************************/
	
	public void incrementXOffset(int i) {
		xOffset += i;
	}
	
	public void incrementYOffset(int i) {
		yOffset += i;
	}
}
