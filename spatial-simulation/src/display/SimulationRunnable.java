package display;

/**
 * Running the universe and camera
 *
 * Learning about this from: http://www.javalobby.org/forums/thread.jspa?threadID=16867&tstart=0
 *
 * Pieces/threads we need:
 * 1) Simulation logic of a given phase of the universe. Currently, that is applying gravity and moving the entities.
 * 2) Render the universe state. Does not have to be phase that was just simulated. Can be the previous phase, so
 * 	that those two phase have occurred on separate threads.
 * 3) Sleep until a certain amount of time has passed, and then draw the graphics rendered in phase 2,
 * thus ensuring a consistent frame rate.
 *
 * Created by mjanes on 5/17/2014.
 */
public abstract class SimulationRunnable implements Runnable {

    protected ISimulationContainer mContainer;

    protected long cycleTime;

    public SimulationRunnable(ISimulationContainer container) {
        mContainer = container;
    }

    @Override
    public void run() {
        cycleTime = System.currentTimeMillis();

        while (true)  {

            // Wait an appropriate amount of time, so that the frame rate is progressing constantly.
            synchFramerate();

            increment();
        }
    }

    protected void synchFramerate() {

        cycleTime = cycleTime + mContainer.getFrameDelay();
        long difference = cycleTime - System.currentTimeMillis();

        try {
            // if frameDelay has already occurred since last cycle time, do not sleep.
            Thread.sleep(Math.max(0,  difference));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    abstract protected void increment();

    public static interface ISimulationContainer {
        public int getFrameDelay();
        public boolean isRunning();
    }
}
