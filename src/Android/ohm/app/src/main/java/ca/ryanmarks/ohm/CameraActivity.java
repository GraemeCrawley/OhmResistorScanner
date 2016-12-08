package ca.ryanmarks.ohm;

import android.annotation.SuppressLint;
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

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ca.ryanmarks.ohm.ImageProcessing.BandReader;
import ca.ryanmarks.ohm.ValueIdentification.ResistorColour;
import ca.ryanmarks.ohm.ValueIdentification.ValueCalculator;


public class CameraActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String TAG = "OCVSample::Activity";

    private CameraBridgeViewBase mOpenCvCameraView;
    private boolean mIsJavaCamera = true;
    private MenuItem mItemSwitchCamera = null;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                    ResistorColour.trainNN(new InputStreamReader(getApplicationContext().getResources().openRawResource(R.raw.train)));
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

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.camera_view);
        mOpenCvCameraView.setMaxFrameSize(590,360);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
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
            ResistorColour.trainNN(new InputStreamReader(getApplicationContext().getResources().openRawResource(R.raw.train)));
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
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
    }

    public void onCameraViewStopped() {
    }

    Mat mRgba;
    Mat mGray;



    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat processed =  inputFrame.rgba();

        Point scanLineLeft = new Point(processed.width() * 1 / 5, processed.height() / 2);
        Point scanLineRight = new Point(processed.width() * 4 / 5 , processed.height() / 2);

        List<Point> points = BandReader.read(processed,scanLineLeft, scanLineRight);

        ArrayList<Integer> values = new ArrayList<Integer>();
        for (int i = 0; i < Math.min(points.size()-1, 7); i = i + 2){
            Point p1 = points.get(i);
            Point p2 = points.get(i+1);
            Point midpoint = new Point((p1.x + p2.x)/2, (p1.y + p2.y)/2);
            Scalar colourAtMidpoint = new Scalar(processed.get((int) midpoint.y, (int) midpoint.x));
            int resistorColourAtMidpoint =
                    ResistorColour.fit((float)colourAtMidpoint.val[0],
                                        (float) colourAtMidpoint.val[1],
                                        (float) colourAtMidpoint.val[2]);
            Scalar rgbAtMidpoint = new Scalar(processed.get((int) midpoint.y, (int) midpoint.x));
            Imgproc.circle(processed, p1, 1, new Scalar(0, 0, 255), 2);
            Imgproc.circle(processed, p2, 1, new Scalar(0, 0, 255), 2);
            Imgproc.circle(processed, midpoint, 10, rgbAtMidpoint, 2);
            Imgproc.putText(processed,
                    Integer.toString(resistorColourAtMidpoint),p1,1,1,new Scalar(255,255,255));
            values.add(resistorColourAtMidpoint);
        }

        Imgproc.line(processed, scanLineLeft, scanLineRight, new Scalar(255, 0, 0));

        if (values.size() == 4){
            ValueCalculator vc = new ValueCalculator(values.get(0),values.get(1), values.get(2),values.get(3));
            Imgproc.putText(processed,vc.getValue(), new Point(0,340),2,1.5,new Scalar(0,0,0),2
            );
        }


        return processed;
    }

}