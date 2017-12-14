package caseydlvr.griffintime;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private boolean mBound = false;
    private GriffinTimeService mGriffinTimeService;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            mBound = true;
            Log.d(TAG,"onServiceConnected");
            GriffinTimeService.LocalBinder localBinder = (GriffinTimeService.LocalBinder) binder;
            mGriffinTimeService = localBinder.getService();
            updateViews(mGriffinTimeService.getCurrentTime());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    @BindView(R.id.timeText) TextView mTimeText;
    @BindView(R.id.nextText) TextView mNextText;
    @BindView(R.id.nextButton) Button mNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // force portrait orientation for non-tablets
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
        Intent intent = new Intent(this, GriffinTimeService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
        if (mBound) updateViews(mGriffinTimeService.getCurrentTime());
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop()");
        super.onStop();
        if (mBound) {
            unbindService(mServiceConnection);
            mBound = false;
        }
    }

    private void updateViews(GriffinTime currentTime) {
        mTimeText.setText(currentTime.getTime());
        mNextText.setText(currentTime.getNextCriteria());
    }

    @OnClick (R.id.nextButton)
    public void nextButtonClick() {
        mGriffinTimeService.nextTime();
        updateViews(mGriffinTimeService.getCurrentTime());
    }
}
