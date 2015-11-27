package spatial.physics;

import spatial.entity.SpatialEntity;

import java.util.List;

public class CollisionPhysics {

    public static List<SpatialEntity> applyCollisions(List<SpatialEntity> entities) {

        int count = entities.size();
        for (int i = 0; i < count; i++) {
            SpatialEntity a = entities.get(i);
            for (int j = i + 1; j < count; j++) {
                SpatialEntity b = entities.get(j);

                // If this triggers, there is a collision, restart.
                if (a.isOverlapping(b)) {
                    SpatialEntity resultantSpatialEntity = SpatialEntity.collide(a, b);
                    entities.remove(a);
                    entities.remove(b);
                    entities.add(resultantSpatialEntity);
                    i -= 1;
                    j -= 1;
                    count = entities.size();
                    break;
                }
            }
        }

        return entities;
    }
}
