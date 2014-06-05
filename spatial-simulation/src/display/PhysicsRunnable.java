package display;

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
        if (entities == null) throw new IllegalArgumentException();
        mEntities = entities;
    }

    protected void increment() {
        // Perform physics simulations
        if (mContainer.isRunning()) GravitationalPhysics.updateUniverseState(mEntities);
    }
}
