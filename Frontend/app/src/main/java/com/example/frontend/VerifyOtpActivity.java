package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class VerifyOtpActivity extends AppCompatActivity {

    EditText otpField;
    Button verifyOtpBtn;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        otpField = findViewById(R.id.otpField);
        verifyOtpBtn = findViewById(R.id.verifyOtpBtn);

        email = getIntent().getStringExtra("email");

        verifyOtpBtn.setOnClickListener(v -> verify());
    }

    private void verify() {
        String otp = otpField.getText().toString();

        Intent i = new Intent(this, ResetPasswordActivity.class);
        i.putExtra("email", email);
        i.putExtra("otp", otp);
        startActivity(i);
    }
}
