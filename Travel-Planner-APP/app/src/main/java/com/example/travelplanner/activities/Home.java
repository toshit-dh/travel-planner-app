package com.example.travelplanner.activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.travelplanner.R;
import com.example.travelplanner.api.ChatItems;
import com.example.travelplanner.api.SocketManager;
import com.example.travelplanner.data.MyPrefs;
import com.example.travelplanner.data.UserData;
import com.example.travelplanner.fragments.AboutFragment;

import com.example.travelplanner.fragments.GroupChatFragment;
import com.example.travelplanner.fragments.MapFragment;
import com.example.travelplanner.fragments.NotifFragment;
import com.example.travelplanner.fragments.SuggestFragment;
import com.example.travelplanner.fragments.HomeFragment;
import com.example.travelplanner.fragments.ItenaryFragment;
import com.example.travelplanner.fragments.ExploreFragment;
import com.example.travelplanner.fragments.PlanFragment;
import com.example.travelplanner.fragments.ChatFragment;
import com.example.travelplanner.fragments.ProfileFragment;
import com.example.travelplanner.fragments.AccessibilityFragment;
import com.example.travelplanner.fragments.TripFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Transport;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
//    ActivityMainBinding binding;
    DrawerLayout  drawerLayout;
    Toolbar toolbar;
    FragmentManager fragmentManager;
    FloatingActionButton fab;
    BottomNavigationView bottomNavigationView;
    //private DrawerLayout drawerLayout;
    ImageView notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gson gson = new Gson();
        String data = MyPrefs.getData(Home.this);
        UserData userData = gson.fromJson(data, UserData.class);
        String userId = userData.get_id();
        setContentView(R.layout.nav_main);
        fab = findViewById(R.id.fab);
        notification = findViewById(R.id.notifButton);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        Socket socket = SocketManager.getInstance();
        socket.connect();
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Home.this.runOnUiThread(new Runnable() {
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
                Home.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Throwable error = (Throwable) args[0];
                        error.printStackTrace();
                        Log.e("sockey",error.getMessage());
                    }
                });
            }
        });
        socket.emit("joinnotif",userId);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openfragment(new NotifFragment());
            }
        });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemid = item.getItemId();
                if (itemid == R.id.home) {
                    openfragment(new HomeFragment());
                    return true;
                } else if (itemid == R.id.trip) {
                    openfragment(new TripFragment());
                    return true;
                } else if (itemid == R.id.offers) {
                    openfragment(new ChatFragment(new OnItemClickListener() {
                        @Override
                        public void onItemClick(ChatItems.Trip trip) {
                            openfragment(new GroupChatFragment(trip));
                        }
                    }));
                    return true;
                } else if (itemid == R.id.calendar) {
                    openfragment(new ExploreFragment());
                    return true;
                }
                return false;
            }
        });

        fragmentManager = getSupportFragmentManager();
        openfragment(new HomeFragment());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog();
            }
        });
    }
    private void showBottomDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);
        LinearLayout trip = dialog.findViewById(R.id.planatrip);
        LinearLayout guides = dialog.findViewById(R.id.planitenary);
        LinearLayout chat = dialog.findViewById(R.id.suggestions);
        LinearLayout locate = dialog.findViewById(R.id.locate);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);
        trip.setOnClickListener(v -> {
            dialog.dismiss();
            openfragment(new PlanFragment());
        });
        guides.setOnClickListener(v -> {

            dialog.dismiss();

            openfragment(new ItenaryFragment());

        });
        chat.setOnClickListener(v -> {
            dialog.dismiss();
            openfragment(new SuggestFragment());
        });
        locate.setOnClickListener(v -> {
            dialog.dismiss();
            openfragment(new MapFragment());
        });
        cancelButton.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemid = item.getItemId();
        if (itemid == R.id.nav_home) {
            openfragment(new AccessibilityFragment());
        } else if (itemid == R.id.nav_settings) {
            openfragment(new com.example.travelplanner.fragments.SettingsFragment());
        } else if (itemid == R.id.nav_profile) {
           openfragment(new ProfileFragment());
        } else if (itemid == R.id.nav_about) {
            openfragment(new AboutFragment());
        } else if (itemid == R.id.nav_logout) {
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            String url = "https://github.com/toshit-dh/travel-planner-app-using-java";
            ClipData clip = ClipData.newPlainText("url",url);
            clipboardManager.setPrimaryClip(clip);
            Toast.makeText(this, "Link Copied!\n"+url, Toast.LENGTH_SHORT).show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private void openfragment (Fragment fragment){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}