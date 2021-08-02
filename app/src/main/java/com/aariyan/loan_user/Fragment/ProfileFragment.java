package com.aariyan.loan_user.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aariyan.loan_user.Common.Common;
import com.aariyan.loan_user.Home;
import com.aariyan.loan_user.LogIn;
import com.aariyan.loan_user.Model.UserModel;
import com.aariyan.loan_user.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.aariyan.loan_user.Common.Common.DEFAULT;
import static com.aariyan.loan_user.Common.Common.userRef;


public class ProfileFragment extends Fragment {


    private ImageView backBtn,addImage,singOutBtn;
    private CircleImageView profileImage;
    private EditText userName,userEmail;
    private EditText userPhone;
    private TextView saveBtn;
    //Firebase FireStorage
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private Uri saveUri;
    private FirebaseAuth userAuth;
    private SharedPreferences sharedPreferences;

    private static String phone,email;
    SharedPreferences.Editor editor;


    public ProfileFragment() {
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
        Common.root =  inflater.inflate(R.layout.fragment_profile, container, false);
        initUI();

        return Common.root;
    }

    private void initUI() {

        userAuth = FirebaseAuth.getInstance();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        backBtn = Common.root.findViewById(R.id.backBtn);

        profileImage = Common.root.findViewById(R.id.profileImage);
        userName = Common.root.findViewById(R.id.userNameInProfile);
        userEmail = Common.root.findViewById(R.id.emailIdInProfile);
        userPhone = Common.root.findViewById(R.id.phoneNumberIdInProfile);
        saveBtn = Common.root.findViewById(R.id.saveProfileInformationButton);
        addImage = Common.root.findViewById(R.id.addImage);
        singOutBtn = Common.root.findViewById(R.id.signOut);

        sharedPreferences = requireActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        phone = sharedPreferences.getString("phone",DEFAULT);
        email = sharedPreferences.getString("email",DEFAULT);

        backBtn.setOnClickListener(v -> startActivity(new Intent(getActivity(), Home.class)));


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserData();
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        singOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //userAuth.signOut();
                if (userAuth.getCurrentUser() != null) {
                    userAuth.signOut();
                    startActivity(new Intent(requireActivity(), LogIn.class));
                    requireActivity().finish();
                } else {

                    editor.putString("active", "off");
                    editor.commit();

                    startActivity(new Intent(requireActivity(), LogIn.class));
                    requireActivity().finish();
                }


            }
        });

        //set the user value

        loadData();
    }

    private void loadData() {

        if (userAuth.getCurrentUser() == null) {

            Common.userRef.orderByChild("phoneNumber").equalTo(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot :dataSnapshot.getChildren()) {
                            UserModel model = snapshot.getValue(UserModel.class);
                            assert model != null;
                            userEmail.setText(model.getEmailId(), TextView.BufferType.EDITABLE);
                            userName.setText(model.getUserName(), TextView.BufferType.EDITABLE);
                            userPhone.setText(model.getPhoneNumber());

                            if (model.getImageUrl().equals("null")) {
                                profileImage.setImageResource(R.drawable.profile);
                            } else {
                                Glide.with(requireActivity()).load(model.getImageUrl()).error(R.drawable.profile).into(profileImage);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else if (userAuth.getCurrentUser() != null) {
            userRef.orderByChild("userId").equalTo(userAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                            UserModel model = dataSnapshot.getValue(UserModel.class);
                            assert model != null;
                            userEmail.setText(model.getEmailId(), TextView.BufferType.EDITABLE);
                            userName.setText(model.getUserName(), TextView.BufferType.EDITABLE);
                            userPhone.setText(model.getPhoneNumber());

                            if (model.getImageUrl().equals("null")) {
                                profileImage.setImageResource(R.drawable.profile);
                            } else {
                                Glide.with(requireActivity()).load(model.getImageUrl()).error(R.drawable.profile).into(profileImage);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }




//        Common.userRef.orderByChild("userId").equalTo(Common.userId()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    for (DataSnapshot snapshot :dataSnapshot.getChildren()) {
//                        UserModel model = snapshot.getValue(UserModel.class);
//                        assert model != null;
//                        userEmail.setText(model.getEmailId(), TextView.BufferType.EDITABLE);
//                        userName.setText(model.getUserName(), TextView.BufferType.EDITABLE);
//                        userPhone.setText(model.getPhoneNumber(), TextView.BufferType.EDITABLE);
//
//                        if (model.getImageUrl().equals("null")) {
//                            profileImage.setImageResource(R.drawable.profile);
//                        } else {
//                            Glide.with(requireActivity()).load(model.getImageUrl()).error(R.drawable.profile).into(profileImage);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }

    //choose image for new Menu
    private void chooseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            saveUri = data.getData();
            Toast.makeText(getContext(), "Image selected", Toast.LENGTH_SHORT).show();

        }
    }


    private void updateUserData() {
//        Common.userRef.child(Common.userId()).child("emailId").setValue(userEmail.getText().toString());
//        Common.userRef.child(Common.userId()).child("phoneNumber").setValue(userPhone.getText().toString());
//        Common.userRef.child(Common.userId()).child("userName").setValue(userName.getText().toString());


        if (userAuth.getCurrentUser() == null) {
            Common.userRef.child(phone).child("emailId").setValue(userEmail.getText().toString());
            //Common.userRef.child(phone).child("phoneNumber").setValue(userPhone.getText().toString());
            Common.userRef.child(phone).child("userName").setValue(userName.getText().toString());
            //Common.userRef.child(phone).child("userName").setValue(userName.getText().toString());
            //Common.userRef.child(phone).child("phoneNumber").setValue(userPhone.getText().toString());
        } else {
            Common.userRef.child(Common.userId()).child("emailId").setValue(userEmail.getText().toString());
            Common.userRef.child(Common.userId()).child("phoneNumber").setValue(userPhone.getText().toString());
            Common.userRef.child(Common.userId()).child("userName").setValue(userName.getText().toString());
           // Common.userRef.child(Common.userId()).child("phoneNumber").setValue(userPhone.getText().toString());
        }



        if (saveUri != null) {
            final ProgressDialog mDialog = new ProgressDialog(getContext());
            mDialog.setMessage("Uploading");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/" + imageName);

            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(requireContext(), "Uploaded", Toast.LENGTH_SHORT).show();

                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //set Value for newCategory
                                    //Common.userRef.child(Common.userId()).child("imageUrl").setValue(uri.toString());

                                    if (userAuth.getCurrentUser() == null) {
                                        Common.userRef.child(phone).child("imageUrl").setValue(uri.toString());
                                    } else {
                                        Common.userRef.child(Common.userId()).child("imageUrl").setValue(uri.toString());
                                    }

                                    loadData();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(requireContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    mDialog.setMessage("Uploaded " + progress + "%");
                }
            });
        }

        loadData();
    }
}