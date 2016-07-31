package spatial.display;

import spatial.camera.Camera;
import spatial.entity.SpatialEntity;
import spatial.physics.UniversePhysics;

import java.util.List;

/**
 * Running the universe and camera
 * <p>
 * Learning about this from: http://www.javalobby.org/forums/thread.jspa?threadID=16867&tstart=0
 * <p>
 * Pieces/threads we need:
 * 1) Simulation logic of a given phase of the universe. Currently, that is applying gravity and moving the entities.
 * 2) Render the universe state. Does not have to be phase that was just simulated. Can be the previous phase, so
 * that those two phase have occurred on separate threads.
 * 3) Sleep until a certain amount of time has passed, and then draw the graphics rendered in phase 2,
 * thus ensuring a consistent frame rate.
 */
class SimulationRunnable implements Runnable {

    private final ISimulationContainer mContainer;

    private long mCycleTime;

    private List<SpatialEntity> mEntities;
    private final SpatialEntityCanvas mCanvas;
    private final Camera mCamera;

    SimulationRunnable(ISimulationContainer container, List<SpatialEntity> entities, SpatialEntityCanvas canvas, Camera camera) {
        mContainer = container;
        mEntities = entities;
        mCanvas = canvas;
        mCamera = camera;
    }

    @Override
    public void run() {
        mCycleTime = System.currentTimeMillis();

        while (mContainer.isRunning()) {

            // Wait an appropriate amount of time, so that the frame rate is progressing constantly.
            syncFrameRate();

            increment();
        }
    }

    private void syncFrameRate() {

        mCycleTime = mCycleTime + mContainer.getFrameDelay();
        long difference = mCycleTime - System.currentTimeMillis();

        try {
            // if frameDelay has already occurred since last cycle time, do not sleep. But log it as an error.
            if (difference < 0) {
                System.out.println("Time taken per cycle for " + this.getClass().getSimpleName() + " exceeded frame delay by " + -difference + " milliseconds. " +
                        "Number of entities: " + mEntities.size());
                // Reset cycle time
                mCycleTime = System.currentTimeMillis();
            }
            Thread.sleep(Math.max(0, difference));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void setEntities(List<SpatialEntity> entities) {
        mEntities = entities;
    }

    private void increment() {
        // Perform physics simulations
        if (mContainer.isRunning()) {
            mEntities = UniversePhysics.updateUniverseState(mEntities);
        }

        // tell graphics to repaint
        mCanvas.updateGraphics(mEntities);

        // TODO: Perhaps create a separate pause camera button?
        mCamera.move();
    }

    interface ISimulationContainer {
        int getFrameDelay();

        boolean isRunning();
    }

}
