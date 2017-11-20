package caseydlvr.griffintime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private GriffinTime mCurrentTime;
    private GriffinTimes mGriffinTimes;

    @BindView(R.id.timeText) TextView mTimeText;
    @BindView(R.id.nextText) TextView mNextText;
    @BindView(R.id.nextButton) Button mNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mGriffinTimes = new GriffinTimes();
        mCurrentTime = mGriffinTimes.getCurrent();
        updateViews();
    }

    private void updateViews() {
        mTimeText.setText(mCurrentTime.getTime());
        mNextText.setText(mCurrentTime.getNextCriteria());
    }

    @OnClick (R.id.nextButton)
    public void nextTime() {
        mCurrentTime = mGriffinTimes.getNext();
        updateViews();
    }

}
