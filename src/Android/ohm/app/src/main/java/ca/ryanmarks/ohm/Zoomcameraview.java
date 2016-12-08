package ca.ryanmarks.ohm;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

import org.opencv.android.JavaCameraView;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Zoomcameraview extends JavaCameraView {

    private Camera.AutoFocusMoveCallback callback = null;

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

        List<String> focusModes = params.getSupportedFocusModes();
        Log.d("ohm", "focusModes=" + focusModes);

        List<String> scenes = params.getSupportedSceneModes();
        Log.d("ohm", "sceneModes=" + scenes);

        //params.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        params.setRecordingHint(true);
        Log.d("ohm", "Focus Area" + params.getMaxNumFocusAreas());

        //float[] output = new float[3];
        //params.getFocusDistances(output);
        //Log.d("ohm", "Focus Distances" + Arrays.toString(output));
        //params.setFocusAreas(null);
        params.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        params.setFocusAreas(Collections.singletonList(new Camera.Area(new Rect(-25,-10,25,10),100)));
        params.setFlashMode(Parameters.FLASH_MODE_TORCH);

        mCamera.setParameters(params);


        if (callback != null){
            mCamera.setAutoFocusMoveCallback(callback);
        }
        return ret;
    }

    public void setCallback(Camera.AutoFocusMoveCallback callback) {
        this.callback = callback;
    }

}