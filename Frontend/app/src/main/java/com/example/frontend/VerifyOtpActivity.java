package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.frontend.api.AuthApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VerifyOtpActivity extends AppCompatActivity {

    EditText otpField;
    Button verifyOtpBtn;
    String email;

    private AuthApi authApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        otpField = findViewById(R.id.otpField);
        verifyOtpBtn = findViewById(R.id.verifyOtpBtn);
        email = getIntent().getStringExtra("email");

        // Initialiser Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.16:9090/") // URL du backend
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        authApi = retrofit.create(AuthApi.class);

        verifyOtpBtn.setOnClickListener(v -> verify());
    }

    private void verify() {
        String otp = otpField.getText().toString().trim();

        if (otp.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer l'OTP", Toast.LENGTH_SHORT).show();
            return;
        }

        // Appel backend pour vérifier OTP
        authApi.verifyOtp(email, otp).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean isValid = response.body();
                    if (isValid) {
                        // OTP correct → Aller à ResetPasswordActivity
                        Intent i = new Intent(VerifyOtpActivity.this, ResetPasswordActivity.class);
                        i.putExtra("email", email);
                        i.putExtra("otp", otp);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(VerifyOtpActivity.this, "OTP incorrect", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(VerifyOtpActivity.this, "Erreur serveur", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(VerifyOtpActivity.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
