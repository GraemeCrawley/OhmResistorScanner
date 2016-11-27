package ca.ryanmarks.ohm;

import android.annotation.SuppressLint;
import android.hardware.Camera;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.InstallCallbackInterface;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ca.ryanmarks.ohm.ImageProcessing.BandReader;
import ca.ryanmarks.ohm.ValueIdentification.ResistorColour;
import ca.ryanmarks.ohm.ValueIdentification.ValueCalculator;


public class CameraActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String TAG = "OCVSample::Activity";

    private Zoomcameraview zcv;
    private boolean mIsJavaCamera = true;
    private MenuItem mItemSwitchCamera = null;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    ResistorColour.train(new InputStreamReader(getApplicationContext().getResources().openRawResource(R.raw.train)));
                    zcv.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_camera);

        zcv = (Zoomcameraview) findViewById(R.id.camera_view);
        zcv.setMaxFrameSize(640/2,310/2);
        zcv.setVisibility(SurfaceView.VISIBLE);
        zcv.setCvCameraViewListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        disableCamera();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        disableCamera();
    }

    public void disableCamera() {
        if (zcv != null)
            zcv.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
    }

    public void onCameraViewStopped() {
    }

    Mat mRgba;
    Mat mGray;


    String resistance = "None seen";

    private Scalar getColorFromCode(int id){
        switch (id){
            case 0:
                return new Scalar(10,10,10);
            case 1:
                return new Scalar(165,42,42);
            case 2:
                return new Scalar(255,0,0);
            case 3:
                return new Scalar(255,69,0);
            case 4:
                return new Scalar(255,255,0);
            case 5:
                return new Scalar(0,255,0);
            case 11:
                return new Scalar(255,255,102);
            default:
                return new Scalar(120,120,120);
        }
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat processed = inputFrame.rgba();


        Point left = new Point(processed.width() / 3, processed.height() / 2);
        Point right = new Point(processed.width() * 2 / 3, processed.height() / 2);

        for (int i = 0; i < processed.height(); i++){
            for (int j = 0; j < processed.width(); j++){
                double[] point = processed.get(i,j);
                int color = ResistorColour.fit((float) point[0],(float)point[1],(float)point[2]);
                if (color != 0)
                    processed.put(i,j,getColorFromCode(color).val);

            }
        }


        ArrayList<ResistorColour> values = new ArrayList<ResistorColour>();

        Imgproc.line(processed, left, right, new Scalar(255, 0, 0), 1);

        if (values.size() == 4) {
            ValueCalculator vc = new ValueCalculator(values.get(0), values.get(1), values.get(2), values.get(3));
            resistance = vc.getValue();
        }
        Imgproc.putText(processed, resistance, new Point(100, 200), 1, 6, new Scalar(0, 0, 0), 5);


        return processed;
    }

}