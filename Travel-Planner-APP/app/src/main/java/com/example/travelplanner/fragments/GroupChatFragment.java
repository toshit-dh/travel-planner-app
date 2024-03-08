package com.example.travelplanner.fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelplanner.R;
import com.example.travelplanner.adapters.ImagesAdapter;
import com.example.travelplanner.adapters.MessagesAdapter;
import com.example.travelplanner.api.ChatItems;
import com.example.travelplanner.api.RetrofitInstance;
import com.example.travelplanner.api.SocketManager;
import com.example.travelplanner.api.StringResponse;
import com.example.travelplanner.data.MyPrefs;
import com.example.travelplanner.data.RawMessage;
import com.example.travelplanner.data.TripChatsImages;
import com.example.travelplanner.data.UserData;
import com.example.travelplanner.utils.NotificationHelper;
import com.example.travelplanner.utils.RealPathUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Transport;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupChatFragment extends Fragment {
    private ChatItems.Trip trip;
    private TextView tripId;
    private TextView tripcity;;
    private TextView tripCreator;
    private EditText writeMessage;
    private RecyclerView groupMessages;
    private RecyclerView groupImages;
    private String foldername;
    private ImageView image;
    private LinearLayout tripDetail;
    private FloatingActionButton download;
    private LinearLayout bottom;
    private static final int PICK_FILE_REQUEST_CODE = 1;
    private TripChatsImages tripChatsImages;
    private MessagesAdapter messagesAdapter;
    private String userId;
    private UserData userData;
    String data;
    public GroupChatFragment(ChatItems.Trip trip){
        this.trip = trip;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_group_chat, container, false);
        Gson gson = new Gson();
        data = MyPrefs.getData(requireContext());
        userData = gson.fromJson(data,UserData.class);
        userId = userData.get_id();
        tripDetail = view.findViewById(R.id.lineartripDetail);
        tripId = view.findViewById(R.id.tripId);
        tripcity = view.findViewById(R.id.tripTo);
        tripCreator = view.findViewById(R.id.tripCreator);
        bottom = view.findViewById(R.id.writeMessagelinear);
        writeMessage = view.findViewById(R.id.writeMessage);
        groupMessages = view.findViewById(R.id.tripMessages);
        groupImages = view.findViewById(R.id.tripImages);
        image = view.findViewById(R.id.messageimage);
        tripId.setText(trip.get_id());
        tripcity.setText(trip.getCity());
        tripCreator.setText(trip.getCreator());
        download = view.findViewById(R.id.fabimageDownload);
        Map<String, String> userHeaders = new HashMap<>();
        userHeaders.put("Content-Type", "application/json");
        userHeaders.put("Authorization", MyPrefs.getToken(requireContext()));
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                String[] mimeTypes = {"image/jpeg", "image/png", "image/jpg"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent, "Choose an image"), PICK_FILE_REQUEST_CODE);
            }
        });
        Socket socket = SocketManager.getInstance();
        socket.connect();
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       Log.e("socket","connected");
                    }
                });
            }
        });
        socket.on(Manager.EVENT_TRANSPORT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Transport transport = (Transport) args[0];
                // Adding headers when EVENT_REQUEST_HEADERS is called
                transport.on(Transport.EVENT_REQUEST_HEADERS, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        Log.v(TAG, "Caught EVENT_REQUEST_HEADERS after EVENT_TRANSPORT, adding headers");
                        Map<String, List<String>> mHeaders = (Map<String, List<String>>)args[0];
                        mHeaders.put("Authorization", Arrays.asList("Basic bXl1c2VyOm15cGFzczEyMw=="));
                    }
                });
            }
        });
        socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Throwable error = (Throwable) args[0];
                        error.printStackTrace();
                        Log.e("sockey",error.getMessage());
                    }
                });
            }
        });
        socket.emit("joinRoom",trip.get_id());
        socket.on("newMessage", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Object object = args[0];
                Gson gson1 = new Gson();
                RawMessage rawMessage = gson1.fromJson(object.toString(), RawMessage.class);
                String fromWhoId = rawMessage.getFromWhoId();
                TripChatsImages.Message message;
                if(fromWhoId.equals(userId)){
                    message = new TripChatsImages.Message(rawMessage.getFromWho(),rawMessage.getMessage(),rawMessage.getIsType(),true);
                }
                else{
                    message = new TripChatsImages.Message(rawMessage.getFromWho(),rawMessage.getMessage(),rawMessage.getIsType(),rawMessage.getFromSelf());
                }
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tripChatsImages.getMessage().add(message);
                        messagesAdapter.notifyItemInserted(messagesAdapter.getItemCount() - 1);
                        groupMessages.smoothScrollToPosition(messagesAdapter.getItemCount()-1);
                    }
                });
            }
        });
        RetrofitInstance.getInstance().apiInterface.onGetMessages(trip.get_id(),userHeaders).enqueue(new Callback<TripChatsImages>() {
            @Override
            public void onResponse(Call<TripChatsImages> call, Response<TripChatsImages> response) {
                if(response.isSuccessful()){
                    tripChatsImages = response.body();
                    messagesAdapter = new MessagesAdapter(tripChatsImages.getMessage());
                    groupMessages.setAdapter(messagesAdapter);
                    groupMessages.setLayoutManager(new LinearLayoutManager(requireContext()));
                    if(messagesAdapter.getItemCount()>0){
                        groupMessages.smoothScrollToPosition(messagesAdapter.getItemCount()-1);
                    }
                    ImagesAdapter imagesAdapter = new ImagesAdapter(tripChatsImages.getImages());
                    groupImages.setAdapter(imagesAdapter);
                    Log.e("count",Integer.toString(imagesAdapter.getItemCount()));
                    groupImages.setLayoutManager(new GridLayoutManager(requireContext(),3,GridLayoutManager.VERTICAL,false));
                }
                else{
                    Log.e("error",response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<TripChatsImages> call, Throwable t) {
                Log.e("servererror",t.getMessage());
            }
        });
        writeMessage.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // Check if the event is the Enter key
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    RetrofitInstance.getInstance().apiInterface.onAddMessage(writeMessage.getText().toString(),trip.get_id(),userHeaders).enqueue(new Callback<StringResponse>() {
                        @Override
                        public void onResponse(Call<StringResponse> call, Response<StringResponse> response) {
                            if(response.isSuccessful()){
                                Log.i("succ","good");
                                writeMessage.setText("");
                            }
                            else{
                                Log.e("error","bad");
                            }
                        }

                        @Override
                        public void onFailure(Call<StringResponse> call, Throwable t) {
                            Log.e("servererror","verybad");
                        }
                    });
                    return true;  // Consume the event
                }
                return false;  // Continue with default behavior
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("travelplanner", "TravelPlanner", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = requireContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        tripDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(groupMessages.getVisibility() == View.VISIBLE){
                    bottom.setVisibility(View.GONE);
                    groupMessages.setVisibility(View.GONE);
                    groupImages.setVisibility(View.VISIBLE);
                    if(tripChatsImages.getMessage()!= null && !tripChatsImages.getMessage().isEmpty()){
                        download.setVisibility(View.VISIBLE);
                    }
                }
                else if(groupImages.getVisibility() == View.VISIBLE){
                    bottom.setVisibility(View.VISIBLE);
                    groupMessages.setVisibility(View.VISIBLE);
                    groupImages.setVisibility(View.GONE);
                    download.setVisibility(View.GONE);
                }
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            ArrayList<String> imageUrls = new ArrayList<>();
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<MultipartBody.Part> imageParts = getSelectedImages(data);
            Map<String, String> userHeaders = new HashMap<>();
            userHeaders.put("Authorization", MyPrefs.getToken(requireContext()));
            RetrofitInstance.getInstance().apiInterface.onAddImagesMessage(trip.get_id(),imageParts,userHeaders).enqueue(new Callback<StringResponse>() {
                @Override
                public void onResponse(Call<StringResponse> call, Response<StringResponse> response) {
                    if(response.isSuccessful()){
                        Log.i("succ","good");

                    }
                    else{
                        Log.e("error","bad");
                    }
                }
                @Override
                public void onFailure(Call<StringResponse> call, Throwable t) {
                    Log.i("succ","notgood");
                }
            });
        }
    }

    private ArrayList<MultipartBody.Part> getSelectedImages(Intent data) {
        ArrayList<MultipartBody.Part> imageParts = new ArrayList<>();
        if (data.getClipData() != null) {
            int count = data.getClipData().getItemCount();
            for (int i = 0; i < count; i++) {
                Uri imageUri = data.getClipData().getItemAt(i).getUri();
                File file = new File(getRealPathFromUri(imageUri));
                String mimeType = URLConnection.guessContentTypeFromName(file.getName());
                RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("images", file.getName(), requestFile);
                imageParts.add(body);
            }
        } else if (data.getData() != null) {
            Uri imageUri = data.getData();
            File file = new File(getRealPathFromUri(imageUri));
            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("images", file.getName(), requestFile);
            imageParts.add(body);
        }
        return imageParts;
    }

    private String getRealPathFromUri(Uri uri) {
        String path = RealPathUtil.getRealPath(requireContext(),uri);
        return path;
    }
}