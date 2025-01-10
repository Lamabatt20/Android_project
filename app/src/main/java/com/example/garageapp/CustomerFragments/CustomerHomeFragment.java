package com.example.garageapp.CustomerFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.garageapp.AddCarActivity;
import com.example.garageapp.CarAdapter;
import com.example.garageapp.CarDetails;
import com.example.garageapp.R;
import com.example.garageapp.Car;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CustomerHomeFragment extends Fragment {
    private List<Car> items = new ArrayList<>();
    private RecyclerView recyclerView;
    private CarAdapter adapter;
    private int customerId;
    private static final String BASE_URL = "http://192.168.1.108/public_html/Android/Customerphp/car.php";
    private ActivityResultLauncher<Intent> addCarActivityLauncher;
    private ImageView imageno;
    private TextView text;
    private static final String PREF_NAME = "GarageAppPreferences";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_home, container, false);

        // Retrieve arguments passed from the parent activity/fragment
        if (getArguments() != null) {
            customerId = getArguments().getInt("customer_id", -1);
        }

        // Initialize UI components
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        imageno = view.findViewById(R.id.no_cars_image);
        text = view.findViewById(R.id.no_cars_text);

        adapter = new CarAdapter(getContext(), items, position -> {
            Car selectedCar = items.get(position);
            Intent intent = new Intent(getContext(), CarDetails.class);
            intent.putExtra("car_id", String.valueOf(selectedCar.getCarId()));
            intent.putExtra("car_photo", selectedCar.getPhoto());
            Log.d("CarSelection", selectedCar.getCarId() + "");
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        addCarActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getBooleanExtra("car_added", false)) {
                            items.clear();
                            loadItems();
                        }
                    }
                }
        );

        Button addCarButton = view.findViewById(R.id.add_car_button);
        addCarButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddCarActivity.class);
            intent.putExtra("customer_id", customerId);
            addCarActivityLauncher.launch(intent);
        });

        loadItems();
        return view;
    }

    private void loadItems() {
        // First, try to load cars from SharedPreferences
        List<Car> savedCars = getCarsFromSharedPreferences(getContext());
        if (savedCars != null && !savedCars.isEmpty()) {
            items.clear();
            items.addAll(savedCars);
            updateUI();
        } else {
            // If no saved cars, load them from the server
            String url = BASE_URL + "?customer_id=" + customerId;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                items.clear();
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    String name = object.getString("cars_name");
                                    String photo = object.getString("photo");
                                    int carId = object.getInt("car_id");
                                    items.add(new Car(name, photo, carId));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            // Save the cars to SharedPreferences after loading from the server
                            saveCarsToSharedPreferences(getContext(), items);
                            updateUI();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    });

            Volley.newRequestQueue(getContext()).add(stringRequest);
        }
    }

    private void updateUI() {
        if (items.isEmpty()) {
            imageno.setVisibility(View.VISIBLE);
            text.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            imageno.setVisibility(View.GONE);
            text.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
    }

    // Save cars to SharedPreferences
    private void saveCarsToSharedPreferences(Context context, List<Car> cars) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            JSONArray jsonArray = new JSONArray();
            for (Car car : cars) {
                JSONObject carObject = new JSONObject();
                carObject.put("cars_name", car.getCars_name());
                carObject.put("photo", car.getPhoto());
                carObject.put("car_id", car.getCarId());
                jsonArray.put(carObject);
            }
            editor.putString("cars_data", jsonArray.toString());
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get cars from SharedPreferences
    private List<Car> getCarsFromSharedPreferences(Context context) {
        List<Car> cars = new ArrayList<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String carsJson = sharedPreferences.getString("cars_data", "[]");

        try {
            JSONArray jsonArray = new JSONArray(carsJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject carObject = jsonArray.getJSONObject(i);
                String carName = carObject.getString("cars_name");
                String photo = carObject.getString("photo");
                int carId = carObject.getInt("car_id");

                Car car = new Car(carName, photo, carId);
                cars.add(car);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cars;
    }
}
