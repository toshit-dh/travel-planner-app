package com.example.travelplanner.fragments;
import java.text.ParseException;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import com.example.travelplanner.R;
import com.example.travelplanner.adapters.ActivityAdapter;
import com.example.travelplanner.adapters.FlightDataAdapter;
import com.example.travelplanner.adapters.HotelAdapter;
import com.example.travelplanner.api.ActivityData;
import com.example.travelplanner.api.FlightsData;
import com.example.travelplanner.api.HotelData;
import com.example.travelplanner.api.RetrofitInstance;
import com.example.travelplanner.data.MyPrefs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExploreFragment extends Fragment {
    private ProgressBar progressBar;
    private FloatingActionButton floatingActionButton;
    private RecyclerView hotelRecyclerView;
    private RecyclerView flightDataRecyclerview;
    private RecyclerView activityRecyclerView;
    private HotelAdapter hotelAdapter;
    private FlightDataAdapter flightDataAdapter;
    private ActivityAdapter activityAdapter;
    private AutoCompleteTextView searchCity;
    private AutoCompleteTextView seachDest;
    private TextView searchhotel;
    private List<String> cityNames;
    private TextView hotelGet;
    private TextView dateText;
    private TextView flightsGet;
    private TextView activitiesGet;
    private TextView pickFlightDate;
    private LinearLayout departureDate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        floatingActionButton = view.findViewById(R.id.fabhotelprice);
        progressBar = view.findViewById(R.id.progressBar);
        hotelRecyclerView = view.findViewById(R.id.hotelrecyclerview);
        activityRecyclerView = view.findViewById(R.id.activityrecyclerview);
        searchCity = view.findViewById(R.id.searchcity);
        pickFlightDate = view.findViewById(R.id.pickdateflight);
        departureDate = view.findViewById(R.id.lineardeparture);
        dateText = view.findViewById(R.id.pickdateflightsettext);
        seachDest = view.findViewById(R.id.searchdest);
        flightDataRecyclerview = view.findViewById(R.id.recyclerViewFlightData);
        int rawResourceId = R.raw.city;
        String jsonData = loadJSONFromResource(rawResourceId);
        hotelGet = view.findViewById(R.id.hotelget);
        flightsGet = view.findViewById(R.id.flightget);
        activitiesGet = view.findViewById(R.id.activitiesget);
        cityNames = extractCityNames(jsonData);
        searchhotel = view.findViewById(R.id.hotel);
        searchhotel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search, 0, 0, 0);
        searchhotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search = searchhotel.getText().toString();
                Log.i("hotel",search);
                switch (search){
                    case "Hotels in: ": {
                        onSearchHotel();
                        break;
                    }
                    case "Flights: ": {
                        onSearchFlights();
                    }
                    case "Activity in: " : {
                        onSearchActivity();
                    }
                }

            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, cityNames);
        searchCity.setAdapter(adapter);
        seachDest.setAdapter(adapter);
        flightsGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                departureDate.setVisibility(View.VISIBLE);
                floatingActionButton.setVisibility(View.GONE);
                seachDest.setVisibility(View.VISIBLE);
                searchhotel.setText("Flights: ");
                hotelRecyclerView.setVisibility(View.GONE);
                flightDataRecyclerview.setVisibility(View.VISIBLE);
            }
        });
        hotelGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                departureDate.setVisibility(View.GONE);
                seachDest.setVisibility(View.GONE);
                floatingActionButton.setVisibility(View.VISIBLE);
                searchhotel.setText("Hotels in: ");
                flightDataRecyclerview.setVisibility(View.GONE);
                hotelRecyclerView.setVisibility(View.VISIBLE);
            }
        });
        activitiesGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                departureDate.setVisibility(View.GONE);
                floatingActionButton.setVisibility(View.GONE);
                seachDest.setVisibility(View.GONE);
                searchhotel.setText("Activity in: ");
                flightDataRecyclerview.setVisibility(View.GONE);
                hotelRecyclerView.setVisibility(View.GONE);
            }
        });
        pickFlightDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(dateText);
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HotelPrice hotelPrice = new HotelPrice();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, hotelPrice)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }
    private String loadJSONFromResource(int resourceId) {
        String jsonData = null;
        try {
            InputStream inputStream = getResources().openRawResource(resourceId);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            jsonData = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return jsonData;
    }

    private List<String> extractCityNames(String jsonData) {
        List<String> names = new ArrayList<>();
        try {
            if (jsonData != null) {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String cityName = jsonObject.optString("name");
                    names.add(cityName);
                }
            } else {
                Log.e("FavouritesFragment", "JSON data is null");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("FavouritesFragment", "Error parsing JSON");
        }
        return names;
    }

    private void onSearchHotel() {
        String enteredCity = searchCity.getText().toString().trim();
        if (!enteredCity.isEmpty()) {
            for (String cityName : cityNames) {
                if (enteredCity.equalsIgnoreCase(cityName)) {
                    String iataCode = getIATACodeForCity(cityName);
                    if (iataCode != null) {
                        searchHotelApi(iataCode);
                        return;
                    }
                }
            }
            searchCity.setError("No matching city found.");
        } else {
            searchCity.setError("Please enter a city name.");
        }
    }
    private void onSearchFlights() {
        String srcCity = searchCity.getText().toString().trim();
        String destCity = seachDest.getText().toString().trim();
        if (!srcCity.isEmpty() && !destCity.isEmpty()) {
            String srcIATACode = getIATACodeForCity(srcCity);
            String destIATACode = getIATACodeForCity(destCity);
            if (srcIATACode != null && destIATACode != null) {
                String departureDateText = dateText.getText().toString().trim();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Calendar selectedDate = Calendar.getInstance();
                try {
                    selectedDate.setTime(sdf.parse(departureDateText));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                selectedDate.add(Calendar.DAY_OF_MONTH, 5);
                String plus5 = String.format(Locale.getDefault(), "%04d-%02d-%02d",
                        selectedDate.get(Calendar.YEAR),
                        selectedDate.get(Calendar.MONTH) + 1,
                        selectedDate.get(Calendar.DAY_OF_MONTH));
                searchFlightsApi(srcIATACode, destIATACode,departureDateText,plus5);
            } else {
                Toast.makeText(requireContext(), "Invalid source or destination city.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "Please enter source and destination cities.", Toast.LENGTH_SHORT).show();
        }
    }

    private void onSearchActivity(){
        searchActivityApi();
    }
    private void searchFlightsApi(String srcIATACode, String destIATACode,String departureDate,String plus5day) {


        Map<String, String> userHeaders = new HashMap<>();
        userHeaders.put("Content-Type", "application/json");
        userHeaders.put("Authorization", MyPrefs.getToken(requireContext()));
        progressBar.setVisibility(View.VISIBLE);
        RetrofitInstance.getInstance().apiInterface.onFlightData(srcIATACode,destIATACode,departureDate,plus5day,userHeaders).enqueue(new Callback<List<FlightsData>>() {
            @Override
            public void onResponse(Call<List<FlightsData>> call, Response<List<FlightsData>> response) {
                searchhotel.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    List<FlightsData> flightsData = response.body();
                    Log.i("dataflight", flightsData.toString());
                    flightDataAdapter = new FlightDataAdapter(flightsData);
                    flightDataRecyclerview.setAdapter(flightDataAdapter);
                    flightDataRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
                }else {
                    Toast.makeText(requireContext(),"Cannot get data",Toast.LENGTH_SHORT).show();
                    Log.e("error","flighterror");
                }

            }

            @Override
            public void onFailure(Call<List<FlightsData>> call, Throwable t) {
                Log.e("servererror",t.getMessage());
            }
        });
    }
    private String getIATACodeForCity(String cityName) {
        try {
            if (cityNames != null) {
                JSONArray jsonArray = new JSONArray(loadJSONFromResource(R.raw.city));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String name = jsonObject.optString("name");
                    if (name.equalsIgnoreCase(cityName)) {
                        return jsonObject.optString("iata");
                    }
                }
            } else {
                Log.e("FavouritesFragment", "City names list is null");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Log or handle the JSON parsing exception
            Log.e("FavouritesFragment", "Error parsing JSON");
        }
        return null;
    }
    private void searchHotelApi(String iata) {
        searchhotel.setEnabled(false);
        Map<String, String> userHeaders = new HashMap<>();
        userHeaders.put("Content-Type", "application/json");
        userHeaders.put("Authorization", MyPrefs.getToken(requireContext()));
        progressBar.setVisibility(View.VISIBLE);
        RetrofitInstance.getInstance().apiInterface.onHotelData(iata, userHeaders).enqueue(new Callback<List<HotelData>>() {
            @Override
            public void onResponse(Call<List<HotelData>> call, Response<List<HotelData>> response) {
                progressBar.setVisibility(View.GONE);
                searchhotel.setEnabled(true);
                if (response.isSuccessful()) {
                    List<HotelData> hotelData = response.body();
                    Log.i("datahotel", hotelData.toString());
                    hotelAdapter = new HotelAdapter(hotelData);
                    hotelRecyclerView.setAdapter(hotelAdapter);
                    hotelRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                } else {
                    Toast.makeText(requireContext(),"Cannot get data",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<HotelData>> call, Throwable t) {
                Log.e("servererr", t.getMessage());
            }
        });
    }
    private void searchActivityApi(){
        Map<String, String> userHeaders = new HashMap<>();
        userHeaders.put("Content-Type", "application/json");
        userHeaders.put("Authorization", MyPrefs.getToken(requireContext()));
        RetrofitInstance.getInstance().apiInterface.onActivityData("",userHeaders).enqueue(new Callback<List<ActivityData>>() {
            @Override
            public void onResponse(Call<List<ActivityData>> call, Response<List<ActivityData>> response) {
                if(response.isSuccessful()){
                    List<ActivityData> activityData= response.body();
                    activityAdapter = new ActivityAdapter(activityData);
                    activityRecyclerView.setAdapter(activityAdapter);
                    activityRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                }
                else{
                    Log.e("error",response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<List<ActivityData>> call, Throwable t) {
                Log.e("servererror",t.getMessage());
            }
        });

    }
    private void showDatePickerDialog(TextView textView) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        String formattedMonth = (selectedMonth + 1) < 10 ? "0" + (selectedMonth + 1) : String.valueOf(selectedMonth + 1);
                        String formattedDay = selectedDay < 10 ? "0" + selectedDay : String.valueOf(selectedDay);
                        String selectedDate = selectedYear + "-" + formattedMonth + "-" + formattedDay;
                        textView.setText(selectedDate);
                    }
                },
                year, month, day);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

}
