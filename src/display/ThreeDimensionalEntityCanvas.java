package display;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Collection;

import camera.ThreeDimensionalViewCamera;
import camera.TwoDimensionalViewCamera;
import entity.BasePhysicalEntity;

public class ThreeDimensionalEntityCanvas extends Canvas implements EntityCanvas {

	private static final long serialVersionUID = 1L;

	ArrayList<BasePhysicalEntity> entities = new ArrayList<BasePhysicalEntity>();

	private BufferStrategy strategy;
	
	private ThreeDimensionalViewCamera camera;
	

	/*******************************************************************************************************
	 * Constructors
	 *******************************************************************************************************/
	
	public ThreeDimensionalEntityCanvas(int width, int height, ThreeDimensionalViewCamera camera) {
		super();
		setPreferredSize(new Dimension(width, height));
		this.camera = camera;
		
		createBufferStrategy(2);
		strategy = getBufferStrategy();
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
	 * 
	 * @param g
	 */
	private void doPaint(Graphics g) {

	}
}
