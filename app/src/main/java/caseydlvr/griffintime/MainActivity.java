package caseydlvr.griffintime;

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

public class MainActivity extends AppCompatActivity {

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
        GriffinTime currentTime =
                GriffinTimes.getGriffinTimeByInt(GriffinTimeStorage.getCurrentTime(this));

        mTimeText.setText(currentTime.getTime());
        mNextText.setText(currentTime.getNextCriteria());
    }

    @OnClick (R.id.nextButton)
    public void nextButtonClick() {
        ActionHandler.handleAction(this, ActionHandler.ACTION_NEXT);
        updateViews();
    }
}
