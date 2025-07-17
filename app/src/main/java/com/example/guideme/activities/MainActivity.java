package com.example.guideme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.guideme.R;
import com.example.guideme.utils.AccessibilityUtils;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextToSpeech tts;
    private boolean isSettingsVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize TTS
        tts = new TextToSpeech(this, status -> {
            if (status != TextToSpeech.ERROR) {
                tts.setLanguage(Locale.US);
                tts.setSpeechRate(1.2f);
            }
        });

        Button blindModeBtn = findViewById(R.id.blindModeBtn);
        ImageButton settingsBtn = findViewById(R.id.settingsBtn);
        LinearLayout instructionsLayout = findViewById(R.id.instructionsLayout);
        TextView instructionsText = findViewById(R.id.instructionsText);

        blindModeBtn.setOnClickListener(v -> {
            AccessibilityUtils.vibrate(this, 100);
            startActivity(new Intent(this, BlindModeActivity.class));
        });

        settingsBtn.setOnClickListener(v -> {
            AccessibilityUtils.vibrate(this, 50);
            isSettingsVisible = !isSettingsVisible;
            instructionsLayout.setVisibility(isSettingsVisible ? View.VISIBLE : View.GONE);

            if (isSettingsVisible) {
                tts.speak(getString(R.string.instructions_tts), TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });
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