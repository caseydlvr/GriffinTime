package caseydlvr.griffintime;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private boolean mBound = false;
    private GriffinTimeService mGriffinTimeService;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            mBound = true;
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
        Intent intent = new Intent(this, GriffinTimeService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBound) updateViews(mGriffinTimeService.getCurrentTime());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mServiceConnection);
            mBound = false;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // to handle user advancing the time from notification while Activity is in the foreground
        // because pulling down the notification bar doesn't trigger onPause
        if (mBound && hasFocus) updateViews(mGriffinTimeService.getCurrentTime());
    }

    private void updateViews(GriffinTime currentTime) {
        mTimeText.setText(currentTime.getTime());
        mNextText.setText(currentTime.getNextCriteria());
    }

    @OnClick (R.id.nextButton)
    public void nextButtonClick() {
        if (mBound) {
            mGriffinTimeService.nextTime();
            updateViews(mGriffinTimeService.getCurrentTime());
        } else {
            Intent intent = new Intent(MainActivity.this, GriffinTimeService.class);
            bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
            Toast.makeText(this, "Something went wrong :O Try again", Toast.LENGTH_SHORT).show();
        }
    }
}
