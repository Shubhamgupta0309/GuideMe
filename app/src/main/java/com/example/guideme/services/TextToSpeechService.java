package com.example.guideme.services;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import java.util.Locale;

public class TextToSpeechService {
    private static final String TAG = "TextToSpeechService";
    private TextToSpeech tts;
    private boolean isReady = false;

    public TextToSpeechService(Context context) {
        tts = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = tts.setLanguage(Locale.US);
                if (result == TextToSpeech.LANG_MISSING_DATA ||
                        result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e(TAG, "Language not supported");
                } else {
                    isReady = true;
                }
            } else {
                Log.e(TAG, "Initialization failed");
            }
        });

        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                Log.d(TAG, "TTS started");
            }

            @Override
            public void onDone(String utteranceId) {
                Log.d(TAG, "TTS completed");
            }

            @Override
            public void onError(String utteranceId) {
                Log.e(TAG, "TTS error");
            }
        });
    }

    public void speak(String text) {
        if (isReady) {
            tts.speak(text, TextToSpeech.QUEUE_ADD, null, "TTS_UTTERANCE");
        }
    }

    public void shutdown() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}