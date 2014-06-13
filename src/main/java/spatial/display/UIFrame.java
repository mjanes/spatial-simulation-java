package spatial.display;

import spatial.camera.Camera;
import spatial.entity.SpatialEntity;
import spatial.physics.UniversePhysics;
import spatial.setup.Setup;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

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
	private int mFrameDelay = 80; // Milliseconds between each frame painting

    // Whether or not the universe is running
    // Note that the camera is currently part of the universe
	private boolean mRunning = true;
	
	private List<SpatialEntity> mEntities = new ArrayList<>();

    // Check if volatile is appropriate. Will affect the universe loop thread.
	private volatile Camera mCamera;
	
	private static final double CAMERA_ACCELERATION = 0.5;
	private static final int ANGLE_INCREMENT = 2;

    private SimulationRunnable mSimulationRunnable;

	/**
	 * UI setup
	 * 
	 * @param width width of panel
	 * @param height height of panel
	 */
	public UIFrame(int width, int height) {	

		// Initiate the mCamera
		mCamera = new Camera(0, 0, 0);
		
		
		// Setup canvas
        SpatialEntityCanvas canvas = new SpatialEntityCanvas(width, height, mCamera);
		
		
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
		contentPane.add(canvas, BorderLayout.CENTER);
		pack();		
		setVisible(true);	

        // Exit listener to force shutdown of universe thread on window close
        WindowListener exitListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(1);
            }
        };
        addWindowListener(exitListener);

		// initialize the buffer of the canvas
		canvas.initBuffer();

        // Start the mCamera thread
        mSimulationRunnable = new SimulationRunnable(this, mEntities, canvas, mCamera);
        Thread simulationThread = new Thread(mSimulationRunnable);
        simulationThread.setPriority(Thread.MIN_PRIORITY);
        simulationThread.start();
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
		northButton.addActionListener(e -> mCamera.addDeltaSelfY(CAMERA_ACCELERATION));
		southButton.addActionListener(e -> mCamera.addDeltaSelfY(-CAMERA_ACCELERATION));
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
		JButton pauseUnpauseButton = new JButton("Pause/Unpause");
        JButton incrementButton = new JButton("Increment");
		JButton increaseSpeedButton = new JButton("Increase Speed");
		JButton decreaseSpeedButton = new JButton("Decrease Speed");
		
		pauseUnpauseButton.addActionListener(e -> setRunning(!isRunning()));
        incrementButton.addActionListener(e -> UniversePhysics.updateUniverseState(mEntities));
		increaseSpeedButton.addActionListener(e -> incrementFrameDelay(-1));
		decreaseSpeedButton.addActionListener(e -> incrementFrameDelay(1));

		timePanel.add(pauseUnpauseButton);
        timePanel.add(incrementButton);

        // TODO: Display current speed.
		timePanel.add(decreaseSpeedButton);
		timePanel.add(increaseSpeedButton);
		
		
		timePanel.setBorder(BorderFactory.createLineBorder(Color.black));
		timePanel.setPreferredSize(new Dimension(200, 100));
		
		return timePanel;
	}


    public JPanel setupSetupPanel() {
        JPanel setupPanel = new JPanel(new FlowLayout());

        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> setEntities(Setup.create()));
        setupPanel.add(startButton);

        return setupPanel;
    }


	/***************************************************************************************************
	 * Utility/General
	 **************************************************************************************************/

	public void setEntities(List<SpatialEntity> entities) {
		mEntities = entities;
		if (mSimulationRunnable != null) {
            mSimulationRunnable.setEntities(mEntities);
        }
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
