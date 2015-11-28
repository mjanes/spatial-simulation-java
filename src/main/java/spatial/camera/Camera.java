package spatial.camera;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import spatial.entity.IDimensionalEntity;
import spatial.entity.IMobileDimensionalEntity;

/**
 * A camera in three dimensional space.
 * <p>
 * And I understand that I'm probably using non-traditional terms for everything here
 * I suppose I should be using: https://en.wikipedia.org/wiki/Euler_angles
 * But at the moment I'm trying to teach myself things.
 * <p>
 * May change to official Euler angles later. Or pitch/yaw/roll.
 */
public class Camera implements IMobileDimensionalEntity {

    private double mX;
    private double mY;
    private double mZ;

    private double mDeltaX;
    private double mDeltaY;
    private double mDeltaZ;

    // Matrices for display math
    private final Array2DRowRealMatrix mTranslationMatrix = new Array2DRowRealMatrix(new double[][]{
            {1, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 1, 0},
            {0, 0, 0, 1}});

    // Orientation values
    // All of these are angles
    // Such that if the camera is located at mX = 0, mY = 0, mZ = 1,
    // With orientation mXAngle = 0, mYAngle = 0, and mZAngle = 0
    // A point located at mX = 0, mY = 0, mZ = 0, would appear in the center of the field of view

    // If the camera is located at mX = 0, mY = 0, mZ = -1,
    // With orientation mXAngle = 0, mYAngle = 0, and mZAngle = 0
    // A point located at mX = 0, mY = 180, mZ = 0, would appear in the center of the field of view

    // Imagine each axis, and each of these angles as a clockwise rotation around that axis
    private double mXAngle;
    private double mYAngle;
    private double mZAngle;

    private final Array2DRowRealMatrix mXRotationMatrix = new Array2DRowRealMatrix(new double[][]{
            {1, 0, 0, 0},
            {0, Math.cos(-Math.toRadians(mXAngle)), Math.sin(Math.toRadians(mXAngle)), 0},
            {0, Math.sin(-Math.toRadians(mXAngle)), -Math.cos(Math.toRadians(mXAngle)), 0},
            {0, 0, 0, 1}});

    private final Array2DRowRealMatrix mYRotationMatrix = new Array2DRowRealMatrix(new double[][]{
            {-Math.cos(Math.toRadians(mYAngle)), 0, -Math.sin(Math.toRadians(mYAngle)), 0},
            {0, 1, 0, 0},
            {Math.sin(Math.toRadians(mYAngle)), 0, -Math.cos(Math.toRadians(mYAngle)), 0},
            {0, 0, 0, 1}});

    private final Array2DRowRealMatrix mZRotationMatrix = new Array2DRowRealMatrix(new double[][]{
            {-Math.cos(Math.toRadians(mZAngle)), Math.sin(Math.toRadians(mZAngle)), 0, 0},
            {-Math.sin(Math.toRadians(mYAngle)), -Math.cos(Math.toRadians(mYAngle)), 0, 0},
            {0, 0, 1, 0},
            {0, 0, 0, 1}});


    public Camera(double x, double y, double z) {
        this(x, y, z, 0, 0, 0);
    }

    private Camera(double x, double y, double z, double xAngle, double yAngle, double zAngle) {
        mX = x;
        mY = y;
        mZ = z;
        mXAngle = xAngle;
        mYAngle = yAngle;
        mZAngle = zAngle;
    }

    /**********************************************************************
     * Movement, positions, getters, setters
     **********************************************************************/

    @Override
    public void setX(double x) {
        mX = x;
        mTranslationMatrix.setEntry(0, 3, -x);
    }

    @Override
    public double getX() {
        return mX;
    }

    @Override
    public void setDeltaX(double deltaX) {
        mDeltaX = deltaX;
    }

    @Override
    public void addDeltaX(double deltaDeltaX) {
        mDeltaX += deltaDeltaX;
    }

    @Override
    public double getDeltaX() {
        return mDeltaX;
    }

    @Override
    public void moveX(double deltaX) {
        setX(mX + deltaX);
    }

    @Override
    public void setY(double y) {
        mY = y;
        mTranslationMatrix.setEntry(1, 3, -y);
    }

    @Override
    public double getY() {
        return mY;
    }

    @Override
    public void setDeltaY(double deltaY) {
        mDeltaY = deltaY;
    }

    @Override
    public void addDeltaY(double deltaDeltaY) {
        mDeltaY += deltaDeltaY;
    }

    @Override
    public double getDeltaY() {
        return mDeltaY;
    }

    @Override
    public void moveY(double deltaY) {
        setY(mY + deltaY);
    }

    @Override
    public void setZ(double z) {
        mZ = z;
        mTranslationMatrix.setEntry(2, 3, -z);
    }

    @Override
    public double getZ() {
        return mZ;
    }

    @Override
    public void setDeltaZ(double deltaZ) {
        mDeltaZ = deltaZ;
    }

    @Override
    public void addDeltaZ(double deltaDeltaZ) {
        mDeltaZ += deltaDeltaZ;
    }

    @Override
    public double getDeltaZ() {
        return mDeltaZ;
    }

    @Override
    public void moveZ(double deltaZ) {
        setZ(mZ + deltaZ);
    }

    @Override
    public void move() {
        moveX(mDeltaX);
        moveY(mDeltaY);
        moveZ(mDeltaZ);
    }


    /*******************************************************************************************************************
     * Distance calculations
     ******************************************************************************************************************/

    @Override
    public double getDistance(IDimensionalEntity other) {
        return IDimensionalEntity.getDistance(this, other);
    }

    @Override
    public Array2DRowRealMatrix getR4Matrix() {
        return null;
    }


    /*********************************************************************
     * Angles
     *********************************************************************/

    private double getXAngle() {
        return mXAngle;
    }

    private void setXAngle(double xAngle) {
        mXAngle = xAngle % 360;
        mXRotationMatrix.setEntry(1, 1, -Math.cos(Math.toRadians(mXAngle)));
        mXRotationMatrix.setEntry(1, 2, Math.sin(Math.toRadians(mXAngle)));
        mXRotationMatrix.setEntry(2, 1, -Math.sin(Math.toRadians(mXAngle)));
        mXRotationMatrix.setEntry(2, 2, -Math.cos(Math.toRadians(mXAngle)));
    }

    private void incrementXAngle(double increment) {
        setXAngle(mXAngle + increment);
    }

    private double getYAngle() {
        return mYAngle;
    }

    private void setYAngle(double yAngle) {
        mYAngle = yAngle % 360;
        mYRotationMatrix.setEntry(0, 0, -Math.cos(Math.toRadians(mYAngle)));
        mYRotationMatrix.setEntry(0, 2, -Math.sin(Math.toRadians(mYAngle)));
        mYRotationMatrix.setEntry(2, 0, Math.sin(Math.toRadians(mYAngle)));
        mYRotationMatrix.setEntry(2, 2, -Math.cos(Math.toRadians(mYAngle)));
    }

    private void incrementYAngle(double increment) {
        setYAngle(mYAngle + increment);
    }

    private double getZAngle() {
        return mZAngle;
    }

    private void setZAngle(double zAngle) {
        mZAngle = zAngle % 360;
        mZRotationMatrix.setEntry(0, 0, -Math.cos(Math.toRadians(mZAngle)));
        mZRotationMatrix.setEntry(0, 1, Math.sin(Math.toRadians(mZAngle)));
        mZRotationMatrix.setEntry(1, 0, -Math.sin(Math.toRadians(mZAngle)));
        mZRotationMatrix.setEntry(1, 1, -Math.cos(Math.toRadians(mZAngle)));
    }

    private void incrementZAngle(double increment) {
        setZAngle(mZAngle + increment);
    }

    /**
     * Get the angle between this entity, based on its location, the location of the other entity, and this entity's
     * current rotation.
     *
     * TODO: How do we want to return this?
     */
    public ? getAngleTo(IDimensionalEntity other){

    }

    /********************************************************************************
     * Incrementing angles relative to the orientation of the screen
     ********************************************************************************/

    /**
     * Incrementing the relative mX angle, meaning, moving the camera angle up and
     * down relative to the monitor. If the mZ angle is 90 degrees, then incrementing
     * the mX angle 1 degree would instead increment the mY angle 1 degree
     *
     * @param increment
     */
    public void incrementRelativeXAngle(double increment) {
        double angle = Math.toRadians(getZAngle());
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double xAngle = increment * cos;
        double yAngle = increment * sin;
        incrementXAngle(xAngle);
        incrementYAngle(yAngle);
    }

    public void incrementRelativeYAngle(double increment) {
        double angle = Math.toRadians((90 + getZAngle()) % 360);
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double xAngle = increment * cos;
        double yAngle = increment * sin;
        incrementXAngle(xAngle);
        incrementYAngle(yAngle);
    }

    /**
     * TODO: Check that past self knew what he was doing when wrote this
     */
    public void incrementRelativeZAngle(double increment) {
        incrementZAngle(increment);
    }


    /****************************************************************************************************
     * Movement functions relative to the direction the camera is facing in.
     * <p>
     * After some initial math on this was broken, turning towards references:
     * http://www.mathsisfun.com/polar-cartesian-coordinates.html
     * https://en.wikipedia.org/wiki/Spherical_coordinate_system
     * https://en.wikipedia.org/wiki/List_of_common_coordinate_transformations#To_Cartesian_coordinates
     *****************************************************************************************************/

    public void addDeltaSelfX(final double delta) {
        double deltaX;
        double angle;

        // Handle rotation on Y axis
        angle = Math.toRadians(getYAngle());
        deltaX = delta * Math.cos(angle);
        final double deltaZ = delta * Math.sin(angle);

        // Handle rotation on Z axis
        angle = Math.toRadians(getZAngle());
        deltaX = deltaX * Math.cos(angle);
        final double deltaY = deltaX * Math.sin(angle);

        // Apply the motion
        addDeltaX(deltaX);
        addDeltaY(deltaY);
        addDeltaZ(deltaZ);
    }

    public void addDeltaSelfY(final double delta) {
        double deltaY;
        double angle;

        // Handle rotation on X axis
        angle = Math.toRadians(getXAngle());
        deltaY = delta * Math.cos(angle);
        final double deltaZ = delta * Math.sin(angle);

        // Handle rotation on Z axis
        angle = Math.toRadians(getZAngle());
        deltaY = deltaY * Math.cos(angle);
        final double deltaX = deltaY * Math.sin(angle);

        // Apply the new motion
        addDeltaX(deltaX);
        addDeltaY(deltaY);
        addDeltaZ(deltaZ);
    }


    public void addDeltaSelfZ(final double delta) {
        double deltaZ;
        double angle;

        // Handle rotation on Y axis
        angle = Math.toRadians(getYAngle());
        final double deltaX = delta * Math.sin(angle);
        deltaZ = delta * Math.cos(angle);

        // Handle rotation on X axis
        angle = Math.toRadians(getXAngle());
        deltaZ = deltaZ * Math.cos(angle);
        final double deltaY = deltaZ * Math.sin(angle);

        // Apply the new motion
        addDeltaX(deltaX);
        addDeltaY(deltaY);
        addDeltaZ(deltaZ);
    }


    /***************************************************************************************************
     * Methods to rotate the camera around the origin
     *
     * May later wish to add methods that simultaneously do this, and rotate, so that the camera may
     * maintain observation of something at the point
     *
     * Need to read through this more: https://en.wikipedia.org/wiki/Axes_conventions
     ***************************************************************************************************/

    public void moveNorthSouth(final double distance, double subjectX, double subjectY, double subjectZ){
        // 1. Find the angle between the camera's location and the subject xyz
        // 2. Add the appropriate deltas (Or just move xyz without delta)
    }

    public void moveEastWest(final double distance, double subjectX, double subjectY, double subjectZ){
        // 1. Find the angle between the camera's location and the subject xyz
        // 2. Add the appropriate deltas (Or just move xyz without delta)
    }

    public void moveUpDown(final double distance, double subjectX, double subjectY, double subjectZ){
        // 1. Find the angle between the camera's location and the subject xyz
        // 2. Add the appropriate deltas (Or just move xyz without delta)
    }

    /***************************************************************************************************
     * Translation and rotation functions to take an entity and create output for use by the camera
     ***************************************************************************************************/

    public Array2DRowRealMatrix translate(IDimensionalEntity dimensionalEntity) {
        return mTranslationMatrix.multiply(dimensionalEntity.getR4Matrix());
    }

    public Array2DRowRealMatrix performXRotation(Array2DRowRealMatrix matrix) {
        return mXRotationMatrix.multiply(matrix);
    }

    public Array2DRowRealMatrix performYRotation(Array2DRowRealMatrix matrix) {
        return mYRotationMatrix.multiply(matrix);
    }

    public Array2DRowRealMatrix performZRotation(Array2DRowRealMatrix matrix) {
        return mZRotationMatrix.multiply(matrix);
    }

}
