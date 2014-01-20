package entity;

public interface ITwoDimensionalEntity {
	public void setX(double x);
	public double getX();
	public void setDeltaX(double deltaX);
	public double getDeltaX();
	public void setY(double y);
	public double getY();
	public void setDeltaY(double deltaY);
	public double getDeltaY();
	public void move();
}
