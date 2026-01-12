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
import com.example.frontend.api.RetrofitClient;
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

public class DocumentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button btnAdd;
    private ProgressBar progressBar;

    private List<Document> list = new ArrayList<>();

    // 🔴 IMPORTANT : champ de classe (PAS local)
    private Long etudiantId;

    private static final int PICK_FILE_REQUEST = 1001;
    private AuthApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);

        recyclerView = findViewById(R.id.recyclerDocuments);
        btnAdd = findViewById(R.id.btnAdd);
        progressBar = findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ✅ Récupération correcte de l’ID étudiant
        etudiantId = getIntent().getLongExtra("STUDENT_ID", -1);
        if (etudiantId == -1) {
            Toast.makeText(this, "ID étudiant introuvable", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // ✅ Retrofit AVEC token (Interceptor JWT)
        api = RetrofitClient
                .getRetrofitInstance(this)
                .create(AuthApi.class);

        loadDocuments();

        btnAdd.setOnClickListener(v -> openFilePicker());
    }

    /* ===============================
       Sélection du fichier
       =============================== */

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(
                Intent.createChooser(intent, "Sélectionner un document"),
                PICK_FILE_REQUEST
        );
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            @Nullable Intent data
    ) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            Uri fileUri = data.getData();
            String fileName = getFileName(fileUri);

            try {
                File file = createTempFileFromUri(fileUri, fileName);
                uploadDocument(file, fileName);
            } catch (Exception e) {
                Toast.makeText(
                        this,
                        "Erreur fichier : " + e.getMessage(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }

    /* ===============================
       Utils fichier
       =============================== */

    private String getFileName(Uri uri) {
        String result = null;

        if ("content".equals(uri.getScheme())) {
            try (Cursor cursor = getContentResolver()
                    .query(uri, null, null, null, null)) {

                if (cursor != null && cursor.moveToFirst()) {
                    int index =
                            cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
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

    private File createTempFileFromUri(Uri uri, String fileName)
            throws Exception {

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

    /* ===============================
       Upload document
       =============================== */

    private void uploadDocument(File file, String fileName) {

        progressBar.setVisibility(View.VISIBLE);

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("*/*"), file);

        MultipartBody.Part filePart =
                MultipartBody.Part.createFormData(
                        "file",
                        fileName,
                        requestFile
                );

        RequestBody etudiantIdPart =
                RequestBody.create(
                        MultipartBody.FORM,
                        String.valueOf(etudiantId)
                );

        api.uploadDocument(filePart, etudiantIdPart)
                .enqueue(new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(
                            Call<ResponseBody> call,
                            Response<ResponseBody> response
                    ) {
                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful()) {
                            Toast.makeText(
                                    DocumentsActivity.this,
                                    "Document uploadé avec succès",
                                    Toast.LENGTH_SHORT
                            ).show();
                            loadDocuments();
                        } else {
                            Toast.makeText(
                                    DocumentsActivity.this,
                                    "Erreur upload (" + response.code() + ")",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<ResponseBody> call,
                            Throwable t
                    ) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(
                                DocumentsActivity.this,
                                "Erreur : " + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    /* ===============================
       Charger les documents
       =============================== */

    private void loadDocuments() {

        progressBar.setVisibility(View.VISIBLE);

        String url =
                "http://10.41.223.37:9090/api/documents/etudiant/" + etudiantId;

        com.android.volley.RequestQueue queue =
                com.android.volley.toolbox.Volley.newRequestQueue(this);

        com.android.volley.toolbox.JsonArrayRequest request =
                new com.android.volley.toolbox.JsonArrayRequest(
                        com.android.volley.Request.Method.GET,
                        url,
                        null,
                        response -> {
                            com.google.gson.Gson gson =
                                    new com.google.gson.Gson();
                            list = Arrays.asList(
                                    gson.fromJson(
                                            response.toString(),
                                            Document[].class
                                    )
                            );
                            recyclerView.setAdapter(
                                    new DocumentAdapter(
                                            this,
                                            new ArrayList<>(list)
                                    )
                            );
                            progressBar.setVisibility(View.GONE);
                        },
                        error -> {
                            Toast.makeText(
                                    this,
                                    "Erreur chargement documents",
                                    Toast.LENGTH_SHORT
                            ).show();
                            progressBar.setVisibility(View.GONE);
                        }
                );

        queue.add(request);
    }
}
