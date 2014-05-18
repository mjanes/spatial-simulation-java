package display;

import camera.ThreeDimensionalViewCamera;

/**
 * Created by mjanes on 5/17/2014.
 */
public class CameraRunnable extends SimulationRunnable {

    private ThreeDimensionalEntityCanvas mCanvas;
    private ThreeDimensionalViewCamera mCamera;

    public CameraRunnable(ISimulationContainer container, ThreeDimensionalEntityCanvas canvas, ThreeDimensionalViewCamera camera) {
        super(container);
        mCanvas = canvas;
        mCamera = camera;
    }

    @Override
    protected void increment() {
        // tell graphics to repaint
        mCanvas.updateGraphics();

        // TODO: Perhaps create a separate pause camera button?
        mCamera.move();
    }
}
