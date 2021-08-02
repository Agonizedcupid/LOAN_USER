package com.aariyan.loan_user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aariyan.loan_user.Common.Common;

import static com.aariyan.loan_user.Common.Common.DEFAULT;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText firstPassword,confirmPassword;
    private TextView savePassword;

    SharedPreferences sharedPreferences;
    String phone;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        initUI();
    }

    private void initUI() {
        firstPassword = findViewById(R.id.firstPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        savePassword = findViewById(R.id.savePassword);

        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        phone = sharedPreferences.getString("phone",DEFAULT);

        savePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstPassword.getText().toString().equals(confirmPassword.getText().toString())) {
                    Common.userRef.child(phone).child("password").setValue(firstPassword.getText().toString());
                    editor.putString("active", "NOT");
                    editor.commit();

                    startActivity(new Intent(ResetPasswordActivity.this,LogIn.class));
                    finish();

                } else {
                    Toast.makeText(ResetPasswordActivity.this, "Password Doesn't match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}