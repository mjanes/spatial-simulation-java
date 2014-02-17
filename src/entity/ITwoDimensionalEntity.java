package entity;

public interface ITwoDimensionalEntity {
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
	public void move();
}
