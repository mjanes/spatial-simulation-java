package display;

import entity.Entity;
import physics.GravitationalPhysics;

import java.util.List;

/**
 * Created by mjanes on 5/17/2014.
 */
public class PhysicsRunnable extends SimulationRunnable {

    private List<Entity> mEntities;

    public PhysicsRunnable(ISimulationContainer container, List<Entity> entities) {
        super(container);
        if (entities == null) throw new IllegalArgumentException();
        mEntities = entities;
    }

    protected void increment() {
        // Perform physics simulations
        if (mContainer.isRunning()) GravitationalPhysics.updateUniverseState(mEntities);
    }
}
