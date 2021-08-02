package com.aariyan.loan_user.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aariyan.loan_user.Adapter.LoanCategoryAdapter;
import com.aariyan.loan_user.Adapter.LoanCategoryAdapter2;
import com.aariyan.loan_user.Common.Common;
import com.aariyan.loan_user.Home;
import com.aariyan.loan_user.Model.CaptionModel;
import com.aariyan.loan_user.Model.ImageModel;
import com.aariyan.loan_user.Model.LoanCategoryModel;
import com.aariyan.loan_user.Model.UserDetailsModel;
import com.aariyan.loan_user.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.aariyan.loan_user.Common.Common.DEFAULT;
import static com.aariyan.loan_user.Common.Common.userId;

public class UserDetailsFragment extends Fragment {

    private FirebaseAuth userAuth;

    private EditText userName, permanentAddress, currentAddress, AadharNumber, panNumber, bankName, branch, IFSCCOde, bacnkAccountNumber;
    private TextView dob, uploadaadharFront, uploadAadharBack, panFront, submitDetails;
    private Spinner genderSpinner;
    private EditText memberId;

    public static TextView loanCategory;

    private String genderSelected = "";
    private String[] genderArray = {"Male", "Female", "Others"};

    List<CaptionModel> listImages = new ArrayList<>();
    public static List<ImageModel> images = new ArrayList<>();
    int countSelected = 0;
    String id = "";

    private SharedPreferences sharedPreferences;
    private String phone;

    private String userID = "";

    //Firebase FireStorage
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private ImageView backBtn;

    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    int day, month, year;

    private TextView aFront,aback,pFront;

    String date = "";
    int k = 0;

    private View bottomSheet;
    public static BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView recyclerView;
    private List<LoanCategoryModel> list = new ArrayList<>();
    private LoanCategoryAdapter2 adapter;

    public UserDetailsFragment() {
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
        Common.root = inflater.inflate(R.layout.fragment_user_details, container, false);
        initUI();
        loadLoanCategory();

        return Common.root;
    }

    private void loadLoanCategory() {

        Common.loanListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    list.clear();
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                        LoanCategoryModel model = snapshot.getValue(LoanCategoryModel.class);
                        list.add(model);
                    }
                    adapter = new LoanCategoryAdapter2(getContext(),list);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    //progressBar.setVisibility(View.GONE);
                } else {
                    //progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                //progressBar.setVisibility(View.GONE);
                Toast.makeText(requireContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initUI() {

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        userAuth = FirebaseAuth.getInstance();

        sharedPreferences = requireActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        phone = sharedPreferences.getString("phone", DEFAULT);

        userName = Common.root.findViewById(R.id.nameInUserDtls);
        permanentAddress = Common.root.findViewById(R.id.permanentAddressInUserDtls);
        currentAddress = Common.root.findViewById(R.id.currentAddressUserDtls);
        AadharNumber = Common.root.findViewById(R.id.aadharNumberInUserDtls);
        panNumber = Common.root.findViewById(R.id.panNumberInUserDtls);
        bankName = Common.root.findViewById(R.id.bankNameInUserDtls);
        branch = Common.root.findViewById(R.id.branchNameInUserDtls);
        IFSCCOde = Common.root.findViewById(R.id.ifscCodeInUserDtls);
        bacnkAccountNumber = Common.root.findViewById(R.id.bankAccountInUserDtls);

        memberId = Common.root.findViewById(R.id.memberId);

        recyclerView = Common.root.findViewById(R.id.recyclerViewInBottomSheet);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        bottomSheet = Common.root.findViewById(R.id.bottomSheets);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        loanCategory = Common.root.findViewById(R.id.loanCategory);
        loanCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        dob = Common.root.findViewById(R.id.dobInUserDtls);
        uploadaadharFront = Common.root.findViewById(R.id.uploadAadharCardFron);
        uploadAadharBack = Common.root.findViewById(R.id.uploadAadharCardBack);
        panFront = Common.root.findViewById(R.id.uploadPanCardFront);
        submitDetails = Common.root.findViewById(R.id.submitDetails);

        aFront = Common.root.findViewById(R.id.afrontText);
        aback = Common.root.findViewById(R.id.aBackText);
        pFront = Common.root.findViewById(R.id.pFrontText);

        uploadaadharFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aFront.setText("Image Selected");
            }
        });

        uploadAadharBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aback.setText("Image Selected");
            }
        });

        panFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pFront.setText("Image Selected");
            }
        });

        genderSpinner = Common.root.findViewById(R.id.genderSpinnerInUserDtls);
        ArrayAdapter<String> genderArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, genderArray);
        genderArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        genderSpinner.setAdapter(genderArrayAdapter);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genderSelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        uploadAadharBack.setOnClickListener(v -> chooseImage());
        uploadaadharFront.setOnClickListener(v -> chooseImage());
        panFront.setOnClickListener(v -> chooseImage());

        submitDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (panNumber.getText().toString().length() != 10) {
                    panNumber.setError("Please input valid pan card umber");
                    panNumber.requestFocus();
                    return;
                }
                if (AadharNumber.getText().toString().length() != 12) {
                    AadharNumber.setError("Please input valid Aadhar number!");
                    AadharNumber.requestFocus();
                    return;
                }

                uploadImage();
            }
        });


        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(requireActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                        int j = i1 + 1;

                        date = i + " - " + j + " - " + i2;
                        dob.setText(date);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                datePickerDialog.show();
            }
        });

        backBtn = Common.root.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Home.class));
            }
        });
    }

    //choose image for new Menu
    private void chooseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        assert data != null;
        if (requestCode == 101 && resultCode == RESULT_OK
                // && data != null && data.getData() != null) {
                && data != null && data.getData() != null) {

            Toast.makeText(requireContext(), "Image Selected", Toast.LENGTH_SHORT).show();

            //int countSelected = Objects.requireNonNull(data.getClipData()).getItemCount();
            countSelected++;
            int start = 0;

            Uri uri = Uri.parse(data.getData().toString());
            File file = new File(uri.getPath());

            Toast.makeText(requireContext(), ""+file.getAbsolutePath(), Toast.LENGTH_SHORT).show();

            listImages.clear();
            while (start < countSelected) {
                listImages.add(new CaptionModel(data.getData()));
                start++;
                Log.d("CHECK_COUNT", start + "   " + countSelected);
            }

            //Toast.makeText(requireContext(), ""+listImages.size(), Toast.LENGTH_SHORT).show();

            //saveUri = data.getData();
        } else {
            Toast.makeText(requireContext(), "Not selected", Toast.LENGTH_SHORT).show();
        }
    }

    //uploading image for new Menu in firestorage
    private void uploadImage() {

        Toast.makeText(requireContext(), "Please wait for a while, we will redirect you!", Toast.LENGTH_SHORT).show();

        id = UUID.randomUUID().toString() + "_ID_" + System.currentTimeMillis();
        images.clear();
        if (listImages != null) {

            for (int i = 0; i < listImages.size(); i++) {

//                final ProgressDialog mDialog = new ProgressDialog(requireContext());
//                mDialog.setMessage("Uploading");
//                mDialog.show();

//here is the change aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
                Uri uri = listImages.get(i).getMultipleImage();
                final String imageName = UUID.randomUUID().toString() + "_N_" + System.currentTimeMillis();
                final StorageReference imageFolder = storageReference.child("images/" + imageName);

                int finalI = i;
                int finalI1 = i;
                int finalI2 = i;
                imageFolder.putFile(uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // mDialog.dismiss();
                                //Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();

                                imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        //set Value for newCategory
                                        //url.add(uri.toString());
                                        String ids = String.valueOf(System.currentTimeMillis());
                                        //images.add(uri.toString());
//                                        images.add(new URL(
//                                                id,
//                                                uri.toString()
//                                        ));

//                                        images.add(new URL(
//                                                "" + finalI,
//                                                uri.toString(),
//                                                ChotoAdapter.list.get(finalI1).getEditTextValue()
//                                        ));

//                                       URL url = new URL(
//                                                "" + finalI,
//                                                uri.toString(),
//                                                ChotoAdapter.list.get(finalI1).getEditTextValue()
//                                        );

                                        ImageModel model = new ImageModel(
                                                UUID.randomUUID().toString(),
                                                uri.toString()
                                        );

                                        images.add(model);

                                        UploadDate(images);
                                        //Common.imageRef.child(id).push().setValue(check);

                                        // ChotoAdapter.list.get(finalI1).getEditTextValue();
                                        //Log.d("CaptionValue: ", "" + ChotoAdapter.list.get(finalI1).getEditTextValue());
                                        //storeOnDatabase(images,url);
                                        //storeOnDatabase();
                                        k++;
                                    }
                                });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // mDialog.dismiss();
                                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        // mDialog.setMessage("Uploaded " + progress + "%");
                    }
                });
            }

//            Toast.makeText(requireContext(), "Called: "+images.size(), Toast.LENGTH_SHORT).show();
//
//            //Will upload the data on Firebase
//            if (userAuth.getCurrentUser() == null) {
//                userID = phone;
//                //Common.userRef.child(phone).child("imageUrl").setValue(uri.toString());
//            } else {
//                userID = userId();
//                //Common.userRef.child(Common.userId()).child("imageUrl").setValue(uri.toString());
//            }
//
//            UserDetailsModel model = new UserDetailsModel(
//                    id,
//                    userID,
//                    userName.getText().toString(),
//                    permanentAddress.getText().toString(),
//                    currentAddress.getText().toString(),
//                    AadharNumber.getText().toString(),
//                    panNumber.getText().toString(),
//                    bankName.getText().toString(),
//                    branch.getText().toString(),
//                    IFSCCOde.getText().toString(),
//                    bacnkAccountNumber.getText().toString(),
//                    dob.getText().toString(),
//                    genderSelected,
//                    images
//            );

//            Common.userDetailsRef.child(userID).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
//                    Toast.makeText(requireContext(), "Submitted", Toast.LENGTH_SHORT).show();
//                    requireActivity().finish();
//                    startActivity(new Intent(requireContext(),Home.class));
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(requireContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });

        }

    }

    private void UploadDate(List<ImageModel> images) {
        if ((images.size()) == k + 1) {
            if (userAuth.getCurrentUser() == null) {
                userID = phone;
                //Common.userRef.child(phone).child("imageUrl").setValue(uri.toString());
            } else {
                userID = userId();
                //Common.userRef.child(Common.userId()).child("imageUrl").setValue(uri.toString());
            }

            UserDetailsModel model = new UserDetailsModel(
                    id,
                    userID,
                    userName.getText().toString(),
                    permanentAddress.getText().toString(),
                    currentAddress.getText().toString(),
                    AadharNumber.getText().toString(),
                    panNumber.getText().toString(),
                    bankName.getText().toString(),
                    branch.getText().toString(),
                    IFSCCOde.getText().toString(),
                    bacnkAccountNumber.getText().toString(),
                    dob.getText().toString(),
                    genderSelected,
                    images,
                    memberId.getText().toString()
            );

            Common.userDetailsRef.child(userID).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    if (getActivity() == null) {
                        return;
                    }
                    Toast.makeText(requireContext(), "Submitted", Toast.LENGTH_SHORT).show();
                    requireActivity().finish();
                    startActivity(new Intent(requireContext(), Home.class));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(requireContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}