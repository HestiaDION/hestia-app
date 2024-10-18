package com.example.hestia_app.presentation.view.camera;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;

import com.example.hestia_app.R;
import com.google.common.util.concurrent.ListenableFuture;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FotoActivity extends AppCompatActivity {

    private static final String FILENAME_FORMAT = "dd-MM-yyyy-HH-mm-ss-SSS";

    // objetos
    private ExecutorService cameraExecutor;
    private androidx.camera.view.PreviewView viewFinder;
    private ImageView foto;
    private Button btPhoto, btSalvar;
    private ImageButton lente;
    private Map<String, String> docData = new HashMap<>();
    private DatabaseFoto database = new DatabaseFoto();
    private ProgressBar progressBar;

    // config camera
    private ImageCapture imageCapture;
    private CameraSelector cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;

    // log
    private static final String TAG = "CameraXBasic";

    // lista de permissões
    private static final String[] REQUIRED_PERMISSIONS;

    static {
        List<String> requiredPermissions = new ArrayList<>();
        requiredPermissions.add("android.permission.CAMERA");
        REQUIRED_PERMISSIONS = requiredPermissions.toArray(new String[0]);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);

        // obter objetos
        cameraExecutor = Executors.newSingleThreadExecutor();
        viewFinder = findViewById(R.id.viewFinder);
        foto = findViewById(R.id.foto);
        btPhoto = findViewById(R.id.bt_photo);
        lente = findViewById(R.id.lente);
        btSalvar = findViewById(R.id.bt_salvar_foto);
        progressBar = findViewById(R.id.progressBar);

        lente.setOnClickListener(v -> {
            if (cameraSelector.equals(CameraSelector.DEFAULT_BACK_CAMERA)) {
                cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;
            } else {
                cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
            }
            startCamera();
        });

        foto.setOnClickListener(v -> {
            foto.setVisibility(View.INVISIBLE);
        });

        btSalvar.setOnClickListener(v -> {
            if (docData.get("url") == null) {
                return;
            }

            Intent resultIntent = new Intent();
            resultIntent.setData(Uri.parse(docData.get("url")));
            setResult(RESULT_OK, resultIntent);

            // Finaliza a Activity após capturar a foto
            finish();
        });

        // Request de permissão
        if (allPermissionsGranted()) {
            // ativar câmera
            startCamera();
        } else {
            // pedir permissão
            // requestPermissions(REQUIRED_PERMISSIONS, 0);
            requestPermissions();
        }

        btPhoto.setOnClickListener(v -> {
            // tirar imagem
            takePhoto();
        });
    }

    private void takePhoto() {

        if (imageCapture == null) {
            return;
        }

        // definir nome e caminho da imagem
        String name = new SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis());
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MyCameraApp");

        // carregando imagem com as configurações
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(
                getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues).build();

        // salvar as fotos de acordo com a orientação
        OrientationEventListener orientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                int rotation;
                if (orientation >= 45 && orientation < 135) {
                    rotation = Surface.ROTATION_270;
                } else if (orientation >= 135 && orientation <= 224) {
                    rotation = Surface.ROTATION_180;
                } else if (orientation >= 225 && orientation <= 314) {
                    rotation = Surface.ROTATION_90;
                } else {
                    rotation = Surface.ROTATION_0;
                }

                imageCapture.setTargetRotation(rotation);
            }
        };

        orientationEventListener.enable();

        // listener para gerar imagem
        imageCapture.takePicture(
                outputFileOptions,
                ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        // imagem gravada
                        foto.setImageURI(outputFileResults.getSavedUri());
                        foto.setVisibility(View.VISIBLE);
                        database.uploadFoto(getBaseContext(), foto, docData, progressBar);
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException error) {
                        // erro ao gravar imagem
                        Log.e(TAG, "Error ao gravar imagem" + error.getMessage(), error);
                    }
                }
        );
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                // Used to bind the lifecycle of cameras to the lifecycle owner
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                // Preview
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(viewFinder.getSurfaceProvider());

                // ImageCapture
                imageCapture = new ImageCapture.Builder().build();

                try {
                    // Unbind use cases before rebinding
                    cameraProvider.unbindAll();

                    // Bind use cases to camera
                    cameraProvider.bindToLifecycle(
                            this,
                            cameraSelector,
                            preview,
                            imageCapture
                    );
                } catch (Exception exc) {
                    Log.e(TAG, "Camera binding failed", exc);
                }

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    public boolean allPermissionsGranted() {
        // verificar permissões
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermissions() {
        activityResultLauncher.launch(REQUIRED_PERMISSIONS);
    }
    private ActivityResultLauncher<String[]> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            permissions -> {
                // Handle Permission granted/rejected
                boolean permissionGranted = true;
                for (Map.Entry<String, Boolean> entry : permissions.entrySet()) {
                    if (Arrays.asList(REQUIRED_PERMISSIONS).contains(entry.getKey()) && !entry.getValue()) {
                        permissionGranted = false;
                        break;
                    }
                }
                if (!permissionGranted) {
                    Toast.makeText(getApplicationContext(),"Permissão NEGADA. Tente novamente.",Toast.LENGTH_SHORT).show();
                } else {
                    startCamera();
                }
            });
}