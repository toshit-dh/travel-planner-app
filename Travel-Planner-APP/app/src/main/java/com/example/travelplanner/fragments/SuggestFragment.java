package com.example.travelplanner.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelplanner.R;
import com.example.travelplanner.adapters.SuggestionAdapter;
import com.example.travelplanner.api.RetrofitInstance;
import com.example.travelplanner.api.StringResponse;
import com.example.travelplanner.api.SuggestionData;
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

public class SuggestFragment extends Fragment {
    private LinearLayout addSuggestion;
    private LinearLayout seeSuggestion;
    private RecyclerView mySuggestionRecycler;
    private RecyclerView otherSuggestionRecycler;
    private AutoCompleteTextView autoCompleteTextViewTag;
    private FloatingActionButton switchLayout;
    private AutoCompleteTextView autoCompleteTextViewCountry;
    private AutoCompleteTextView autoCompleteTextViewCity;
    private EditText msg;
    private AutoCompleteTextView seeTag;
    private EditText seeCity;
    private ImageView searchSugg;
    private Button button;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suggest, container, false);
        seeTag = view.findViewById(R.id.tagsugg);
        seeCity = view.findViewById(R.id.citysugg);
        addSuggestion = view.findViewById(R.id.addsuggLay);
        seeSuggestion = view.findViewById(R.id.seesuggLay);
        switchLayout = view.findViewById(R.id.switchLay);
        autoCompleteTextViewTag = view.findViewById(R.id.autotag);
        autoCompleteTextViewCountry = view.findViewById(R.id.autocountry);
        searchSugg = view.findViewById(R.id.searchsuggbyother);
        autoCompleteTextViewCity = view.findViewById(R.id.autocity);
        msg = view.findViewById(R.id.suggmsg);
        mySuggestionRecycler = view.findViewById(R.id.mysuggestion);
        otherSuggestionRecycler = view.findViewById(R.id.suggestionChat);
        button = view.findViewById(R.id.addSuggestionbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateInputs()){
                String tagText = autoCompleteTextViewTag.getText().toString();
                String countryText = autoCompleteTextViewCountry.getText().toString();
                String cityText = autoCompleteTextViewCity.getText().toString();
                String messageText = msg.getText().toString();
                Toast.makeText(requireContext(),tagText,Toast.LENGTH_SHORT).show();
                Map<String, String> userHeaders = new HashMap<>();
                userHeaders.put("Content-Type", "application/json");
                userHeaders.put("Authorization", MyPrefs.getToken(requireContext()));
                RetrofitInstance.getInstance().apiInterface.onAddSuggestion(tagText,cityText,countryText,messageText,userHeaders).enqueue(new Callback<StringResponse>() {
                    @Override
                    public void onResponse(Call<StringResponse> call, Response<StringResponse> response) {
                        if(response.isSuccessful()){
                            StringResponse stringResponse = response.body();
                            Toast.makeText(requireContext(),stringResponse.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Log.e("error",response.errorBody().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<StringResponse> call, Throwable t) {
                        Log.e("servererror",t.getMessage());
                    }
                });
            }}
        });
        switchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSuggestion.setVisibility(View.VISIBLE);
                seeSuggestion.setVisibility(View.GONE);
                otherSuggestionRecycler.setVisibility(View.GONE);
                switchLayout.setVisibility(View.GONE);
                getSuggestion();
            }
        });
        List<String> countries = loadCountriesFromJson(requireContext(), R.raw.countriesndcities);
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, countries);
        autoCompleteTextViewCountry.setAdapter(countryAdapter);

        autoCompleteTextViewCountry.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedCountry = parent.getItemAtPosition(position).toString();
            // Load cities for the selected country from JSON
            List<String> cities = loadCitiesFromJson(requireContext(), R.raw.countriesndcities, selectedCountry);
            ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, cities);
            autoCompleteTextViewCity.setAdapter(cityAdapter);
        });

        String[] tags = {"Destination Details", "Travel Itinerary", "Accommodation", "Transportation", "Budget and Expenses", "Emergency Contacts", "Notes and Reminders", "Packing List", "Health and Safety Information", "Travel Documents", "Weather Information", "Personal Preferences"};
        ArrayAdapter<String> tagAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, tags);
        autoCompleteTextViewTag.setAdapter(tagAdapter);
        seeTag.setAdapter(tagAdapter);
        searchSugg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(seeTag.getText().toString()!=null && seeTag.getText().toString().equals("") && seeCity.getText().toString()!=null && seeCity.getText().toString().equals("")){
                    Toast.makeText(requireContext(),"Fill all fields.",Toast.LENGTH_SHORT).show();
                }else {
                    Map<String, String> userHeaders = new HashMap<>();
                    userHeaders.put("Content-Type", "application/json");
                    userHeaders.put("Authorization", MyPrefs.getToken(requireContext()));
                    RetrofitInstance.getInstance().apiInterface.onSearchSuggestion(seeTag.getText().toString(),seeCity.getText().toString(),userHeaders).enqueue(new Callback<List<SuggestionData>>() {
                        @Override
                        public void onResponse(Call<List<SuggestionData>> call, Response<List<SuggestionData>> response) {
                            if(response.isSuccessful()){
                                List<SuggestionData> suggestionData = response.body();
                                SuggestionAdapter suggestionAdapter = new SuggestionAdapter(suggestionData,false);
                                otherSuggestionRecycler.setAdapter(suggestionAdapter);
                                otherSuggestionRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
                            }
                            else {
                                Log.e("error",response.errorBody().toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<List<SuggestionData>> call, Throwable t) {
                            Log.e("servererror",t.getMessage());
                        }
                    });
                }
            }
        });

        return view;
    }

    private List<String> loadCountriesFromJson(Context context, int resourceId) {
        List<String> countries = new ArrayList<>();
        try {
            InputStream is = context.getResources().openRawResource(resourceId);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(json);
            JSONArray countryArray = jsonObject.names();

            if (countryArray != null) {
                for (int i = 0; i < countryArray.length(); i++) {
                    countries.add(countryArray.getString(i));
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return countries;
    }

    private List<String> loadCitiesFromJson(Context context, int resourceId, String selectedCountry) {
        List<String> cities = new ArrayList<>();
        try {
            InputStream is = context.getResources().openRawResource(resourceId);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(json);
            JSONArray cityArray = jsonObject.getJSONArray(selectedCountry);

            for (int i = 0; i < cityArray.length(); i++) {
                cities.add(cityArray.getString(i));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return cities;
    }
    private boolean validateInputs() {
        String tagText = autoCompleteTextViewTag.getText().toString();
        String countryText = autoCompleteTextViewCountry.getText().toString();
        String cityText = autoCompleteTextViewCity.getText().toString();
        String messageText = msg.getText().toString();

        // Validate that all AutoCompleteTextViews have valid selections
        if (tagText.isEmpty() || countryText.isEmpty() || cityText.isEmpty()) {
            return false;
        }

        // Validate that the entered texts match items in their respective adapters
        if (!containsItem(autoCompleteTextViewTag, tagText)) {
            return false;
        }

        if (!containsItem(autoCompleteTextViewCountry, countryText)) {
            return false;
        }

        if (!containsItem(autoCompleteTextViewCity, cityText)) {
            return false;
        }
        if(messageText.length() >= 100){
            return true;
        }else {
            msg.setError("Suggestion should be greater than 100 characters.");
            return false;
        }
    }

    private boolean containsItem(AutoCompleteTextView autoCompleteTextView, String text) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) autoCompleteTextView.getAdapter();
        if (adapter != null) {
            for (int i = 0; i < adapter.getCount(); i++) {
                if (adapter.getItem(i) != null && adapter.getItem(i).equals(text)) {
                    return true;
                }
            }
        }
        autoCompleteTextView.setError("Type donot match");
        return false;
    }
    private void getSuggestion(){
        Map<String, String> userHeaders = new HashMap<>();
        userHeaders.put("Content-Type", "application/json");
        userHeaders.put("Authorization", MyPrefs.getToken(requireContext()));
        RetrofitInstance.getInstance().apiInterface.onSuggestion(userHeaders).enqueue(new Callback<List<SuggestionData>>() {
            @Override
            public void onResponse(Call<List<SuggestionData>> call, Response<List<SuggestionData>> response) {
                List<SuggestionData> suggestionData = response.body();
                SuggestionAdapter suggestionAdapter = new SuggestionAdapter(suggestionData,true);
                mySuggestionRecycler.setAdapter(suggestionAdapter);
                mySuggestionRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
            }

            @Override
            public void onFailure(Call<List<SuggestionData>> call, Throwable t) {

            }
        });
    }
}


