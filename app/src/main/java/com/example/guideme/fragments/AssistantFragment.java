package com.example.guideme.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.guideme.R;
import com.example.guideme.services.SpeechRecognitionService;

public class AssistantFragment extends Fragment {
    private SpeechRecognitionService speechService;
    private TextView assistantText;
    private Button speakButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_assistant, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assistantText = view.findViewById(R.id.assistantText);
        speakButton = view.findViewById(R.id.speakButton);

        speechService = new SpeechRecognitionService(requireContext(), text -> {
            assistantText.setText(text);
            // Process the speech input here
        });

        speakButton.setOnClickListener(v -> {
            if (speechService.isListening()) {
                speechService.stopListening();
                speakButton.setText("Speak");
            } else {
                speechService.startListening();
                speakButton.setText("Listening...");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        speechService.stopListening();
    }
}