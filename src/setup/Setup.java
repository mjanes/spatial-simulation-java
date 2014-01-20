package setup;

import java.util.ArrayList;
import java.util.Collection;

import entity.BasePhysicalEntity;

public class Setup {

	public static Collection<BasePhysicalEntity> create() {
		ArrayList<BasePhysicalEntity> entities = new ArrayList<BasePhysicalEntity>();
		
		BasePhysicalEntity entityA = new BasePhysicalEntity(100, 100, 0, 500);
		entityA.setDeltaX(1.0);
		entities.add(entityA);
				
		entities.add(new BasePhysicalEntity(300, 300, 0, 4000));
		
		return entities;
	}
	
}
