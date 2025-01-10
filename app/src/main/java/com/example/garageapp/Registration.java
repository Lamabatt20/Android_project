package com.example.garageapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Registration extends AppCompatActivity {
    RadioButton radioEmployee;
    RadioButton radioCustomer;
    EditText edtName;
    EditText edtEmail;
    EditText edtPhone;
    EditText edtAuthorization;
    Button btnNext;
    TextView txtLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        radioEmployee = findViewById(R.id.radioEmployee);
        radioCustomer = findViewById(R.id.radioCustomer);
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtAuthorization = findViewById(R.id.edtAuthorization);
        btnNext = findViewById(R.id.btnNext);
        txtLogin = findViewById(R.id.txtLogin);

        //if the user checks the employee radio button, show the authorization edit text
        radioEmployee.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                edtAuthorization.setVisibility(View.VISIBLE);
            } else {
                edtAuthorization.setVisibility(View.GONE);
            }
        });



        btnNext.setOnClickListener(this::onClick);

        //clean all the edit text
        edtName.setText("");
        edtEmail.setText("");
        edtPhone.setText("");

        txtLogin.setOnClickListener(v -> {
            // Start the login activity
            startActivity(new Intent(Registration.this, Login.class));
        });

    }

    private void onClick(View v) {
        // Start the next activity
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String authorization = radioEmployee.isChecked() ? edtAuthorization.getText().toString().trim() : null;

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || (radioEmployee.isChecked() && (authorization == null || authorization.isEmpty()))) {
            Toast.makeText(Registration.this, "Please fill all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (radioEmployee.isChecked()) {
            // Validate authorization code
            validateAuthorizationCode(authorization, isValid -> {
                if (isValid) {
                    proceedToNextScreen(name, email, phone, authorization);
                } else {
                    // Show error message
                    Toast.makeText(Registration.this, "Invalid authorization code.", Toast.LENGTH_SHORT).show();
                    return;
                }
            });
        } else {
            proceedToNextScreen(name, email, phone, null);
        }
    }

    private void validateAuthorizationCode(String authorization, Consumer<Boolean> callback) {
        String url = "http://192.168.1.108/public_html/Android/validate_auth_code.php";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> callback.accept(response.equals("true")),
                error -> {
                    Toast.makeText(this, "Error validating authorization code: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    callback.accept(false);
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("authorization", authorization);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    private void proceedToNextScreen(String name, String email, String phone, String authorization) {
        Intent intent = new Intent(Registration.this, Registartion2.class);
        intent.putExtra("name", name);
        intent.putExtra("email", email);
        intent.putExtra("phone", phone);
        intent.putExtra("authorization", authorization);
        intent.putExtra("role", radioEmployee.isChecked() ? "Employee" : "Customer");
        startActivity(intent);
    }
}