package com.aariyan.loan_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aariyan.loan_user.Model.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class SignUp extends AppCompatActivity {

    private EditText userName, emailId, password, phoneNumber;
    private TextView signUpBtn, signInText;
    private CardView googleCard, facebookCard;

    private FirebaseAuth userAuth;
    private DatabaseReference userRef;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("User");

        phoneCallback();

        initUI();
    }

    private void phoneCallback() {

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                // Log.d(TAG, "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                //Log.w(TAG, "onVerificationFailed", e);

                Log.d("TEST_RESULT", "" + e.getMessage());

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                //Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                Intent intent = new Intent(SignUp.this, OTP_verification.class);
                intent.putExtra("name", userName.getText().toString());
                intent.putExtra("email", emailId.getText().toString());
                intent.putExtra("phone", phoneNumber.getText().toString());
                intent.putExtra("password", password.getText().toString());
                intent.putExtra("otp", verificationId);
                startActivity(intent);

            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {


    }

    private void initUI() {

        userName = findViewById(R.id.userName);
        emailId = findViewById(R.id.emailId);
        password = findViewById(R.id.password);
        phoneNumber = findViewById(R.id.phoneNumber);
        signUpBtn = findViewById(R.id.signUpForNow);
        signInText = findViewById(R.id.singInText);
        googleCard = findViewById(R.id.googleLogInCard);
        facebookCard = findViewById(R.id.facebookLogInCard);

        signUpBtn.setOnClickListener(v -> {
            checkValidation();
        });

        signInText.setOnClickListener(v -> {
            startActivity(new Intent(SignUp.this, LogIn.class));
        });
    }

    private void checkValidation() {
        if (TextUtils.isEmpty(userName.getText().toString())) {
            userName.setError("Please enter valid Name");
            userName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(emailId.getText().toString())) {
            emailId.setError("Please enter valid Email");
            emailId.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phoneNumber.getText().toString())) {
            phoneNumber.setError("Please enter valid Phone Number");
            phoneNumber.requestFocus();
            return;
        }

        if (phoneNumber.getText().toString().length() > 14 ||
                phoneNumber.getText().toString().length() < 13) {
            phoneNumber.setError("Phone number should be valid");
            phoneNumber.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password.getText().toString())) {
            password.setError("Please enter valid Password");
            password.requestFocus();
            return;
        }

        if (password.getText().toString().length() < 6) {
            password.setError("Password length should be at least 6!");
            password.requestFocus();
            return;
        }


        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(userAuth)
                        .setPhoneNumber(phoneNumber.getText().toString().trim())       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }


}