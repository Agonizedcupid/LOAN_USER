package com.aariyan.loan_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aariyan.loan_user.Common.Common;
import com.aariyan.loan_user.Model.UserDetailsModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import com.ceylonlabs.imageviewpopup.ImagePopup;

import org.jetbrains.annotations.NotNull;

public class UserDetailsActivity extends AppCompatActivity {

    private EditText userName, permanentAddress, currentAddress, AadharNumber, panNumber, bankName, branch, IFSCCOde, bacnkAccountNumber;
    private EditText dob,gender,memberId;

    private TextView updateDetails;
    private ImageView deleteDetails;

    private String id = "";

    private ImageView firstImage,secondImage,thirdImage;
    ImagePopup imagePopupOne, imagePopupTwo, imagePopupThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        imagePopupOne = new ImagePopup(UserDetailsActivity.this);
        imagePopupTwo = new ImagePopup(UserDetailsActivity.this);
        imagePopupThree = new ImagePopup(UserDetailsActivity.this);

        if (getIntent() != null) {
            id = getIntent().getStringExtra("id");
        }

        initUI();

        loadData();

        deleteDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.userDetailsRef.child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        startActivity(new Intent(UserDetailsActivity.this,Home.class));
                        finish();
                        Toast.makeText(UserDetailsActivity.this, "Your Information Deleted!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void loadData() {
        Common.userDetailsRef.orderByChild("userId").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                        UserDetailsModel model = snapshot.getValue(UserDetailsModel.class);
                        assert model != null;
                        userName.setText(model.getUserName(), TextView.BufferType.EDITABLE);
                        permanentAddress.setText(model.getPermanentAddress(), TextView.BufferType.EDITABLE);
                        currentAddress.setText(model.getCurrentAddress(), TextView.BufferType.EDITABLE);
                        AadharNumber.setText(model.getAadharNumber(), TextView.BufferType.EDITABLE);
                        panNumber.setText(model.getPanNumber(), TextView.BufferType.EDITABLE);
                        bankName.setText(model.getBankName(), TextView.BufferType.EDITABLE);
                        branch.setText(model.getBranch(), TextView.BufferType.EDITABLE);
                        IFSCCOde.setText(model.getCode(), TextView.BufferType.EDITABLE);
                        bacnkAccountNumber.setText(model.getBankAccount(), TextView.BufferType.EDITABLE);
                        dob.setText(model.getDob(), TextView.BufferType.EDITABLE);
                        gender.setText(model.getGender(), TextView.BufferType.EDITABLE);
                        memberId.setText(model.getMemberId(), TextView.BufferType.EDITABLE);

                        imagePopupOne.setFullScreen(true);
                        imagePopupTwo.setFullScreen(true);
                        imagePopupThree.setFullScreen(true);

                        if (model.getList().size() >=3) {
                            Glide.with(UserDetailsActivity.this).load(model.getList().get(0).getImageUrl())
                                    .into(firstImage);

                            Glide.with(UserDetailsActivity.this).load(model.getList().get(1).getImageUrl())
                                    .into(secondImage);

                            Glide.with(UserDetailsActivity.this).load(model.getList().get(2).getImageUrl())
                                    .into(thirdImage);
                        } else if (model.getList().size() == 2) {
                            Glide.with(UserDetailsActivity.this).load(model.getList().get(0).getImageUrl())
                                    .into(firstImage);

                            Glide.with(UserDetailsActivity.this).load(model.getList().get(1).getImageUrl())
                                    .into(secondImage);
                        } else if (model.getList().size() == 1) {
                            Glide.with(UserDetailsActivity.this).load(model.getList().get(0).getImageUrl())
                                    .into(firstImage);
                        } else if (model.getList().size() < 1) {
                            Toast.makeText(UserDetailsActivity.this, "No Image Provided!", Toast.LENGTH_SHORT).show();
                        }

                        firstImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                imagePopupOne.initiatePopup(firstImage.getDrawable());
                                imagePopupOne.viewPopup();
                            }
                        });


                        secondImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                imagePopupTwo.initiatePopup(secondImage.getDrawable());
                                imagePopupTwo.viewPopup();
                            }
                        });

                        thirdImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                imagePopupThree.initiatePopup(thirdImage.getDrawable());
                                imagePopupThree.viewPopup();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void initUI() {
        userName = findViewById(R.id.nameInUserDtls);
        permanentAddress = findViewById(R.id.permanentAddressInUserDtls);
        currentAddress = findViewById(R.id.currentAddressUserDtls);
        AadharNumber = findViewById(R.id.aadharNumberInUserDtls);
        panNumber = findViewById(R.id.panNumberInUserDtls);
        bankName = findViewById(R.id.bankNameInUserDtls);
        branch = findViewById(R.id.branchNameInUserDtls);
        IFSCCOde = findViewById(R.id.ifscCodeInUserDtls);
        bacnkAccountNumber = findViewById(R.id.bankAccountInUserDtls);
        memberId = findViewById(R.id.memberId);

        updateDetails = findViewById(R.id.updateDetails);
        deleteDetails = findViewById(R.id.deleteDetails);

        dob = findViewById(R.id.dobInUserDtls);
        gender = findViewById(R.id.genderInUserDtls);

        firstImage = findViewById(R.id.firstImage);
        secondImage = findViewById(R.id.secondImage);
        thirdImage = findViewById(R.id.thirdImage);

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        updateDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsUpdate();
            }
        });
    }

    private void detailsUpdate() {
        Common.userDetailsRef.child(id).child("aadharNumber").setValue(AadharNumber.getText().toString());
        Common.userDetailsRef.child(id).child("bankAccount").setValue(bacnkAccountNumber.getText().toString());
        Common.userDetailsRef.child(id).child("bankName").setValue(bankName.getText().toString());
        Common.userDetailsRef.child(id).child("branch").setValue(branch.getText().toString());
        Common.userDetailsRef.child(id).child("code").setValue(IFSCCOde.getText().toString());
        Common.userDetailsRef.child(id).child("currentAddress").setValue(currentAddress.getText().toString());
        Common.userDetailsRef.child(id).child("memberId").setValue(memberId.getText().toString());
        Common.userDetailsRef.child(id).child("panNumber").setValue(panNumber.getText().toString());
        Common.userDetailsRef.child(id).child("permanentAddress").setValue(permanentAddress.getText().toString());
        Common.userDetailsRef.child(id).child("userName").setValue(userName.getText().toString());
        Common.userDetailsRef.child(id).child("dob").setValue(dob.getText().toString());
        Common.userDetailsRef.child(id).child("gender").setValue(gender.getText().toString());
    }
}