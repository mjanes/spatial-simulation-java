package main;

import java.util.Collection;

import physics.GravitationalPhysics;
import setup.Setup;
import display.UIFrame;
import entity.BasePhysicalEntity;

public class Main {
	
	protected static UIFrame frame;
	protected static Collection<BasePhysicalEntity> entities;
	

	public static void main(String[] args) {
		// Rough idea:
		// 	1) Launch UI
		frame = new UIFrame(1000, 1000);
		
		//	2) Initialize objects
		entities = Setup.create();
		frame.setEntities(entities);
		
		//	3) Begin running physics on things
		Thread universeThread = new Thread(new UniverseLoop());
		universeThread.setPriority(Thread.MIN_PRIORITY);
		universeThread.start();
	}


	/**
	 * Learning about this from: http://www.javalobby.org/forums/thread.jspa?threadID=16867&tstart=0
	 * 
	 * TODO: I need to rewrite this so that it handles threading issues more intelligently. We need to
	 * break this down into a couple of discrete sections, that may or may not all fit into a timer task.
	 * Probably won't fit into the same timer task, actually.
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
		
		@Override
		public void run() {
			cycleTime = System.currentTimeMillis();
						
			while (true)  {
				updateUniverseState();
				
				// tell graphics to repaint
				frame.updateGraphics();
				
				synchFramerate();								
			}
			
		}
		
		private void synchFramerate() {
			cycleTime = cycleTime + UIFrame.FRAME_DELAY;
			long difference = cycleTime - System.currentTimeMillis();
			
			try {
				Thread.sleep(Math.max(0,  difference));				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private static void updateUniverseState() {
		// run round of physics
		GravitationalPhysics.gravity(entities);
		for (BasePhysicalEntity entity : entities) {
			entity.move();
		}		
	}
}
