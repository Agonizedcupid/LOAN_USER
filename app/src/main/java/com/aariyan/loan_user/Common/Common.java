package com.aariyan.loan_user.Common;

import android.content.SharedPreferences;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Common {

    public static View root;
    public static DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User");
    public static DatabaseReference transactionRef = FirebaseDatabase.getInstance().getReference().child("Transactions");
    public static DatabaseReference loanRef = FirebaseDatabase.getInstance().getReference().child("ApplyForLoan");
    public static DatabaseReference userDetailsRef = FirebaseDatabase.getInstance().getReference().child("UserDetails");
    public static DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference().child("Notification");
    public static DatabaseReference loanListRef = FirebaseDatabase.getInstance().getReference().child("LoanList");
    public static DatabaseReference adminNotificationRef = FirebaseDatabase.getInstance().getReference().child("Admin_Notification");

    public static FirebaseAuth userAuth = FirebaseAuth.getInstance();
    public static String userId = "";

    public static String userIdForNotification = "";

    public static final String DEFAULT = "N/A";

    public static String userId () {
        if (userAuth.getCurrentUser() != null) {
            userId = userAuth.getCurrentUser().getUid();
        } else {
            userId = "";
        }

        return userId;
    }

    public static  String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static  String getCurrentTime() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

}
