package entity;

import java.util.List;

/**
 * Interface to allow entities to be connected to each other. 
 * At the moment, a connection between two entities either exists or it doesn't.
 * 
 * TODO: Allow differing types of connections. Possibly creating a connection object.
 * 
 * TODO: Graphically display connections, which, given how entities don't know where they
 * will be displayed on screen, is going to require some rethinking of the entity canvases.
 * 
 * @author mjanes
 *
 */
public interface IConnectedEntity {
	
	public void addConnection(IConnectedEntity entity);
	public void removeConnection(IConnectedEntity entity);
	public List<IConnectedEntity> getConnections();

}
