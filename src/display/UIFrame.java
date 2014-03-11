package display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicArrowButton;

import physics.GravitationalPhysics;
import camera.TwoDimensionalViewCamera;
import entity.BasePhysicalEntity;

/**
 * Aside from me hating Swing, how am I going to make this work?
 * 
 * UIFrame root/parent/be-all display panel. Presuming I don't have too many misconceptions about Swing,
 * every other UI element is going to be attached to or inside of this one.
 * 
 * Current TODO:
 * Menu options
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
	
	// Ideally, we don't want these static, but for convenience at the moment
	private static volatile int frameDelay = 10; // Milliseconds between each frame painting	
	private static volatile boolean running = true;
	
	private Collection<BasePhysicalEntity> entities;
	
	// The canvas that is the display screen
	private TwoDimensionalEntityCanvas canvas;
	
	// The control panel, to one side of the canvas, that is for manipulating the view and universe
	private JPanel controlPanel;
	
	// The navigation panel, which is responsible for moving around the universe, ie, changing how it
	// is displayed in the canvas.
	private JPanel navigationPanel;
	
	// This panel contains buttons for changing the speed of the simulation
	private JPanel timePanel;
	
	// Check if volatile is appropriate. Will affect the universe loop thread.
	private volatile TwoDimensionalViewCamera camera; 
	
	
	/**
	 * UI setup
	 * 
	 * TODO: Break this into a set of differentt initialization functions, getting too big and clumsy.
	 * 
	 * @param width
	 * @param height
	 */
	public UIFrame(int width, int height) {	

		// Initiate the camera
		camera = new TwoDimensionalViewCamera(width / 2, height / 2, TwoDimensionalEntityCanvas.DEFAULT_EYE_Z_DISTANCE);
		
		
		
		// Setup canvas
		canvas = new TwoDimensionalEntityCanvas(width, height, camera);		
		
		// Set up control panel, which will have buttons to manipulate view of canvas
		controlPanel = new JPanel(new BorderLayout()); // May wish to create a unique class for this.
		controlPanel.setPreferredSize(new Dimension(200, height));
		
		
		
		
		// Navigation panel, with up/down/left/right buttons
		navigationPanel = new JPanel(new GridLayout(3, 3));
		
		BasicArrowButton eastButton = new BasicArrowButton(BasicArrowButton.EAST);		
		BasicArrowButton northButton = new BasicArrowButton(BasicArrowButton.NORTH);
		BasicArrowButton southButton = new BasicArrowButton(BasicArrowButton.SOUTH);
		BasicArrowButton westButton = new BasicArrowButton(BasicArrowButton.WEST);
		JButton zoomInButton = new JButton("Zoom in");
		JButton zoomOutButton = new JButton("Zoom out");
		
		Dimension buttonDimension = new Dimension(66, 66);
		eastButton.setPreferredSize(buttonDimension);
		northButton.setPreferredSize(buttonDimension);
		southButton.setPreferredSize(buttonDimension);
		westButton.setPreferredSize(buttonDimension);
		zoomInButton.setPreferredSize(buttonDimension);
		zoomOutButton.setPreferredSize(buttonDimension);
		
		eastButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				camera.moveX(10);			
			}			
		});
		
		westButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				camera.moveX(-10);
			}
		});
		
		northButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				camera.moveY(-10);			
			}			
		});
		

		southButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				camera.moveY(10);			
			}			
		});
		
		
		zoomInButton.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent arg0) {
				camera.moveZ(-10);
			}
		});
		
		zoomOutButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				camera.moveZ(10);
			}
		});
		
		navigationPanel.add(new JPanel());
		navigationPanel.add(northButton);
		navigationPanel.add(new JPanel());
		navigationPanel.add(westButton);
		navigationPanel.add(new JPanel());
		navigationPanel.add(eastButton);	
		navigationPanel.add(zoomOutButton);
		navigationPanel.add(southButton);
		navigationPanel.add(zoomInButton);
		
		navigationPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		navigationPanel.setPreferredSize(new Dimension(200, 200));
		
		
		// Initialize time control panel
		timePanel = new JPanel(new FlowLayout());
		JButton pauseButton = new JButton("Pause");
		JButton playButton = new JButton("Play");
		JButton increaseSpeedButton = new JButton("Increase Speed");
		JButton decreaseSpeedButton = new JButton("Decrease Speed");
		
		pauseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				running = false;
			}
			
		});
		
		playButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				running = true;
			}
			
		});
		
		increaseSpeedButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				frameDelay--;
			}
			
		});
		
		decreaseSpeedButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frameDelay++;
			}
			
		});
		
		timePanel.add(pauseButton);
		timePanel.add(playButton);
		timePanel.add(decreaseSpeedButton);
		timePanel.add(increaseSpeedButton);
		
		
		timePanel.setBorder(BorderFactory.createLineBorder(Color.black));
		timePanel.setPreferredSize(new Dimension(200, 100));
		
		
		
		// Add different panels to control panel
		controlPanel.add(navigationPanel, BorderLayout.NORTH);
		controlPanel.add(timePanel, BorderLayout.SOUTH);
		controlPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		
		
		// Final packing of everything into content pane and display
		Container contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout());
		contentPane.add(controlPanel);
		contentPane.add(canvas);				
		pack();		
		setVisible(true);	
		
		
		// initialize the buffer of the canvas
		canvas.initBuffer();
	}
	
	
	/***************************************************************************************************
	 * Utility/General
	 **************************************************************************************************/
	
	public void setEntities(Collection<BasePhysicalEntity> entities) {
		this.entities = entities;
		canvas.setEntities(entities);
	}
	
	public void start() {
		//	3) Begin running physics on things
		Thread universeThread = new Thread(new UniverseLoop(canvas, entities));
		universeThread.setPriority(Thread.MIN_PRIORITY);
		universeThread.start();
	}
	
	
	/**************************************************************************************************
	 * Running the universe
	 **************************************************************************************************/
	
	/**
	 * Learning about this from: http://www.javalobby.org/forums/thread.jspa?threadID=16867&tstart=0
	 * 
	 * Pieces/threads we need:
	 * 1) Simulation logic of a given phase of the universe. Currently, that is applying gravity and moving the entities.
	 * 2) Render the universe state. Does not have to be phase that was just simulated. Can be the previous phase, so 
	 * 	that those two phase have occurred on separate threads.
	 * 3) Sleep until a certain amount of time has passed, and then draw the graphics rendered in phase 2,
	 * thus ensuring a consistent frame rate. 
	 * 
	 * @author mjanes
	 */
	private static class UniverseLoop implements Runnable {
		
		long cycleTime;
		private TwoDimensionalEntityCanvas canvas;
		private Collection<BasePhysicalEntity> entities;
		
		public UniverseLoop(TwoDimensionalEntityCanvas canvas, Collection<BasePhysicalEntity> entities) {
			this.canvas = canvas;
			this.entities = entities;
		}
		
		@Override
		public void run() {
						
			while (true)  {

				// Wait an appropriate amount of time, so that the frame rate is progressing constantly.
				synchFramerate();
				
				// tell graphics to repaint
				canvas.updateGraphics();

				if (running) {
					
					// Perform physics simulations
					updateUniverseState();					
				}
			}
			
		}
		
		private void synchFramerate() {
			// Check for initialization.
			if (cycleTime == 0) cycleTime = System.currentTimeMillis();
			
			cycleTime = cycleTime + frameDelay;
			long difference = cycleTime - System.currentTimeMillis();
			
			try {
				Thread.sleep(Math.max(0,  difference));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	
		private void updateUniverseState() {
			// run round of physics
			GravitationalPhysics.gravity(entities);
			for (BasePhysicalEntity entity : entities) {
				entity.move();
			}		
		}
	}
	
}
