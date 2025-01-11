package com.example.garageapp.CustomerFragments;

import android.content.Intent;
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
    private static final String BASE_URL = "http://172.19.33.199/public_html/Android/Customerphp/car.php";
    private ActivityResultLauncher<Intent> addCarActivityLauncher;
    private ImageView imageno;
    private TextView text;

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
}
