package spatial.physics;

import spatial.entity.SpatialEntity;

import java.util.List;

public class UniversePhysics {

    /**
     * Run round of physics
     *
     * @param entities Entities to run universe physics on.
     */
    public static synchronized List<SpatialEntity> updateUniverseState(List<SpatialEntity> entities) {
        if (entities == null || entities.size() == 0) return entities;

        entities = CollisionPhysics.applyCollisions(entities);

        GravitationalPhysics.gravity(entities);

        entities.parallelStream().forEach(SpatialEntity::move);

        return entities;
    }

}
