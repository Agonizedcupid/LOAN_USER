package com.aariyan.loan_user.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aariyan.loan_user.Common.Common;
import com.aariyan.loan_user.Model.UserModel;
import com.aariyan.loan_user.NotificationActivity;
import com.aariyan.loan_user.R;
import com.aariyan.loan_user.UserDetailsActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.aariyan.loan_user.Common.Common.DEFAULT;
import static com.aariyan.loan_user.Common.Common.userId;
import static com.aariyan.loan_user.Common.Common.userRef;


public class HomeFragment extends Fragment {

    private CircleImageView profileIcon;
    private LinearLayout loanLinearLayout, userDetailsLinearLayout, transactionLinearLayout, calculatorLinerLayout;

    private static String name, phone;
    private TextView userName;

    private FirebaseAuth userAuth;

    private ImageView notificationIcon;
    private String userID = "";

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Common.root = inflater.inflate(R.layout.fragment_home, container, false);
        initUI();
        return Common.root;
    }

    private void initUI() {
        profileIcon = Common.root.findViewById(R.id.profileIcon);
        loanLinearLayout = Common.root.findViewById(R.id.loanLinearLayout);
        userDetailsLinearLayout = Common.root.findViewById(R.id.userDetailsLinearLayout);
        transactionLinearLayout = Common.root.findViewById(R.id.transactionLinearLayout);
        calculatorLinerLayout = Common.root.findViewById(R.id.calculatorLinearLayout);
        userName = Common.root.findViewById(R.id.userNameInMainPage);

        notificationIcon = Common.root.findViewById(R.id.notificationIcon);

        notificationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), NotificationActivity.class));
            }
        });

        userAuth = FirebaseAuth.getInstance();

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("data", Context.MODE_PRIVATE);

        if (userAuth.getCurrentUser() == null) {
            name = sharedPreferences.getString("name", DEFAULT);
            phone = sharedPreferences.getString("phone", DEFAULT);

            Toast.makeText(requireContext(), "" + phone, Toast.LENGTH_SHORT).show();

            userName.setText("Hi, " + name);

            Common.userRef.orderByChild("phoneNumber").equalTo(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            UserModel model = snapshot.getValue(UserModel.class);
                            assert model != null;

                            if (model.getImageUrl().equals("null")) {
                                profileIcon.setImageResource(R.drawable.profile);
                            } else {
                                if (getActivity() == null) {
                                    return;
                                }
                                Glide.with(requireContext()).load(model.getImageUrl()).error(R.drawable.profile).into(profileIcon);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else {
            userRef.orderByChild("userId").equalTo(userAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            UserModel model = dataSnapshot.getValue(UserModel.class);
                            userName.setText("Hi, " + model.getUserName());
                            Glide.with(getContext()).load(model.getImageUrl()).into(profileIcon);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new ProfileFragment()).commit();
            }
        });

        loanLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new ApplyForLoanFragment()).commit();
            }
        });

        userDetailsLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userAuth.getCurrentUser() == null) {
                    userID = phone;
                    //Common.userRef.child(phone).child("imageUrl").setValue(uri.toString());
                } else {
                    userID = userId();
                    //Common.userRef.child(Common.userId()).child("imageUrl").setValue(uri.toString());
                }

                Common.userDetailsRef.orderByChild("userId").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Intent intent = new Intent(requireContext(), UserDetailsActivity.class);
                            intent.putExtra("id",userID);
                            requireContext().startActivity(intent);
                        } else {
                            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new UserDetailsFragment()).commit();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

            }
        });

        transactionLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new TransactionFragment()).commit();
            }
        });

        calculatorLinerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new CalculatorFragment()).commit();
            }
        });
    }
}