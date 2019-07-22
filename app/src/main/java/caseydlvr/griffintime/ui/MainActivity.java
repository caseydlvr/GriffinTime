package caseydlvr.griffintime.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import caseydlvr.griffintime.ActionHandler;
import caseydlvr.griffintime.GriffinTimeApp;
import caseydlvr.griffintime.data.GriffinTimeRepository;
import caseydlvr.griffintime.model.GriffinTime;
import caseydlvr.griffintime.R;

public class MainActivity extends AppCompatActivity {

    GriffinTimeRepository mRepository;

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

        mRepository = ((GriffinTimeApp) getApplication()).getRepository();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateViews();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // to handle user advancing the time from notification while Activity is in the foreground
        // because pulling down the notification bar doesn't trigger onPause
        updateViews();
    }

    private void updateViews() {
        GriffinTime currentTime = mRepository.getCurrentTime();

        mTimeText.setText(currentTime.getTime());
        mNextText.setText(currentTime.getNextCriteria());
    }

    @OnClick (R.id.nextButton)
    public void nextButtonClick() {
        ActionHandler actionHandler = ((GriffinTimeApp) getApplication()).getActionHandler();
        actionHandler.handleAction(ActionHandler.ACTION_NEXT);
        updateViews();
    }
}
