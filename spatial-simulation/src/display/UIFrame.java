package display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicArrowButton;

import physics.GravitationalPhysics;
import camera.ThreeDimensionalViewCamera;
import entity.BasePhysicalEntity;
import setup.Setup;

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
public class UIFrame extends JFrame implements SimulationRunnable.ISimulationContainer {
	
	private static final long serialVersionUID = 1L;
	
	// Ideally, we don't want these static, but for convenience at the moment
	private int mFrameDelay = 10; // Milliseconds between each frame painting

    // Whether or not the universe is running
	private boolean mRunning = true;
	
	private List<BasePhysicalEntity> mEntities = new ArrayList<>();
	
	// The canvas that is the display screen and JPanel that holds it	
	private ThreeDimensionalEntityCanvas mCanvas;

    // Check if volatile is appropriate. Will affect the universe loop thread.
	private volatile ThreeDimensionalViewCamera mCamera;
	
	private static final double CAMERA_ACCELERATION = 0.1;
	private static final int ANGLE_INCREMENT = 2;

    private Thread mUniverseThread;

	/**
	 * UI setup
	 * 
	 * @param width width of panel
	 * @param height height of panel
	 */
	public UIFrame(int width, int height) {	

		// Initiate the mCamera
		mCamera = new ThreeDimensionalViewCamera(0, 0, 0);
		
		
		// Setup canvas
		mCanvas = new ThreeDimensionalEntityCanvas(width, height, mCamera);
		
		
		// The navigation panel, which is responsible for moving around the universe, ie, changing how it
		// is displayed in the canvas.
		JPanel navigationPanel = setupNavigationPanel();		
		
		// Panel for controlling the orientation of the mCamera
		JPanel orientationPanel = setupOrientationPanel();

		// This panel contains buttons for changing the speed of the simulation
		JPanel timePanel = setupTimePanel();

        JPanel setupPanel = setupSetupPanel();


		// Set up control panel, which will have buttons to manipulate view of canvas
        JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		controlPanel.setPreferredSize(new Dimension(200, height));

		
		// Add different panels to control panel
		controlPanel.add(navigationPanel);
		controlPanel.add(orientationPanel);
		controlPanel.add(timePanel);
        controlPanel.add(setupPanel);
		controlPanel.setBorder(BorderFactory.createLineBorder(Color.black));

		
		// Final packing of everything into content pane and display
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(controlPanel, BorderLayout.WEST);
		contentPane.add(mCanvas, BorderLayout.CENTER);
		pack();		
		setVisible(true);	

		// initialize the buffer of the canvas
		mCanvas.initBuffer();

        // Start the mCamera thread
        Thread cameraThread = new Thread(new CameraRunnable(this, mCanvas, mCamera));
        cameraThread.setPriority(Thread.MIN_PRIORITY);
        cameraThread.start();
	}
	
	
	/**
	 * Navigation panel, for moving the location of the camera, with up/down/left/right buttons
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
		
        eastButton.addActionListener(e -> mCamera.addDeltaSelfX(CAMERA_ACCELERATION));
        westButton.addActionListener(e -> mCamera.addDeltaSelfX(-CAMERA_ACCELERATION));
		northButton.addActionListener(e -> mCamera.addDeltaSelfY(-CAMERA_ACCELERATION));
		southButton.addActionListener(e -> mCamera.addDeltaSelfY(CAMERA_ACCELERATION));
		forwardButton.addActionListener(e -> mCamera.addDeltaSelfZ(CAMERA_ACCELERATION));
		backwardsButton.addActionListener(e -> mCamera.addDeltaSelfZ(-CAMERA_ACCELERATION));
		
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
		JButton xAngleMinus = new JButton("xAngle plus");
		JButton xAnglePlus = new JButton("xAngle minus");
		JButton yAnglePlus = new JButton("yAngle plus");
		JButton zAngleMinus = new JButton("zAngle minus");
		JButton zAnglePlus = new JButton("zAngle Plus");
		
		Dimension buttonDimension = new Dimension(66, 66);
		yAngleMinus.setPreferredSize(buttonDimension);
		xAngleMinus.setPreferredSize(buttonDimension);
		xAnglePlus.setPreferredSize(buttonDimension);
		yAnglePlus.setPreferredSize(buttonDimension);
		zAngleMinus.setPreferredSize(buttonDimension);
		zAnglePlus.setPreferredSize(buttonDimension);
		
		xAngleMinus.addActionListener(e -> mCamera.incrementRelativeXAngle(-ANGLE_INCREMENT));
		xAnglePlus.addActionListener(e -> mCamera.incrementRelativeXAngle(ANGLE_INCREMENT));
		yAngleMinus.addActionListener(e -> mCamera.incrementRelativeYAngle(-ANGLE_INCREMENT));
		yAnglePlus.addActionListener(e -> mCamera.incrementRelativeYAngle(ANGLE_INCREMENT));
		zAngleMinus.addActionListener(e -> mCamera.incrementRelativeZAngle(-ANGLE_INCREMENT));
		zAnglePlus.addActionListener(e -> mCamera.incrementRelativeZAngle(ANGLE_INCREMENT));

		orientationPanel.add(new JPanel());
		orientationPanel.add(xAngleMinus);
		orientationPanel.add(new JPanel());
		orientationPanel.add(yAngleMinus);
		orientationPanel.add(new JPanel());
		orientationPanel.add(yAnglePlus);	
		orientationPanel.add(zAnglePlus);
		orientationPanel.add(xAnglePlus);
		orientationPanel.add(zAngleMinus);
		
		orientationPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		orientationPanel.setPreferredSize(new Dimension(200, 200));
		
		return orientationPanel;
	}
	
	/**
	 * Initialize time control panel
	 */
	private JPanel setupTimePanel() {
		JPanel timePanel = new JPanel(new FlowLayout());
		JButton pauseButton = new JButton("Pause");
        JButton incrementButton = new JButton("Increment");
		JButton playButton = new JButton("Play");
		JButton increaseSpeedButton = new JButton("Increase Speed");
		JButton decreaseSpeedButton = new JButton("Decrease Speed");
		
		pauseButton.addActionListener(e -> setRunning(false));
        incrementButton.addActionListener(e -> GravitationalPhysics.updateUniverseState(mEntities));
		playButton.addActionListener(e -> setRunning(true));
		increaseSpeedButton.addActionListener(e -> incrementFrameDelay(-1));
		decreaseSpeedButton.addActionListener(e -> incrementFrameDelay(1));

		timePanel.add(pauseButton);
        timePanel.add(incrementButton);
		timePanel.add(playButton);
        // TODO: Display current speed.
		timePanel.add(decreaseSpeedButton);
		timePanel.add(increaseSpeedButton);
		
		
		timePanel.setBorder(BorderFactory.createLineBorder(Color.black));
		timePanel.setPreferredSize(new Dimension(200, 100));
		
		return timePanel;
	}


    public JPanel setupSetupPanel() {
        JPanel setupPanel = new JPanel(new FlowLayout());

        JButton setupButton = new JButton("Setup");
        setupButton.addActionListener(e -> setEntities(Setup.create()));
        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> start());
        setupPanel.add(setupButton);
        setupPanel.add(startButton);

        return setupPanel;
    }


	/***************************************************************************************************
	 * Utility/General
	 **************************************************************************************************/
	
	public void setEntities(List<BasePhysicalEntity> entities) {
		mEntities = entities;
		mCanvas.setEntities(mEntities);
	}
	
	public void start() {
        if (mUniverseThread != null) mUniverseThread.interrupt();
        mUniverseThread = new Thread(new PhysicsRunnable(this, mEntities));
		mUniverseThread.setPriority(Thread.MIN_PRIORITY);
		mUniverseThread.start();
	}

    @Override
    public synchronized int getFrameDelay() {
        return mFrameDelay;
    }

    public synchronized void incrementFrameDelay(int increment) {
        mFrameDelay += increment;
    }

    @Override
    public synchronized boolean isRunning() {
        return mRunning;
    }

    public synchronized void setRunning(boolean running) {
        mRunning = running;
    }

}
