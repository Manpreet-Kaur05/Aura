package com.mentalhealth.auraapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.Image;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.mentalhealth.auraapp.R;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultimodalCheckInActivity extends AppCompatActivity {

    private static final String TAG = "MultimodalCheckIn";
    private static final int PERMISSION_REQUEST_CODE = 100;

    private PreviewView cameraPreview;
    private TextView statusText;
    private TextView analysisResultText;
    private Button startButton;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private MediaRecorder mediaRecorder;
    private String audioFilePath;
    private boolean isRecording = false;

    private FaceDetector faceDetector;
    private ProcessCameraProvider cameraProvider;
    private ExecutorService cameraExecutor;

    private int smileDetections = 0;
    private int totalFaceDetections = 0;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multimodal_checkin);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        cameraPreview = findViewById(R.id.cameraPreview);
        statusText = findViewById(R.id.statusText);
        analysisResultText = findViewById(R.id.analysisResultText);
        startButton = findViewById(R.id.startButton);
        progressBar = findViewById(R.id.progressBar);

        cameraExecutor = Executors.newSingleThreadExecutor();

        FaceDetectorOptions options = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .build();
        faceDetector = FaceDetection.getClient(options);

        startButton.setOnClickListener(v -> {
            if (checkPermissions()) {
                startMultimodalCheckIn();
            } else {
                requestPermissions();
            }
        });
    }

    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startMultimodalCheckIn();
            } else {
                Toast.makeText(this, "Permissions required", Toast.LENGTH_LONG).show();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void startMultimodalCheckIn() {
        startButton.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        statusText.setText("Starting...");

        smileDetections = 0;
        totalFaceDetections = 0;
        startTime = System.currentTimeMillis();

        startCamera();
        startAudioRecording();
        showCheckInQuestions();
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                bindCameraPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Error starting camera", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindCameraPreview(ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(cameraPreview.getSurfaceProvider());

        CameraSelector cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        imageAnalysis.setAnalyzer(cameraExecutor, new FaceAnalyzer());

        try {
            cameraProvider.unbindAll();
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
        } catch (Exception e) {
            Log.e(TAG, "Camera binding failed", e);
        }
    }

    private void startAudioRecording() {
        try {
            File audioDir = new File(getExternalFilesDir(null), "audio");
            if (!audioDir.exists() && !audioDir.mkdirs()) {
                Log.w(TAG, "Failed to create directory");
            }

            audioFilePath = audioDir.getAbsolutePath() + "/checkin_" +
                    System.currentTimeMillis() + ".3gp";

            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(audioFilePath);
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;

            Log.d(TAG, "Recording started");
        } catch (IOException e) {
            Log.e(TAG, "Audio recording failed", e);
        }
    }

    private void stopAudioRecording() {
        if (isRecording && mediaRecorder != null) {
            try {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
                isRecording = false;
                Log.d(TAG, "Recording stopped");
            } catch (Exception e) {
                Log.e(TAG, "Error stopping recording", e);
            }
        }
    }

    private class FaceAnalyzer implements ImageAnalysis.Analyzer {
        @androidx.camera.core.ExperimentalGetImage
        @Override
        public void analyze(@NonNull ImageProxy imageProxy) {
            Image mediaImage = imageProxy.getImage();

            if (mediaImage != null) {
                InputImage image = InputImage.fromMediaImage(
                        mediaImage, imageProxy.getImageInfo().getRotationDegrees());

                faceDetector.process(image)
                        .addOnSuccessListener(faces -> processFaces(faces))
                        .addOnFailureListener(e -> Log.e(TAG, "Detection failed", e))
                        .addOnCompleteListener(task -> imageProxy.close());
            } else {
                imageProxy.close();
            }
        }
    }

    private void processFaces(List<Face> faces) {
        if (!faces.isEmpty()) {
            totalFaceDetections++;
            for (Face face : faces) {
                Float smilingProb = face.getSmilingProbability();
                if (smilingProb != null && smilingProb > 0.5f) {
                    smileDetections++;
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void showCheckInQuestions() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> statusText.setText("How are you feeling?"), 2000);
        handler.postDelayed(() -> statusText.setText("What's on your mind?"), 12000);
        handler.postDelayed(() -> statusText.setText("Energy level?"), 22000);
        handler.postDelayed(this::endCheckIn, 32000);
    }

    private void endCheckIn() {
        stopAudioRecording();
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
        }
        calculateResults();
    }

    @SuppressLint("SetTextI18n")
    private void calculateResults() {
        progressBar.setVisibility(View.GONE);
        statusText.setText("Complete!");

        float smilePercent = totalFaceDetections > 0 ?
                (smileDetections * 100f / totalFaceDetections) : 0;

        String analysis = "Results:\n\nSmile: " + String.format("%.0f%%", smilePercent) +
                "\n\n" + (smilePercent > 50 ? "Positive affect!" : "Keep going!");

        analysisResultText.setText(analysis);
        analysisResultText.setVisibility(View.VISIBLE);

        saveData(smilePercent);

        new AlertDialog.Builder(this)
                .setTitle("Complete")
                .setMessage("Check-in saved!")
                .setPositiveButton("OK", (d, w) -> finish())
                .show();
    }

    private void saveData(float smilePercent) {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", mAuth.getCurrentUser().getUid());
        data.put("timestamp", System.currentTimeMillis());
        data.put("smilePercent", smilePercent);

        db.collection("multimodal_checkins").add(data)
                .addOnSuccessListener(doc -> Log.d(TAG, "Saved"))
                .addOnFailureListener(e -> Log.e(TAG, "Save failed", e));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
        if (isRecording) {
            stopAudioRecording();
        }
    }
}
