package com.example.hestia_app.utils;

import android.net.Uri;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FirebaseGaleriaUtils {
    private FirebaseStorage storage;


    public FirebaseGaleriaUtils() {
        storage = FirebaseStorage.getInstance();
    }

    public void uploadImage(String imagePath, final FirebaseStorageCallback callback) {
        Uri fileUri = Uri.parse(imagePath); // Converte o caminho da imagem em Uri
        StorageReference storageRef = storage.getReference("imagensGaleria").child("imagensGaleria/" + System.currentTimeMillis() + fileUri.getLastPathSegment() + ".jpg");

        // Faz o upload do arquivo
        storageRef.putFile(fileUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    // Obtém o link de download
                    storageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                callback.onSuccess(downloadUri.toString()); // Retorna o link de download
                            } else {
                                callback.onFailure(task.getException());
                            }
                        }
                    });
                } else {
                    callback.onFailure(task.getException());
                }
            }
        });
    }

    // Interface de callback para obter o resultado do link de download
    public interface FirebaseStorageCallback {
        void onSuccess(String downloadUrl);
        void onFailure(Exception e);
    }
}
