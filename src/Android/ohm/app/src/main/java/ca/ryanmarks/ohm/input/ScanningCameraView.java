package ca.ryanmarks.ohm.input;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.AttributeSet;

import org.opencv.android.JavaCameraView;

import java.util.Collections;


public class ScanningCameraView extends JavaCameraView {

    public ScanningCameraView(Context context, int cameraId) {
        super(context, cameraId);
    }

    public ScanningCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Initialize a camera with the preferred parameters for resistor scanning
     * @param width  the width of the pictures, in pixels
     * @param height the height of the pictures, in pixels
     * @return The success of the initialization
     */
    protected boolean initializeCamera(int width, int height)
    {
        boolean ret = super.initializeCamera(width, height);

        Camera.Parameters params = mCamera.getParameters();

        params.setRecordingHint(true);
        params.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        params.setFocusAreas(Collections.singletonList(new Camera.Area(new Rect(-25,-10,25,10),100)));
        params.setFlashMode(Parameters.FLASH_MODE_TORCH);

        mCamera.setParameters(params);

        return ret;
    }
}