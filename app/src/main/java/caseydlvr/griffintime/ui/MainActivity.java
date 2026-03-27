package caseydlvr.griffintime.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import caseydlvr.griffintime.actions.ActionHandler;
import caseydlvr.griffintime.GriffinTimeApp;
import caseydlvr.griffintime.data.Repository;
import caseydlvr.griffintime.databinding.ActivityMainBinding;
import caseydlvr.griffintime.model.GriffinTime;
import caseydlvr.griffintime.R;

public class MainActivity extends AppCompatActivity {

    Repository mRepository;
    ActivityMainBinding mBinding;

    private final ActivityResultLauncher<String> mRequestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    ActionHandler actionHandler = ((GriffinTimeApp) getApplication()).getActionHandler();
                    actionHandler.handleAction(ActionHandler.ACTION_NOTIFY);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        // force portrait orientation for non-tablets
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        mRepository = ((GriffinTimeApp) getApplication()).getRepository();

        mBinding.nextButton.setOnClickListener(v -> nextButtonClick());

        requestNotificationPermission();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
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

        mBinding.timeText.setText(currentTime.getTime());
        mBinding.nextText.setText(currentTime.getNextCriteria());
    }

    private void nextButtonClick() {
        ActionHandler actionHandler = ((GriffinTimeApp) getApplication()).getActionHandler();
        actionHandler.handleAction(ActionHandler.ACTION_NEXT);
        updateViews();
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                mRequestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }
}
