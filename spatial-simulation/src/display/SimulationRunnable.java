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

    protected final ISimulationContainer mContainer;

    protected long mCycleTime;

    public SimulationRunnable(ISimulationContainer container) {
        mContainer = container;
    }

    @Override
    public void run() {
        mCycleTime = System.currentTimeMillis();

        while (mContainer.isRunning())  {

            // Wait an appropriate amount of time, so that the frame rate is progressing constantly.
            syncFrameRate();

            increment();
        }
    }

    protected void syncFrameRate() {

        mCycleTime = mCycleTime + mContainer.getFrameDelay();
        long difference = mCycleTime - System.currentTimeMillis();

        try {
            // if frameDelay has already occurred since last cycle time, do not sleep. But log it as an error.
            if (difference < 0) {
                System.out.println("Time taken per cycle for " + this.getClass().getSimpleName() + " exceeded frame delay by " + -difference + " milliseconds.");
                // Reset cycle time
                mCycleTime = System.currentTimeMillis();
            }
            Thread.sleep(Math.max(0, difference));
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
