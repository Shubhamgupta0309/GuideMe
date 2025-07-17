package com.example.guideme.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Size;
import android.view.TextureView;

import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CameraService {
    public interface CameraCallback {
        void onImageCaptured(Bitmap bitmap);
        void onError(String error);
    }

    private ExecutorService cameraExecutor;
    private ProcessCameraProvider cameraProvider;
    private Context context;
    private CameraCallback callback;

    public CameraService(Context context, CameraCallback callback) {
        this.context = context;
        this.callback = callback;
        cameraExecutor = Executors.newSingleThreadExecutor();
    }

    public void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(context);

        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                bindCameraUseCases();
            } catch (ExecutionException | InterruptedException e) {
                callback.onError("Camera initialization failed: " + e.getMessage());
            }
        }, ContextCompat.getMainExecutor(context));
    }

    private void bindCameraUseCases() {
        try {
            // Camera preview
            Preview preview = new Preview.Builder().build();

            // Image analysis
            ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                    .setTargetResolution(new Size(1280, 720))
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build();

            imageAnalysis.setAnalyzer(cameraExecutor, image -> {
                try {
                    Bitmap bitmap = convertImageProxyToBitmap(image);
                    if (bitmap != null) {
                        callback.onImageCaptured(bitmap);
                    }
                } catch (Exception e) {
                    callback.onError("Image processing error: " + e.getMessage());
                } finally {
                    image.close();
                }
            });

            // Select back camera
            CameraSelector cameraSelector = new CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build();

            // Unbind use cases before rebinding
            cameraProvider.unbindAll();

            // Bind use cases to camera
            cameraProvider.bindToLifecycle(
                    (androidx.lifecycle.LifecycleOwner) context,
                    cameraSelector,
                    preview,
                    imageAnalysis);

        } catch (Exception e) {
            callback.onError("Camera setup failed: " + e.getMessage());
        }
    }

    private Bitmap convertImageProxyToBitmap(ImageProxy image) {
        // Implement image conversion from ImageProxy to Bitmap
        return null; // Placeholder
    }

    public void stopCamera() {
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
        }
        if (cameraExecutor != null) {
            cameraExecutor.shutdown();
        }
    }
}