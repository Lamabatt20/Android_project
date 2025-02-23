package com.example.garageapp;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class AddCarActivity extends AppCompatActivity {
    private String pathurl= "http://172.19.33.18";
    private final String BASE_URL = pathurl+"/public_html/Android/Customerphp/add_car.php";
    private int customerId;  // To hold the customer ID
    private Uri selectedImageUri;  // To hold the URI of the selected image

    private EditText licensePlateInput, modelInput, makeInput, yearInput, odometerInput, engineInput, companyInput, carsNameInput;
    private Spinner transmissionInput;
    private ImageView addPhoto;

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    ImageView photoPreview = findViewById(R.id.photo_preview);
                    photoPreview.setVisibility(View.VISIBLE);
                    photoPreview.setImageURI(selectedImageUri);
                    addPhoto.setVisibility(View.GONE);
                    TextView text=findViewById(R.id.textadd);
                    text.setVisibility(View.GONE);


                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        customerId = getIntent().getIntExtra("customer_id", -1);

        licensePlateInput = findViewById(R.id.licensePlateInput);
        modelInput = findViewById(R.id.modelInput);
        makeInput = findViewById(R.id.makeInput);
        yearInput = findViewById(R.id.yearInput);
        odometerInput = findViewById(R.id.odometerInput);
        engineInput = findViewById(R.id.engineInput);
        transmissionInput = findViewById(R.id.transmissionSpinner);
        companyInput = findViewById(R.id.companyInput);
        carsNameInput = findViewById(R.id.nameInput);
        addPhoto = findViewById(R.id.add_photo);

        Button saveButton = findViewById(R.id.add_car_button);
        String[] transmissionOptions = {"Automatic", "Manual"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, transmissionOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transmissionInput.setAdapter(adapter);
        saveButton.setOnClickListener(v -> saveCarData());

        addPhoto.setOnClickListener(v -> openImageChooser());
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void saveCarData() {
        // Get data from input fields
        String licensePlate = licensePlateInput.getText().toString().trim();
        String model = modelInput.getText().toString().trim();
        String make = makeInput.getText().toString().trim();
        String year = yearInput.getText().toString().trim();
        String odometer = odometerInput.getText().toString().trim();
        String engineSpecification = engineInput.getText().toString().trim();
        String transmission = transmissionInput.getSelectedItem().toString().trim();
        String company = companyInput.getText().toString().trim();
        String carsName = carsNameInput.getText().toString().trim();

        // Validate that all fields are filled
        if(carsName.isEmpty()){
            Toast.makeText(this, "Please enter car name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (licensePlate.isEmpty() || model.isEmpty() || make.isEmpty() || year.isEmpty() ||
                odometer.isEmpty() || engineSpecification.isEmpty() || transmission.isEmpty() ||
                company.isEmpty() ) {
            Toast.makeText(this, "Please fill out every field.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if an image is selected
        if (selectedImageUri == null) {
            Toast.makeText(this, "Please select an image.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Encode data
        String photoPath = encodeInput(selectedImageUri.toString());
        String url = BASE_URL + "?customer_id=" + customerId +
                "&licensePlate=" + encodeInput(licensePlate) +
                "&model=" + encodeInput(model) +
                "&make=" + encodeInput(make) +
                "&year=" + encodeInput(year) +
                "&odometer=" + encodeInput(odometer) +
                "&engineSpecification=" + encodeInput(engineSpecification) +
                "&transmission=" + encodeInput(transmission) +
                "&company=" + encodeInput(company) +
                "&cars_name=" + encodeInput(carsName) +
                "&photo=" + photoPath;

        // Send the request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(AddCarActivity.this, response, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("car_added", true);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddCarActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(AddCarActivity.this).add(stringRequest);
    }


    private String encodeInput(String input) {
        try {
            return URLEncoder.encode(input, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

}