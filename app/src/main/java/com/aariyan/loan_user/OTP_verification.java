package com.aariyan.loan_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aariyan.loan_user.Common.Common;
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

import java.util.concurrent.TimeUnit;

import static com.aariyan.loan_user.Common.Common.DEFAULT;
import static com.aariyan.loan_user.Common.Common.userRef;

public class OTP_verification extends AppCompatActivity {

    private static String name, email, phone, password, otp, givenOTP;
    private EditText firstCode, secondCode, thirdCode, fourthCode, fifthCode, sixthCode;
    private TextView resendCode, verificationOTP;

    private FirebaseAuth userAuth;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_verification);

        userAuth = FirebaseAuth.getInstance();


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

                //signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                //Log.w(TAG, "onVerificationFailed", e);

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
//                mVerificationId = verificationId;
//                mResendToken = token;
//
//                Intent intent = new Intent(SignUp.this, OTP_verification.class);
//                intent.putExtra("name", userName.getText().toString());
//                intent.putExtra("email", emailId.getText().toString());
//                intent.putExtra("phone", phoneNumber.getText().toString());
//                intent.putExtra("password", password.getText().toString());
//                intent.putExtra("otp", verificationId);
//                startActivity(intent);

                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, givenOTP);
                if (givenOTP.equals(credential.getSmsCode())) {
                    //Toast.makeText(this, "Matched", Toast.LENGTH_SHORT).show();

                    //Create Sign up options:
                    signUp();

                } else {
                    Toast.makeText(OTP_verification.this, "not matched", Toast.LENGTH_SHORT).show();
                }
            }
        };


        if (getIntent() != null) {
            name = getIntent().getStringExtra("name");
            email = getIntent().getStringExtra("email");
            phone = getIntent().getStringExtra("phone");
            password = getIntent().getStringExtra("password");
            otp = getIntent().getStringExtra("otp");
        }

        initUI();

    }

    private void initUI() {
        firstCode = findViewById(R.id.firstCode);
        secondCode = findViewById(R.id.secondCode);
        thirdCode = findViewById(R.id.thirdCode);
        fourthCode = findViewById(R.id.fourthCode);
        fifthCode = findViewById(R.id.fifthCode);
        sixthCode = findViewById(R.id.sixthCode);
        resendCode = findViewById(R.id.resendText);
        verificationOTP = findViewById(R.id.verifyOtp);


        verificationOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });

        numberOTPMOve();

        resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOTP();
            }
        });
    }

    private void resendOTP() {
        startActivity(new Intent(OTP_verification.this,SignUp.class));

//        PhoneAuthOptions options =
//                PhoneAuthOptions.newBuilder(userAuth)
//                        .setPhoneNumber(phone)       // Phone number to verify
//                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                        .setActivity(this)                 // Activity (for callback binding)
//                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                            @Override
//                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//
//                            }
//
//                            @Override
//                            public void onVerificationFailed(@NonNull FirebaseException e) {
//
//                            }
//
//                            @Override
//                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                                otp = s;
//                            }
//                        })
//                        // OnVerificationStateChangedCallbacks
//                        .build();
//        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void numberOTPMOve() {
        firstCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty()) {
                    secondCode.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        secondCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty()) {
                    thirdCode.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        thirdCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty()) {
                    fourthCode.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fourthCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty()) {
                    fifthCode.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fifthCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty()) {
                    sixthCode.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void validation() {

        if (!TextUtils.isEmpty(firstCode.getText().toString().trim())
                && !TextUtils.isEmpty(secondCode.getText().toString().trim())
                && !TextUtils.isEmpty(thirdCode.getText().toString().trim())
                && !TextUtils.isEmpty(fourthCode.getText().toString().trim())
                && !TextUtils.isEmpty(fifthCode.getText().toString().trim())
                && !TextUtils.isEmpty(sixthCode.getText().toString().trim())
        ) {
             givenOTP = firstCode.getText().toString() + secondCode.getText().toString() + thirdCode.getText().toString() +
                    fourthCode.getText().toString() + fifthCode.getText().toString() + sixthCode.getText().toString();


            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otp, givenOTP);
            if (givenOTP.equals(credential.getSmsCode())) {
                //Toast.makeText(this, "Matched", Toast.LENGTH_SHORT).show();

                //Create Sign up options:
                signUp();

            } else {
                Toast.makeText(this, "not matched", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please enter the OTP", Toast.LENGTH_SHORT).show();
        }
    }

    private void signUp() {
        UserModel model = new UserModel(
                "",
                name,
                email,
                phone,
                password,
                "null"
        );

        userRef.child(phone).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("phone",phone);
                editor.putString("name",name);
                editor.commit();

                Common.userIdForNotification = sharedPreferences.getString("phone",DEFAULT);

                Toast.makeText(OTP_verification.this, "Sign Up Success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(OTP_verification.this, Home.class));
            }
        });

//        userAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//            @Override
//            public void onSuccess(AuthResult authResult) {
//
//                String id = userAuth.getCurrentUser().getUid();
//
//                UserModel model = new UserModel(
//                        id,
//                        name,
//                        email,
//                        phone,
//                       password,
//                        "null"
//                );
//
//                userRef.child(id).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(OTP_verification.this, "Sign Up Success", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(OTP_verification.this, Home.class));
//                    }
//                });
//            }
//        });
    }
}