package com.aariyan.loan_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.aariyan.loan_user.Adapter.NotificationAdapter;
import com.aariyan.loan_user.Common.Common;
import com.aariyan.loan_user.Model.NotificationModel;
import com.facebook.share.Share;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.aariyan.loan_user.Common.Common.DEFAULT;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private FirebaseAuth userAuth;

    private List<NotificationModel> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        userAuth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        initUi();

        loadNotification();
    }

    private void loadNotification() {
        if (userAuth.getCurrentUser() == null) {
            Common.userIdForNotification = sharedPreferences.getString("phone",DEFAULT);
        } else {
            Common.userIdForNotification = userAuth.getCurrentUser().getUid();
        }

        Common.notificationRef.orderByChild("type").equalTo(Common.userIdForNotification).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    list.clear();
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                        NotificationModel model = snapshot.getValue(NotificationModel.class);
                        list.add(model);
                    }

                    NotificationAdapter adapter = new NotificationAdapter(NotificationActivity.this,list);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(NotificationActivity.this, "No notification found for you!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

                Toast.makeText(NotificationActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void initUi() {
        recyclerView = findViewById(R.id.notificationRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}