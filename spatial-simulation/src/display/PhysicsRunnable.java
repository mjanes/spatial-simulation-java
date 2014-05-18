package display;

import camera.ThreeDimensionalViewCamera;
import entity.BasePhysicalEntity;
import physics.GravitationalPhysics;

import java.util.List;

/**
 * Created by mjanes on 5/17/2014.
 */
public class PhysicsRunnable extends SimulationRunnable {

    private List<BasePhysicalEntity> mEntities;

    public PhysicsRunnable(ISimulationContainer container, List<BasePhysicalEntity> entities) {
        super(container);
        mEntities = entities;
    }

    protected void increment() {
        // Perform physics simulations
        if (mContainer.isRunning()) GravitationalPhysics.updateUniverseState(mEntities);
    }
}
