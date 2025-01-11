package com.example.garageapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class Registartion2 extends AppCompatActivity {
    private TextInputEditText edtUsername, edtIdentity, edtPassword, edtPassword2;
    private Button btnCreateAccount;
    private TextView txtLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registartion2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextInputLayout usernameLayout = findViewById(R.id.edtUsername);
        TextInputLayout identityLayout = findViewById(R.id.edtIdentity);
        TextInputLayout passwordLayout = findViewById(R.id.edtPassword);
        TextInputLayout confirmPasswordLayout = findViewById(R.id.edtPassword2);

        // Access the inner TextInputEditText
        edtUsername = (TextInputEditText) usernameLayout.getEditText();
        edtIdentity = (TextInputEditText) identityLayout.getEditText();
        edtPassword = (TextInputEditText) passwordLayout.getEditText();
        edtPassword2 = (TextInputEditText) confirmPasswordLayout.getEditText();
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        txtLogin = findViewById(R.id.txtLogin);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String phone = intent.getStringExtra("phone");
        String authorization = intent.getStringExtra("authorization");
        String role = intent.getStringExtra("role");

        btnCreateAccount.setOnClickListener(v -> {
            // Get input values
            String username = edtUsername.getText().toString().trim();
            String identity = edtIdentity.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String confirmPassword = edtPassword2.getText().toString().trim();

            // Validate input
            if (username.isEmpty() || identity.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(Registartion2.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(Registartion2.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Proceed to register the user
            registerUser(name, email, phone, authorization, role, username, identity, password);
        });

        // Clean all the edit text
        edtUsername.setText("");
        edtIdentity.setText("");
        edtPassword.setText("");
        edtPassword2.setText("");

        // Redirect to login screen
        txtLogin.setOnClickListener(v -> startActivity(new Intent(Registartion2.this, Login.class)));
    }

    private void registerUser(String name, String email, String phone, String authorization, String role,
                              String username, String identity, String password) {
        String url = "http://172.19.33.199/public_html/Android/register.php";  // Adjust the URL as needed

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Create a POST request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Check response from the server
                        if (response.contains("Registration successful!")) {
                            Toast.makeText(Registartion2.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Registartion2.this, Login.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Registartion2.this, "Registration failed: " + response, Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Registartion2.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Add parameters to the request

                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("phone", phone);
                if(authorization != null) {
                    params.put("authorization", authorization);
                } else if (authorization == null) {
                    params.put("authorization", "  ");

                }
                params.put("role", role);
                params.put("username", username);
                params.put("identity", identity);
                params.put("password", password);
                return params;
            }
        };

        // Add the request to the RequestQueue
        queue.add(stringRequest);

        // Show progress message while waiting
        Toast.makeText(this, "Registration in progress...", Toast.LENGTH_SHORT).show();
    }
}
