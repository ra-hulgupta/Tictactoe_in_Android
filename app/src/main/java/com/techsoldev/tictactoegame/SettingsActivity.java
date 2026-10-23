package com.techsoldev.tictactoegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;

public class SettingsActivity extends AppCompatActivity {

    private Switch vibrationSwitch;
    private Switch soundSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_settings);

        vibrationSwitch = findViewById(R.id.vibration_switch);
        soundSwitch = findViewById(R.id.sound_switch);

        ImageView backBtn = findViewById(R.id.settings_back_btn);
        LinearLayout rateUs = findViewById(R.id.rate_us_layout);
        LinearLayout feedback = findViewById(R.id.feedback_layout);

        vibrationSwitch.setChecked(MyServices.VIBRATION_CHECK);
        vibrationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> MyServices.VIBRATION_CHECK = isChecked);

        soundSwitch.setChecked(MyServices.SOUND_CHECK);
        soundSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> MyServices.SOUND_CHECK = isChecked);

        backBtn.setOnClickListener(v -> onBackPressed());

        rateUs.setOnClickListener(v -> askRatings());

        feedback.setOnClickListener(v -> composeEmail("Tic Tac Toe Feedback"));
    }

    void askRatings() {
        ReviewManager manager = ReviewManagerFactory.create(this);
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ReviewInfo reviewInfo = task.getResult();
                manager.launchReviewFlow(this, reviewInfo);
            }
        });
    }

    public void composeEmail(String subject) {
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:abdullahsh123@hotmail.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            startActivity(Intent.createChooser(intent, "Send feedback"));
        } catch (ActivityNotFoundException e) {
            // Handle activity not found
        }
    }
}
