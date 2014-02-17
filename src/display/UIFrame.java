package display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicArrowButton;

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
	
	// The canvas that is the display screen
	private TwoDimensionalEntityCanvas canvas;
	
	// The control panel, to one side of the canvas, that is for manipulating the view and universe
	private JPanel controlPanel;
	
	// The navigation panel, which is responsible for moving around the universe, ie, changing how it
	// is displayed in the canvas.
	// TODO: Change all navigation listeners to edit a camera object.
	private JPanel navigationPanel;
	
	
	public UIFrame(int width, int height) {	

		// Setup canvas
		canvas = new TwoDimensionalEntityCanvas(width, height);		
		
		// Set up control panel, which will have buttons to manipulate view of canvas
		controlPanel = new JPanel(new BorderLayout()); // May wish to create a unique class for this.
		controlPanel.setPreferredSize(new Dimension(200, height));
		
		// Navigation panel, with up/down/left/right buttons
		navigationPanel = new JPanel(new GridLayout(3, 3));
		
		BasicArrowButton eastButton = new BasicArrowButton(BasicArrowButton.EAST);		
		BasicArrowButton northButton = new BasicArrowButton(BasicArrowButton.NORTH);
		BasicArrowButton southButton = new BasicArrowButton(BasicArrowButton.SOUTH);
		BasicArrowButton westButton = new BasicArrowButton(BasicArrowButton.WEST);
		
		Dimension buttonDimension = new Dimension(66, 66);
		eastButton.setPreferredSize(buttonDimension);
		northButton.setPreferredSize(buttonDimension);
		southButton.setPreferredSize(buttonDimension);
		westButton.setPreferredSize(buttonDimension);
		
		eastButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				canvas.incrementXOffset(-10);				
			}			
		});
		
		westButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				canvas.incrementXOffset(10);
			}
		});
		
		northButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				canvas.incrementYOffset(10);				
			}			
		});
		

		southButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				canvas.incrementYOffset(-10);				
			}			
		});
		
		navigationPanel.add(new JPanel());
		navigationPanel.add(northButton);
		navigationPanel.add(new JPanel());
		navigationPanel.add(westButton);
		navigationPanel.add(new JPanel());
		navigationPanel.add(eastButton);	
		navigationPanel.add(new JPanel());
		navigationPanel.add(southButton);
		
		navigationPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		navigationPanel.setPreferredSize(new Dimension(200, 200));
		
		controlPanel.add(navigationPanel, BorderLayout.NORTH);
		controlPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		
		// Final packing of everything into content pane and display
		Container contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout());
		contentPane.add(controlPanel);
		contentPane.add(canvas);				
		pack();		
		setVisible(true);	
		
		
		// init the buffer of the canvas
		canvas.initBuffer();
	}
	
	public void setEntities(Collection<BasePhysicalEntity> entities) {
		canvas.setEntities(entities);
	}
	
	public void updateGraphics() {
		canvas.updateGraphics();
	}
	
	
}
