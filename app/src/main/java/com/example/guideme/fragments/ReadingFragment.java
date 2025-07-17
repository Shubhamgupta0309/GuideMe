package com.example.guideme.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.guideme.R;
import com.example.guideme.services.CameraService;
import com.example.guideme.services.TextToSpeechService;

public class ReadingFragment extends Fragment {
    private CameraService cameraService;
    private TextToSpeechService ttsService;
    private TextView readingText;
    private ImageView readingImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reading, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        readingText = view.findViewById(R.id.readingText);
        readingImageView = view.findViewById(R.id.readingImageView);

        ttsService = new TextToSpeechService(requireContext());

        cameraService = new CameraService(requireContext(), new CameraService.CameraCallback() {
            @Override
            public void onImageCaptured(Bitmap bitmap) {
                readingImageView.setImageBitmap(bitmap);
                // Process the image for text recognition here
                String recognizedText = "Sample recognized text"; // Replace with OCR
                readingText.setText(recognizedText);
                ttsService.speak(recognizedText);
            }

            @Override
            public void onError(String error) {
                readingText.setText("Error: " + error);
            }
        });
        cameraService.startCamera();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cameraService.stopCamera();
        ttsService.shutdown();
    }
}