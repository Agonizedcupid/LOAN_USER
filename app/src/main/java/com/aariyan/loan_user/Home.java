package com.aariyan.loan_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.aariyan.loan_user.Common.Common;
import com.aariyan.loan_user.Fragment.FavoriteFragment;
import com.aariyan.loan_user.Fragment.HomeFragment;
import com.aariyan.loan_user.Notification.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.aariyan.loan_user.Common.Common.DEFAULT;

public class Home extends AppCompatActivity {

    private TabLayout tabLayout;
    private FirebaseAuth auth;
    public static String User_token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();

        initUi();

        retrieveToken();
    }

    private void retrieveToken() {

//        SharedPreferences sharedPreference = getSharedPreferences("TOKEN_SAVING", Context.MODE_PRIVATE);
//
//        User_token = sharedPreference.getString("FCM_TOKEN", DEFAULT);
//        updateToken(User_token);

        if (auth.getCurrentUser() == null) {
            SharedPreferences sharedPreference = getSharedPreferences("TOKEN_SAVING", Context.MODE_PRIVATE);

            User_token = sharedPreference.getString("FCM_TOKEN", DEFAULT);
            updateToken(User_token);
        } else {

            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                return;
                            }

                            // Get new FCM registration token
                            String token = task.getResult();
                            updateToken(token);

                        }
                    });

        }


    }

    private void updateToken(String token) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tokens");

        Token token1 = new Token(token);

        if (auth.getCurrentUser() != null) {
            databaseReference.child(auth.getCurrentUser().getUid()).setValue(token1);
        } else {
            if (Common.userIdForNotification != null) {
                databaseReference.child(Common.userIdForNotification).setValue(token1);
            }
            //here will be copied from Loan Admin app
        }


    }

    private void openNavigationMenu(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }

    private void initUi() {

        tabLayout = findViewById(R.id.tabs);
        Objects.requireNonNull(tabLayout.getTabAt(0)).getIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
        openNavigationMenu(new HomeFragment());


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(Home.this, R.color.white);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);

                if (tab.getPosition() == 0) {
                    openNavigationMenu(new HomeFragment());
                } else if (tab.getPosition() == 1) {
                    openNavigationMenu(new FavoriteFragment());
                } else if (tab.getPosition() == 2) {
                    openNavigationMenu(new FavoriteFragment());
                } else if (tab.getPosition() == 3) {
                    openNavigationMenu(new FavoriteFragment());
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#f2a309"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}