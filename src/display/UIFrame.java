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
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicArrowButton;

import physics.GravitationalPhysics;
import camera.ThreeDimensionalViewCamera;
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
	
	// The canvas that is the display screen and JPanel that holds it	
	private ThreeDimensionalEntityCanvas canvas;
	
	// The control panel, to one side of the canvas, that is for manipulating the view and universe
	private JPanel controlPanel;
	
	
	// Check if volatile is appropriate. Will affect the universe loop thread.
	private volatile ThreeDimensionalViewCamera camera; 
	
	private static final double CAMERA_ACCELERATION = 0.1;
	private static final int ANGLE_INCREMENT = 2;
	
	/**
	 * UI setup
	 * 
	 * @param width
	 * @param height
	 */
	public UIFrame(int width, int height) {	

		// Initiate the camera
		camera = new ThreeDimensionalViewCamera(0, 0, 0);
		
		
		// Setup canvas
		canvas = new ThreeDimensionalEntityCanvas(width, height, camera);
		
		
		
		
		// The navigation panel, which is responsible for moving around the universe, ie, changing how it
		// is displayed in the canvas.
		JPanel navigationPanel = setupNavigationPanel();		
		
		// Panel for controlling the orientation of the camera
		JPanel orientationPanel = setupOrientationPanel();

		// This panel contains buttons for changing the speed of the simulation
		JPanel timePanel = setupTimePanel();
		

		// Set up control panel, which will have buttons to manipulate view of canvas
		controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		controlPanel.setPreferredSize(new Dimension(200, height));

		
		// Add different panels to control panel
		controlPanel.add(navigationPanel);
		controlPanel.add(orientationPanel);
		controlPanel.add(timePanel);
		controlPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		
		
		// Final packing of everything into content pane and display
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(controlPanel, BorderLayout.WEST);
		contentPane.add(canvas, BorderLayout.CENTER);				
		pack();		
		setVisible(true);	
		
		
		// initialize the buffer of the canvas
		canvas.initBuffer();
	}
	
	
	/**
	 * Navigation panel, for moving the location of the camera, with up/down/left/right buttons
	 *
	 * @return
	 */
	private JPanel setupNavigationPanel() {
		JPanel navigationPanel = new JPanel(new GridLayout(3, 3));
		
		BasicArrowButton eastButton = new BasicArrowButton(BasicArrowButton.EAST);		
		BasicArrowButton northButton = new BasicArrowButton(BasicArrowButton.NORTH);
		BasicArrowButton southButton = new BasicArrowButton(BasicArrowButton.SOUTH);
		BasicArrowButton westButton = new BasicArrowButton(BasicArrowButton.WEST);
		JButton forwardButton = new JButton("Forward");
		JButton backwardsButton = new JButton("Backwards");
		
		Dimension buttonDimension = new Dimension(66, 66);
		eastButton.setPreferredSize(buttonDimension);
		northButton.setPreferredSize(buttonDimension);
		southButton.setPreferredSize(buttonDimension);
		westButton.setPreferredSize(buttonDimension);
		forwardButton.setPreferredSize(buttonDimension);
		backwardsButton.setPreferredSize(buttonDimension);
		
		eastButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				camera.addDeltaLeftRight(CAMERA_ACCELERATION);			
			}			
		});
		
		westButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				camera.addDeltaLeftRight(-CAMERA_ACCELERATION);
			}
		});
		
		northButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				camera.addDeltaUpDown(-CAMERA_ACCELERATION);			
			}			
		});
		

		southButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				camera.addDeltaUpDown(CAMERA_ACCELERATION);			
			}			
		});
		
		
		forwardButton.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent arg0) {
				camera.addDeltaForwardBackwards(CAMERA_ACCELERATION);
			}
		});
		
		backwardsButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				camera.addDeltaForwardBackwards(-CAMERA_ACCELERATION);
			}
		});
		
		navigationPanel.add(new JPanel());
		navigationPanel.add(northButton);
		navigationPanel.add(new JPanel());
		navigationPanel.add(westButton);
		navigationPanel.add(new JPanel());
		navigationPanel.add(eastButton);	
		navigationPanel.add(backwardsButton);
		navigationPanel.add(southButton);
		navigationPanel.add(forwardButton);
		
		navigationPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		navigationPanel.setPreferredSize(new Dimension(200, 200));
		
		return navigationPanel;
	}
	
	private JPanel setupOrientationPanel() {
		JPanel orientationPanel = new JPanel(new GridLayout(3, 3));
		
		JButton yAngleMinus = new JButton("yAngle minus");		
		JButton xAnglePlus = new JButton("xAngle plus");
		JButton xAngleMinus = new JButton("xAngle minus");
		JButton yAnglePlus = new JButton("yAngle plus");
		JButton zAngleMinus = new JButton("zAngle minus");
		JButton zAnglePlus = new JButton("zAngle Plus");
		
		Dimension buttonDimension = new Dimension(66, 66);
		yAngleMinus.setPreferredSize(buttonDimension);
		xAnglePlus.setPreferredSize(buttonDimension);
		xAngleMinus.setPreferredSize(buttonDimension);
		yAnglePlus.setPreferredSize(buttonDimension);
		zAngleMinus.setPreferredSize(buttonDimension);
		zAnglePlus.setPreferredSize(buttonDimension);
		
		xAnglePlus.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				camera.incrementXAngle(ANGLE_INCREMENT);			
			}			
		});
		
		xAngleMinus.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				camera.incrementXAngle(-ANGLE_INCREMENT);
			}
		});
		
		yAngleMinus.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				camera.incrementYAngle(-ANGLE_INCREMENT);			
			}			
		});
		

		yAnglePlus.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				camera.incrementYAngle(ANGLE_INCREMENT);			
			}			
		});
		
		
		zAngleMinus.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent arg0) {
				camera.incrementZAngle(-ANGLE_INCREMENT);
			}
		});
		
		zAnglePlus.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				camera.incrementZAngle(ANGLE_INCREMENT);
			}
		});
		
		orientationPanel.add(new JPanel());
		orientationPanel.add(xAnglePlus);
		orientationPanel.add(new JPanel());
		orientationPanel.add(yAngleMinus);
		orientationPanel.add(new JPanel());
		orientationPanel.add(yAnglePlus);	
		orientationPanel.add(zAnglePlus);
		orientationPanel.add(xAngleMinus);
		orientationPanel.add(zAngleMinus);
		
		orientationPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		orientationPanel.setPreferredSize(new Dimension(200, 200));
		
		return orientationPanel;
	}
	
	/**
	 * Initialize time control panel
	 * 
	 * @return
	 */
	private JPanel setupTimePanel() {
		JPanel timePanel = new JPanel(new FlowLayout());
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
		
		return timePanel;
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
		Thread universeThread = new Thread(new UniverseLoop(canvas, entities, camera));
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
		private ThreeDimensionalEntityCanvas canvas;
		private Collection<BasePhysicalEntity> entities;
		private ThreeDimensionalViewCamera camera;
		
		public UniverseLoop(ThreeDimensionalEntityCanvas canvas, Collection<BasePhysicalEntity> entities, ThreeDimensionalViewCamera camera) {
			this.canvas = canvas;
			this.entities = entities;
			this.camera = camera;					
		}
		
		@Override
		public void run() {
			cycleTime = System.currentTimeMillis();
			
			while (true)  {

				// Wait an appropriate amount of time, so that the frame rate is progressing constantly.
				synchFramerate();
				
				// tell graphics to repaint
				canvas.updateGraphics();

				// Perform physics simulations
				if (running) updateUniverseState();					
				
				// TODO: Perhaps create a separate pause camera button?
				updateCameraState();
			}
			
		}
		
		private void synchFramerate() {
			
			cycleTime = cycleTime + frameDelay;
			long difference = cycleTime - System.currentTimeMillis();
		
			try {
				// if frameDelay has already occurred since last cycle time, do not sleep.
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
		
		private void updateCameraState() {
			camera.move();			
		}
	}
	
}
