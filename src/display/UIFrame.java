package display;

import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.Collection;

import javax.swing.JFrame;

import entity.BasePhysicalEntity;

/**
 * Aside from me hating Swing, how am I going to make this work?
 * 
 * UIFrame root/parent/be-all display panel. Presuming I don't have too many misconceptions about Swing,
 * every other UI element is going to be attached to or inside of this one.
 * 
 * Current TODO:
 * 1) Various buttons!
 * 	a) moving the display panel
 * 	b) zooming in and out
 * 2) Menu options
 * 	a) saving
 * 	b) loading
 * 	c) restarting the simulation
 * 
 * And at some point I want to put in things that you can edit how Setup works from here... but I'm going to 
 * deal with that later.
 * 
 * This is intended to be the controller in the MVP model.
 * TODO: I am running into issues with that, given how Swing defines buffer strategies for frames and windows, but not for
 * individual panels. Need to research.
 * 
 * @author mjanes
 *
 */
public class UIFrame extends JFrame {
	
	
	private static final long serialVersionUID = 1L;
	
	private TwoDimensionalEntityDisplayPanel displayPanel;
	
	private BufferStrategy strategy;
	
	
	public UIFrame(int width, int height) {
		setSize(width, height);		
		displayPanel = new TwoDimensionalEntityDisplayPanel(width, height);
		setContentPane(displayPanel);
		setIgnoreRepaint(true);
		pack();
		setVisible(true);		
		

		/** 
		 * TODO: Not sure if I am not understanding Swing terms, but I would prefer the frame not care about
		 * buffering, but instead delegate that to the displayPanel. 
		 */
		createBufferStrategy(2);
		strategy = getBufferStrategy();
	}
	
	public void setEntities(Collection<BasePhysicalEntity> entities) {
		displayPanel.setEntities(entities);
	}
	
	
	
	/*************************************************************************************************************
	 * Graphics
	 *************************************************************************************************************/
	
	/**
	 * 
	 */
	public void incrementGraphics() {
		Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
		displayPanel.paint(g);
		
		g.dispose();
		strategy.show();
	}
}
