package com.example.guideme.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.guideme.R;
import com.example.guideme.services.CameraService;

public class NavigationFragment extends Fragment {
    private CameraService cameraService;
    private TextView navigationText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        navigationText = view.findViewById(R.id.navigationText);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cameraService = new CameraService(requireContext(), navigationText);
        cameraService.startCamera();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (cameraService != null) {
            cameraService.stopCamera();
        }
    }
}