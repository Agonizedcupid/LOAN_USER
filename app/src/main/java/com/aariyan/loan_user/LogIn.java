package com.aariyan.loan_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aariyan.loan_user.Common.Common;
import com.aariyan.loan_user.Model.UserModel;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import static com.aariyan.loan_user.Common.Common.DEFAULT;
import static com.aariyan.loan_user.Common.Common.userRef;

public class LogIn extends AppCompatActivity {

    private EditText emailId, password;
    private TextView logInBtn;

    private TextView signUpText;

    private CardView facebookCard, googleCard;

    private FirebaseAuth userAuth;

    private TextView forgotPassword;

    private RadioButton rememberBtn;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private GoogleSignInClient mGoogleSignInClient;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        userAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        googleLogIn();


        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        initUI();
    }

    private void googleLogIn() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                // Google Sign In failed, update UI appropriately
                //Log.w(TAG, "Google sign in failed", e);
            }
        }


        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        userAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = userAuth.getCurrentUser();
                            assert user != null;
                            String id = user.getUid();
                            UserModel model = new UserModel(
                                    id,
                                    user.getDisplayName(),
                                    user.getEmail(),
                                    ""+user.getPhoneNumber(),
                                    "",
                                    user.getPhotoUrl().toString()
                            );

                            userRef.child(id).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(LogIn.this, "log-In Success", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LogIn.this, Home.class));
                                    finish();
                                }
                            });


                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //updateUI(null);
                            Toast.makeText(LogIn.this, "Sign in failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {

        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        String status = sharedPreferences.getString("active", DEFAULT);

        if (userAuth.getCurrentUser() == null) {
            Common.userIdForNotification = sharedPreferences.getString("phone",DEFAULT);
            if (status.equals("on")) {
                startActivity(new Intent(LogIn.this, Home.class));
                rememberBtn.setChecked(true);
                finish();
            } else {
                rememberBtn.setChecked(false);
            }
        } else if (userAuth.getCurrentUser() != null) {
            startActivity(new Intent(LogIn.this, Home.class));
        }



        super.onStart();
    }

    private void initUI() {
        emailId = findViewById(R.id.logInEmailId);
        password = findViewById(R.id.logInPassword);
        logInBtn = findViewById(R.id.signInNowText);
        signUpText = findViewById(R.id.singUpText);
        facebookCard = findViewById(R.id.facebookLogInCard);
        googleCard = findViewById(R.id.googleLogInCard);
        forgotPassword = findViewById(R.id.forgotPasswordText);
        rememberBtn = findViewById(R.id.rememberBtn);

        googleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        facebookCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithFacebook();
            }
        });

        rememberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("active", "on");
                editor.commit();

                rememberBtn.setChecked(true);
            }
        });

        signUpText.setOnClickListener(v -> {
            startActivity(new Intent(LogIn.this, SignUp.class));
        });

        logInBtn.setOnClickListener(v -> {
            checkValidation();
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogIn.this, ForgetPhoneNumberActivity.class));
            }
        });
    }

    private void signInWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(LogIn.this, Arrays.asList("email"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
       // Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        userAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                           // Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = userAuth.getCurrentUser();

                            assert user != null;
                            String id = user.getUid();
                            UserModel model = new UserModel(
                                    id,
                                    user.getDisplayName(),
                                    user.getEmail(),
                                    ""+user.getPhoneNumber(),
                                    "",
                                    user.getPhotoUrl().toString()
                            );

                            userRef.child(id).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(LogIn.this, "log-In Success", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LogIn.this, Home.class));
                                    finish();
                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                           // Toast.makeText(FacebookLoginActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Pass the activity result back to the Facebook SDK
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//    }

    private void checkValidation() {
        if (TextUtils.isEmpty(emailId.getText().toString())) {
            emailId.setError("Please enter valid Email");
            emailId.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password.getText().toString())) {
            password.setError("Please enter valid Password");
            password.requestFocus();
            return;
        }

        userRef.orderByChild("emailId").equalTo(emailId.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        UserModel model = dataSnapshot.getValue(UserModel.class);
                        String pass = model.getPassword();
                        String email = model.getEmailId();

                        if (password.getText().toString().equals(pass) && email.equals(emailId.getText().toString())) {
                            editor.putString("email", emailId.getText().toString());
                            editor.putString("name",model.getUserName());
                            editor.putString("phone",model.getPhoneNumber());
                            editor.commit();

                            Common.userIdForNotification = sharedPreferences.getString("phone",DEFAULT);

                            Toast.makeText(LogIn.this, "Log-In Success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LogIn.this, Home.class));
                            finish();
                        } else {
                            Toast.makeText(LogIn.this, "Invalid Data!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LogIn.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

//        userAuth.signInWithEmailAndPassword(emailId.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//            @Override
//            public void onSuccess(AuthResult authResult) {
//                Toast.makeText(LogIn.this, "Log-In Success", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(LogIn.this,Home.class));
//                finish();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });


    }
}