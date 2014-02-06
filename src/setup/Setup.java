package setup;

import java.util.ArrayList;
import java.util.Collection;

import entity.BasePhysicalEntity;

public class Setup {
	
	private static ArrayList<BasePhysicalEntity> entities;

	public static Collection<BasePhysicalEntity> create() {
		entities = new ArrayList<BasePhysicalEntity>();
		basicOrbitCouple();
		
		//randomSet(10);
		
		return entities;
	}
	
	private static void basicOrbitCouple() {

		BasePhysicalEntity entityA = new BasePhysicalEntity(100, 100, 0, 500);
		entityA.setDeltaX(1.0);
		entities.add(entityA);
				
		entities.add(new BasePhysicalEntity(300, 300, 0, 8000));
	}
	
	private static void randomSet(int n) {
		BasePhysicalEntity newEntity;
		for (int i = 0; i < n; ++i) {
			newEntity = new BasePhysicalEntity(Math.random() * 600, Math.random() * 600, 0, Math.random() * 100);
			newEntity.setDeltaX(Math.random() / 10);
			newEntity.setDeltaY(Math.random() / 10);
			entities.add(newEntity);
		}
	}
	
}
