package entity;

public interface IDimensionalEntity {

    public void setX(double x);
    public double getX();
    public void setDeltaX(double deltaX);
    public void addDeltaX(double deltaDeltaX);
    public double getDeltaX();
    public void moveX(double deltaX);

    public void setY(double y);
    public double getY();
    public void setDeltaY(double deltaY);
    public void addDeltaY(double deltaDeltaY);
    public double getDeltaY();
    public void moveY(double deltaY);

	public void setZ(double z);
	public double getZ();
	public void setDeltaZ(double deltaZ);
	public void addDeltaZ(double deltaDeltaZ);
	public double getDeltaZ();
	public void moveZ(double deltaZ);

    public void move();
	public double getDistance(IDimensionalEntity other);


    public static double getDistance(IDimensionalEntity a, IDimensionalEntity b) {
        return Math.sqrt(
                Math.pow((a.getX() - b.getX()), 2) +
                        Math.pow((a.getY() - b.getY()), 2) +
                        Math.pow((a.getZ() - b.getZ()), 2)
        );
    }
}
