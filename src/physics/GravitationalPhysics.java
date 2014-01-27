package physics;

import java.util.Collection;

import entity.BasePhysicalEntity;

/**
 * Do we want this to extend an interface of 'Physics'? Maybe? What do I want the base physics operation to be? Well, something that takes a
 * collection of entities? And... hrm... how do we want that to work? I mean... so, all these objects are going to be passed as pointers, so, we
 * can edit the objects themselves. But, some of the entities might be destroyed or new ones created.
 * 
 * Yeah, I suppose it would probably be cleaner to not have this be a separate class, but to put everything in the entities themselves. So...
 * well, makes it harder to use the same code for both this and for social netwok display, but that's probably just premature optimization.
 * 
 * @author mjanes
 */
public class GravitationalPhysics {
	
	private static final double GRAVITATIONAL_CONSTANT = 0.1;
	
	/** 
	 * I might want to rename this to threeDimensionalGravitationalPhysics to separate it from two dimensional objects. 
	 * Might just have both in here for now.
	 * 
	 * Another question, can we depend upon this being sorted when it is giving to us? 
	 * Well, for this one, it's not going to matter, as it's always going to be O(n^2)
	 * 
	 * @param entities
	 * @return
	 */
	public static Collection<BasePhysicalEntity> gravity(Collection<BasePhysicalEntity> entities) {
		// At the moment this is not allowing for 
		for (BasePhysicalEntity puller : entities) {
			for (BasePhysicalEntity pullee : entities) {
				if (puller != pullee) {
					gravitationalPull(puller, pullee);
				}
			}
		}
		return entities;
	}

	/**
	 * Ah great, I have to do math.
	 * 
	 * So F = G ((m1 * m2) / r ^ 2)
	 * 
	 * @param puller
	 * @param pullee
	 */
	public static void gravitationalPull(BasePhysicalEntity puller, BasePhysicalEntity pullee) {
		// Distance between the two points
		double distance = distance(puller, pullee);
		
		// Gravitational force between the two objects
		double force = GRAVITATIONAL_CONSTANT * ((puller.getMass() * pullee.getMass()) / Math.pow(distance, 2)); 
		
		// 
		double ratio = force / distance;

		double distanceX = puller.getX() - pullee.getX();
		double distanceY = puller.getY() - pullee.getY();
		double distanceZ = puller.getZ() - pullee.getZ();		
		
		double deltaDeltaX = (distanceX * ratio) / pullee.getMass();
		double deltaDeltaY = (distanceY * ratio) / pullee.getMass();
		double deltaDeltaZ = (distanceZ * ratio) / pullee.getMass();
		
		
		pullee.addDeltaX(deltaDeltaX);
		pullee.addDeltaY(deltaDeltaY);
		pullee.addDeltaZ(deltaDeltaZ);
	}

	
	
	/******************************************************************************************************
	 *  Distance
	 *  
	 *  This should be perhaps put in a root physics class. Hrm... Still need to determine how to organize 
	 *  all this.
	 ******************************************************************************************************/
	
	public static double distance(BasePhysicalEntity a, BasePhysicalEntity b) {
		return Math.sqrt(
					Math.pow((a.getX() - b.getX()), 2) + 
					Math.pow((a.getY() - b.getY()), 2) + 
					Math.pow((a.getZ() - b.getZ()), 2)
				);
	}
	
}
