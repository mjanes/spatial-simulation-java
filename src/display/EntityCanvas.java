package display;

import java.util.Collection;

import entity.BasePhysicalEntity;

public interface EntityCanvas {
	
	public void setEntities(Collection<BasePhysicalEntity> entities);
	
	public void updateGraphics();
	

}
