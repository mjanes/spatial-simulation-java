package physics;

import entity.Entity;

import java.util.List;

/**
 * Created by mjanes on 6/10/2014.
 */
public class CollisionPhysics {

    public static List<Entity> applyCollisions(List<Entity> entities) {

        int count = entities.size();
        for (int i = 0; i < count; i++) {
            Entity a = entities.get(i);
            for (int j = i + 1; j < count; j++) {
                Entity b = entities.get(j);

                // If this triggers, there is a collision, restart.
                if (a.isOverlapping(b)) {
                    Entity resultantEntity = Entity.collide(a, b);
                    entities.remove(a);
                    entities.remove(b);
                    entities.add(resultantEntity);
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
