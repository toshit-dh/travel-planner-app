package com.example.travelplanner.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.travelplanner.R;
import com.example.travelplanner.adapters.UsersAdapter;
import com.example.travelplanner.api.ChatItems;
import com.example.travelplanner.api.RetrofitInstance;
import com.example.travelplanner.api.StringResponse;
import com.example.travelplanner.data.MyPrefs;
import com.example.travelplanner.utils.RealPathUtil;
import com.google.gson.Gson;


import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanFragment extends Fragment {
    private Button filePick;
    private Button submit;
    private ImageView filePath;
    private TextView date;
    private TextView retdate;
    private EditText arrivalCity;
    private TextView deptime;
    private MultiAutoCompleteTextView tripmates;
    private String path;
    private List<ChatItems.User> friendsList;
    private static final int PICK_FILE_REQUEST_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_plan, container, false);
        submit = rootView.findViewById(R.id.pickfilebtnsubmit);
        date = rootView.findViewById(R.id.datePickerdep);
        retdate = rootView.findViewById(R.id.datePickertarr);
        deptime = rootView.findViewById(R.id.timepickerdep);
        arrivalCity = rootView.findViewById(R.id.arrivalcity);
        filePick = rootView.findViewById(R.id.pickfilebtn);
        filePath = rootView.findViewById(R.id.pathtext);
        tripmates = rootView.findViewById(R.id.tripmates);
        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showDatePickerDialog(date);
            }
        });
        retdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(retdate);
            }
        });
        deptime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(deptime);
            }
        });
        filePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFile(view);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit.setVisibility(View.GONE);
                filePath.setVisibility(View.GONE);
                upLoad();
            }
        });
        Map<String, String> userHeaders = new HashMap<>();
        userHeaders.put("Content-Type", "application/json");
        userHeaders.put("Authorization", MyPrefs.getToken(requireContext()));
        RetrofitInstance.getInstance().apiInterface.onChatItems(userHeaders).enqueue(new Callback<ChatItems>() {
            @Override
            public void onResponse(Call<ChatItems> call, Response<ChatItems> response) {
                if (response.isSuccessful()) {
                    ChatItems chatItems = response.body();
                    friendsList = chatItems.getFriends();
                    Log.e("hvy", friendsList.toString());
                    List<String> friendNames = new ArrayList<>();
                    for (ChatItems.User user : friendsList) {
                        friendNames.add(user.getName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, friendNames);
                    tripmates.setAdapter(adapter);
                    tripmates.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
                } else {
                    Log.e("error", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<ChatItems> call, Throwable t) {
                Log.e("servererrorplan", t.getMessage());
            }
        });
        return rootView;
    }


    private void pickFile(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        String[] mimeTypes = {"image/jpeg", "image/png", "image/jpg"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(Intent.createChooser(intent, "Choose an image"), PICK_FILE_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == getActivity().RESULT_OK) {
            Uri selectedFileUri = data.getData();
            if (selectedFileUri != null) {
                path = RealPathUtil.getRealPath(requireContext(), selectedFileUri);
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                filePath.setVisibility(View.VISIBLE);
                filePath.setImageBitmap(bitmap);
                submit.setVisibility(View.VISIBLE);
            }
        }
    }

    private void upLoad() {

        if (arrivalCity.getText() == null || date.getText() == null || retdate.getText() == null || deptime.getText() == null) {
            Toast.makeText(requireContext(), "Fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        String city = arrivalCity.getText().toString();
        Map<String, String> userHeaders = new HashMap<>();
        userHeaders.put("Authorization", MyPrefs.getToken(requireContext()));

        String dateStr = (String) date.getText();
        String retDateStr = (String) retdate.getText();
        String retTimeStr = (String) deptime.getText();
        File file = new File(path);
        String selectedTripMates = tripmates.getText().toString();
        String[] selectedMatesArray = selectedTripMates.split(","); // Assuming ',' as the separator
        Map<String, String> selectedMatesMap = new HashMap<>();
        for (ChatItems.User user : friendsList) {
            selectedMatesMap.put(user.getName(), user.get_id());
        }
        ArrayList<String> selectedMatesIds = new ArrayList<>();
        for (String mateName : selectedMatesArray) {
            if (selectedMatesMap.containsKey(mateName.trim())) {
                selectedMatesIds.add(selectedMatesMap.get(mateName.trim()));
            }
        }
        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        RequestBody dateBody = RequestBody.create(MediaType.parse("multipart/form-data"), dateStr);
        RequestBody returndateBody = RequestBody.create(MediaType.parse("multipart/form-data"), retDateStr);
        RequestBody departureBody = RequestBody.create(MediaType.parse("multipart/form-data"), retTimeStr);
        RequestBody arrcity = RequestBody.create(MediaType.parse("multipart/form-data"), city);
        RequestBody ticket = RequestBody.create(MediaType.parse(mimeType), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), ticket);
        RetrofitInstance.getInstance().apiInterface.onTripData(dateBody, returndateBody, departureBody, arrcity, selectedMatesIds, body, userHeaders).enqueue(new Callback<StringResponse>() {
            @Override
            public void onResponse(Call<StringResponse> call, Response<StringResponse> response) {
                if (response.isSuccessful()) {
                    date.setText("");date.setHint("YYYY-MM-DD");
                    retdate.setText("");retdate.setHint("YYYY-MM-DD");
                    deptime.setText("");deptime.setHint("HH:MM");
                    arrivalCity.setText("");arrivalCity.setHint("Arrival City");
                    tripmates.setText("");tripmates.setHint("Add Friends");
                    StringResponse msg = response.body();
                    Toast.makeText(requireContext(),msg.getMsg(),Toast.LENGTH_SHORT).show();
                } else {
                    Log.i("succ1", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<StringResponse> call, Throwable t) {
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

    private void showTimePickerDialog(TextView textView) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                        String formattedHour = selectedHour < 10 ? "0" + selectedHour : String.valueOf(selectedHour);
                        String formattedMinute = selectedMinute < 10 ? "0" + selectedMinute : String.valueOf(selectedMinute);
                        String selectedTime = formattedHour + ":" + formattedMinute;
                        textView.setText(selectedTime);
                    }
                },
                hour, minute, true);
        timePickerDialog.show();
    }
}