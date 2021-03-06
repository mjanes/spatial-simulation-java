package spatial.entity;

public interface IMobileDimensionalEntity extends IDimensionalEntity {

    void setDeltaX(double deltaX);

    void addDeltaX(double deltaDeltaX);

    double getDeltaX();

    void moveX(double deltaX);

    void setDeltaY(double deltaY);

    void addDeltaY(double deltaDeltaY);

    double getDeltaY();

    void moveY(double deltaY);

    void setDeltaZ(double deltaZ);

    void addDeltaZ(double deltaDeltaZ);

    double getDeltaZ();

    void moveZ(double deltaZ);

    void move();


}
