package com.example.guideme.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.guideme.R;
import com.example.guideme.adapters.ModePagerAdapter;
import com.example.guideme.fragments.AssistantFragment;
import com.example.guideme.fragments.NavigationFragment;
import com.example.guideme.fragments.ReadingFragment;
import com.example.guideme.utils.AccessibilityUtils;

import java.util.Locale;

public class BlindModeActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST = 100;
    private static final int AUDIO_PERMISSION_REQUEST = 101;

    private TextToSpeech tts;
    private ViewPager viewPager;
    private ModePagerAdapter pagerAdapter;
    private ImageView micIcon, bookIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blind_mode);

        // Initialize TTS
        tts = new TextToSpeech(this, status -> {
            if (status != TextToSpeech.ERROR) {
                tts.setLanguage(Locale.US);
                tts.setSpeechRate(1.5f);
            }
        });

        // Check permissions
        if (!checkPermissions()) {
            requestPermissions();
        }

        // Setup ViewPager
        viewPager = findViewById(R.id.viewPager);
        pagerAdapter = new ModePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        micIcon = findViewById(R.id.micIcon);
        bookIcon = findViewById(R.id.bookIcon);

        // Setup gesture detector
        GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                toggleAssistantMode();
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                toggleReadingMode();
            }
        });

        viewPager.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return false;
        });
    }

    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO},
                CAMERA_PERMISSION_REQUEST);
    }

    private void toggleAssistantMode() {
        int currentItem = viewPager.getCurrentItem();
        if (currentItem == 0) { // Navigation mode
            viewPager.setCurrentItem(1); // Switch to Assistant
            tts.speak("Assistant mode activated", TextToSpeech.QUEUE_FLUSH, null, null);
            micIcon.setColorFilter(ContextCompat.getColor(this, R.color.green));
        } else {
            viewPager.setCurrentItem(0); // Switch back to Navigation
            tts.speak("Navigation mode activated", TextToSpeech.QUEUE_FLUSH, null, null);
            micIcon.setColorFilter(ContextCompat.getColor(this, R.color.gray));
        }
    }

    private void toggleReadingMode() {
        int currentItem = viewPager.getCurrentItem();
        if (currentItem != 2) { // Not already in reading mode
            viewPager.setCurrentItem(2); // Switch to Reading
            tts.speak("Reading mode activated", TextToSpeech.QUEUE_FLUSH, null, null);
            bookIcon.setColorFilter(ContextCompat.getColor(this, R.color.green));
        } else {
            viewPager.setCurrentItem(0); // Switch back to Navigation
            tts.speak("Navigation mode activated", TextToSpeech.QUEUE_FLUSH, null, null);
            bookIcon.setColorFilter(ContextCompat.getColor(this, R.color.gray));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}