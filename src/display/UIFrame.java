package display;

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
	public static final int FRAME_DELAY = 10; // Milliseconds between each frame painting
	
	private TwoDimensionalEntityCanvas canvas;
	
	
	public UIFrame(int width, int height) {
		setSize(width, height);		
		canvas = new TwoDimensionalEntityCanvas(width, height);
		getContentPane().add(canvas);
		
		pack();
		setVisible(true);		
	}
	
	public void setEntities(Collection<BasePhysicalEntity> entities) {
		canvas.setEntities(entities);
	}
	
	public void updateGraphics() {
		canvas.repaint();
	}
	
	
}
