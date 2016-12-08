package ca.ryanmarks.ohm;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

import org.opencv.android.JavaCameraView;


public class Zoomcameraview extends JavaCameraView {
    public Zoomcameraview(Context context, int cameraId) {
        super(context, cameraId);
    }

    public Zoomcameraview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    protected boolean initializeCamera(int width, int height)
    {

        boolean ret = super.initializeCamera(width, height);


        Camera.Parameters params = mCamera.getParameters();

        //if(params.isZoomSupported())
        //    params.setZoom(params.getMaxZoom());

        params.setFlashMode(Parameters.FLASH_MODE_TORCH);
        mCamera.setParameters(params);

        return ret;
    }

}