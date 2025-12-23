package com.example.frontend;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.adapter.DocumentAdapter;
import com.example.frontend.api.AuthApi;
import com.example.frontend.model.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DocumentsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button btnAdd;
    ProgressBar progressBar;
    List<Document> list = new ArrayList<>();
    Long etudiantId;

    private static final int PICK_FILE_REQUEST = 1001;
    private AuthApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);

        recyclerView = findViewById(R.id.recyclerDocuments);
        btnAdd = findViewById(R.id.btnAdd);
        progressBar = findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // récupérer l'ID de l'étudiant depuis l'Intent
        long etudiantId = getIntent().getLongExtra("STUDENT_ID", -1);
        if (etudiantId == -1) {
            Toast.makeText(this, "ID étudiant introuvable", Toast.LENGTH_LONG).show();
            finish();
            return;
        }


        // Initialiser Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.16:9090/") // ton backend
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(AuthApi.class);

        loadDocuments();

        btnAdd.setOnClickListener(v -> openFilePicker());
    }

    // ------------------- Ouvrir le picker de fichiers -------------------
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Sélectionner un document"), PICK_FILE_REQUEST);
    }

    // ------------------- Récupérer le fichier sélectionné -------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri fileUri = data.getData();
            String fileName = getFileName(fileUri);

            try {
                File file = createTempFileFromUri(fileUri, fileName);
                uploadDocument(file, fileName);
            } catch (Exception e) {
                Toast.makeText(this, "Erreur fichier: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // ------------------- Obtenir le nom du fichier -------------------
    private String getFileName(Uri uri) {
        String result = null;
        if ("content".equals(uri.getScheme())) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index >= 0) {
                        result = cursor.getString(index);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    // ------------------- Créer un fichier temporaire à partir du Uri -------------------
    private File createTempFileFromUri(Uri uri, String fileName) throws Exception {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        File tempFile = new File(getCacheDir(), fileName);
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            out.flush();
        }
        return tempFile;
    }

    // ------------------- Upload du document via Retrofit -------------------
    private void uploadDocument(File file, String fileName) {
        progressBar.setVisibility(View.VISIBLE);

        RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", fileName, requestFile);
        RequestBody etudiantIdPart = RequestBody.create(MultipartBody.FORM, String.valueOf(etudiantId));

        Call<ResponseBody> call = api.uploadDocument(body, etudiantIdPart);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Toast.makeText(DocumentsActivity.this, "Document uploadé avec succès", Toast.LENGTH_SHORT).show();
                    loadDocuments(); // rafraîchir la liste
                } else {
                    Toast.makeText(DocumentsActivity.this, "Erreur lors de l'upload", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(DocumentsActivity.this, "Erreur: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ------------------- Charger les documents existants -------------------
    private void loadDocuments() {
        progressBar.setVisibility(View.VISIBLE);
        // URL backend pour récupérer les documents de l'étudiant
        String url = "http://192.168.43.16:9090/api/documents/etudiant/" + etudiantId;

        // On peut continuer à utiliser Volley pour le GET
        com.android.volley.RequestQueue queue = com.android.volley.toolbox.Volley.newRequestQueue(this);
        com.android.volley.toolbox.JsonArrayRequest request = new com.android.volley.toolbox.JsonArrayRequest(com.android.volley.Request.Method.GET, url, null,
                response -> {
                    com.google.gson.Gson gson = new com.google.gson.Gson();
                    list = new ArrayList<>();
                    list = Arrays.asList(gson.fromJson(response.toString(), Document[].class));
                    recyclerView.setAdapter(new DocumentAdapter(this, list));
                    progressBar.setVisibility(View.GONE);
                },
                error -> {
                    Toast.makeText(this, "Erreur chargement", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                });
        queue.add(request);
    }
}
