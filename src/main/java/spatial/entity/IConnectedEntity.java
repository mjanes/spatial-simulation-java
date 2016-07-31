package spatial.entity;

import java.util.List;

/**
 * Interface to allow entities to be connected to each other.
 * At the moment, a connection between two entities either exists or it doesn't.
 * <p>
 * TODO: Allow differing types of connections. Possibly creating a connection object.
 * <p>
 * TODO: Graphically display connections, which, given how entities don't know where they
 * will be displayed on screen, is going to require some rethinking of the entity canvases.
 */
interface IConnectedEntity {

    void addConnection(IConnectedEntity entity);

    void removeConnection(IConnectedEntity entity);

    List<IConnectedEntity> getConnections();

}
