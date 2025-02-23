package com.example.garageapp.CustomerFragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.garageapp.Login;
import com.example.garageapp.R;

import org.json.JSONException;
import org.json.JSONObject;


public class CustomerProfileFragment extends Fragment {

    private ImageView userImageView;
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText phoneNumberEditText;
    private EditText authenticationCodeEditText;
    private TextView tapToChangeTextView;
    private Button updateButton;
    private Uri selectedImageUri;
    private ImageView logoutIcon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        userImageView = view.findViewById(R.id.user_image);
        usernameEditText = view.findViewById(R.id.username);
        emailEditText = view.findViewById(R.id.email);
        phoneNumberEditText = view.findViewById(R.id.phone_number);
        authenticationCodeEditText = view.findViewById(R.id.authentication_code);
        tapToChangeTextView = view.findViewById(R.id.tap_to_change);
        updateButton = view.findViewById(R.id.update_button);
        logoutIcon = view.findViewById(R.id.logout);

        if (getArguments() != null) {
            int userId = getArguments().getInt("user_id", -1);
            String userRole = getArguments().getString("role");

            if (userId != -1) {
                loadUserProfile(userId, userRole);
            } else {
                Toast.makeText(getActivity(), "Invalid User", Toast.LENGTH_SHORT).show();
            }

            tapToChangeTextView.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            });

            updateButton.setOnClickListener(v -> updateUserProfile(userId, userRole));
            logoutIcon.setOnClickListener(v -> logout());
        }

        return view;
    }

    private void loadUserProfile(int userId, String role) {
        String url = "http://172.19.33.18/public_html/Android/Customerphp/getUserProfile.php?user_id=" + userId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject userJson = new JSONObject(response);

                            String username = userJson.getString("username");
                            String email = userJson.getString("email");
                            String phone = userJson.getString("phone_number");
                            String imageUrl = userJson.getString("photo");
                            String authCode = userJson.getString("authentication_code_id");

                            usernameEditText.setText(username);
                            emailEditText.setText(email);
                            phoneNumberEditText.setText(phone);

                            if (imageUrl.startsWith("/")) {
                                imageUrl = "http://172.19.33.18" + imageUrl;
                            }

                            Glide.with(getContext())
                                    .load(imageUrl)
                                    .placeholder(R.drawable.user_placeholder)
                                    .into(userImageView);

                            if ("Admin".equals(role)) {
                                authenticationCodeEditText.setVisibility(View.VISIBLE);
                                authenticationCodeEditText.setText(authCode);
                            } else {
                                authenticationCodeEditText.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error loading profile", Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == getActivity().RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {

                String imageUrl = selectedImageUri.toString();
                if (imageUrl.startsWith("content://")) {

                    Glide.with(this)
                            .load(selectedImageUri)
                            .placeholder(R.drawable.user_placeholder)
                            .into(userImageView);
                }
            }
        }
    }

    private void updateUserProfile(int userId, String role) {
        String updatedUsername = usernameEditText.getText().toString();
        String updatedEmail = emailEditText.getText().toString();
        String updatedPhone = phoneNumberEditText.getText().toString();
        String updatedAuthCode = authenticationCodeEditText.getText().toString();

        String imageUrl = selectedImageUri != null ? selectedImageUri.toString() : "";

        String url = "http://172.19.33.18/public_html/Android/Customerphp/UpdateUserProfile.php";

        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("user_id", userId);
            jsonBody.put("username", updatedUsername);
            jsonBody.put("email", updatedEmail);
            jsonBody.put("phone_number", updatedPhone);
            jsonBody.put("authentication_code_id", updatedAuthCode);
            jsonBody.put("photo", imageUrl);
            jsonBody.put("role", role);

            JsonObjectRequest jsonRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.d("UpdateResponse", "Response: " + response.toString());  // Log the response for debugging

                                String successMessage = response.getString("success");  // Get the success message
                                Toast.makeText(getContext(), successMessage, Toast.LENGTH_SHORT).show();  // Show the success message

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Response parsing error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), "Error updating profile", Toast.LENGTH_SHORT).show();
                        }
                    }
            );

            Volley.newRequestQueue(getContext()).add(jsonRequest);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error creating JSON body", Toast.LENGTH_SHORT).show();
        }
    }

    private void logout() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_preferences", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("user_id");
        editor.remove("role");
        editor.apply();

        Intent loginIntent = new Intent(getContext(), Login.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        getActivity().finish();
    }
}