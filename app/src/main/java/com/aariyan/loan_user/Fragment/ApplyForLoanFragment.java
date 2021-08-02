package com.aariyan.loan_user.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aariyan.loan_user.Adapter.LoanCategoryAdapter;
import com.aariyan.loan_user.Common.Common;
import com.aariyan.loan_user.Home;
import com.aariyan.loan_user.Interface.APISerrvice;
import com.aariyan.loan_user.Model.LoanCategoryModel;
import com.aariyan.loan_user.Model.LoanModel;
import com.aariyan.loan_user.Model.NotificationModel;
import com.aariyan.loan_user.Notification.Client;
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

import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.LongAccumulator;

import static android.app.Activity.RESULT_OK;
import static com.aariyan.loan_user.Common.Common.DEFAULT;
import static com.aariyan.loan_user.Common.Common.adminNotificationRef;
import static com.aariyan.loan_user.Common.Common.getCurrentDate;
import static com.aariyan.loan_user.Common.Common.getCurrentTime;
import static com.aariyan.loan_user.Common.Common.userId;


public class ApplyForLoanFragment extends Fragment {


    private ImageView backBtn;

    private EditText userName, permanentAddress, aadharNumber, panNumber;
    public static TextView interestRate, loanCategoryText;
    private TextView dob, uploadText, uploadBtn, submitBtn;
    private Spinner genderSpinner, uploadProofSpinner;
    private EditText tenure,loanAmount;


    private String[] genderArray = {"Male", "Female", "Others"};
    private String[] uploadProofSpinnerArray = {"Pan", "Aadhar"};

    ArrayAdapter<String> genderAdapter, uploadProofAdapter;

    String genderSelected = "", proofSelected = "";


    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    int day, month, year;

    String date = "";

    private Uri saveUri;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private FirebaseAuth userAuth;
    private String userID = "";

    SharedPreferences sharedPreferences;
    String phone;
    private View bottomSheet;
    public static BottomSheetBehavior bottomSheetBehavior;

    private RecyclerView recyclerView;
    private LoanCategoryAdapter adapter;

    private List<LoanCategoryModel> list = new ArrayList<>();

    //Notification Part:
    APISerrvice apiSerrvice;
    private static String whatTypes = "";

    private String invitationMessage = "Your loan approved!";
    private String receiverId;
    public static boolean notify = false;

    private String notificationStatus = "";

    private ScrollView scrollView;

    public ApplyForLoanFragment() {
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
        Common.root = inflater.inflate(R.layout.fragment_apply_for_loan, container, false);

        userAuth = FirebaseAuth.getInstance();
        apiSerrvice = Client.getClient("https://fcm.googleapis.com/").create(APISerrvice.class);

        sharedPreferences = requireActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        phone = sharedPreferences.getString("phone", DEFAULT);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        initUI();

        checkUserDetails();

        loadLoanCategory();
        return Common.root;
    }

    private void checkUserDetails() {

        if (userAuth.getCurrentUser() == null) {
            userID = phone;
            //Common.userRef.child(phone).child("imageUrl").setValue(uri.toString());
        } else {
            userID = userId();
            //Common.userRef.child(Common.userId()).child("imageUrl").setValue(uri.toString());
        }

        Common.userDetailsRef.orderByChild("userId").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    scrollView.setVisibility(View.VISIBLE);
                } else {
                    scrollView.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Please first fill the User Details!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                scrollView.setVisibility(View.GONE);
            }
        });
    }

    private void loadLoanCategory() {

        Common.loanListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    list.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        LoanCategoryModel model = snapshot.getValue(LoanCategoryModel.class);
                        list.add(model);
                    }
                    adapter = new LoanCategoryAdapter(getContext(), list);
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
                Toast.makeText(requireContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initUI() {
        userName = Common.root.findViewById(R.id.nameInLoan);
        permanentAddress = Common.root.findViewById(R.id.permanentAddressInLoan);
        aadharNumber = Common.root.findViewById(R.id.aadharNumberInLoan);
        panNumber = Common.root.findViewById(R.id.panNumberInLoan);
        interestRate = Common.root.findViewById(R.id.interestRateInLoan);

        loanCategoryText = Common.root.findViewById(R.id.loanCategoryText);

        tenure = Common.root.findViewById(R.id.tenureInApplyForLoan);
        loanAmount = Common.root.findViewById(R.id.loanAmountInApplyForLoan);

        scrollView = Common.root.findViewById(R.id.scrollView);

        recyclerView = Common.root.findViewById(R.id.recyclerViewInBottomSheet);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        bottomSheet = Common.root.findViewById(R.id.bottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        loanCategoryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        dob = Common.root.findViewById(R.id.dobInLoan);
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDOB();
            }
        });
        uploadText = Common.root.findViewById(R.id.uploadTextInLoan);
        uploadBtn = Common.root.findViewById(R.id.uploadButtonInLoan);
        submitBtn = Common.root.findViewById(R.id.submitApplyForLon);


        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        genderSpinner = Common.root.findViewById(R.id.genderInLoan);
        uploadProofSpinner = Common.root.findViewById(R.id.uploadProofSpinnerInLoan);
        //loanCategorySpinner = Common.root.findViewById(R.id.loanCategorySpinnerInLoan);

//        genderSpinner.setAdapter(genderAdapter);
//        uploadProofSpinner.setAdapter(uploadProofAdapter);

// Application of the Array to the Spinner
        ArrayAdapter<String> genderArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, genderArray);
        genderArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        genderSpinner.setAdapter(genderArrayAdapter);


        ArrayAdapter<String> proofArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, uploadProofSpinnerArray);
        proofArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        uploadProofSpinner.setAdapter(proofArrayAdapter);

        uploadProofSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                proofSelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genderSelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            saveUri = data.getData();
            uploadText.setText("Image selected");
            Toast.makeText(getContext(), "Image selected", Toast.LENGTH_SHORT).show();

        }
    }

    //choose image for new Menu
    private void chooseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 101);
    }

    public void uploadData() {
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
                                        userID = phone;
                                        //Common.userRef.child(phone).child("imageUrl").setValue(uri.toString());
                                    } else {
                                        userID = userId();
                                        //Common.userRef.child(Common.userId()).child("imageUrl").setValue(uri.toString());
                                    }

                                    String id = UUID.randomUUID().toString();
                                    LoanModel model = new LoanModel(
                                            id,
                                            userID,
                                            userName.getText().toString(),
                                            dob.getText().toString(),
                                            genderSelected,
                                            permanentAddress.getText().toString(),
                                            aadharNumber.getText().toString(),
                                            panNumber.getText().toString(),
                                            proofSelected,
                                            uri.toString(),
                                            "",
                                            "",
                                            "no","",""
                                    );

                                    Common.loanRef.child(userID).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show();
                                            String notificationId = UUID.randomUUID().toString();
                                            NotificationModel model = new NotificationModel(
                                                    notificationId,
                                                    id,
                                                    getCurrentDate(),
                                                    getCurrentTime(),
                                                    userID,
                                                    "",
                                                    "Applied for loan",
                                                    "no"
                                            );

                                            adminNotificationRef.child(notificationId).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    requireActivity().finish();
                                                    startActivity(new Intent(requireActivity(), Home.class));
                                                }
                                            });


                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(requireContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    //loadData();
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
        } else {

            if (userAuth.getCurrentUser() == null) {
                userID = phone;
                //Common.userRef.child(phone).child("imageUrl").setValue(uri.toString());
            } else {
                userID = userId();
                //Common.userRef.child(Common.userId()).child("imageUrl").setValue(uri.toString());
            }

            String id = UUID.randomUUID().toString();
            LoanModel model = new LoanModel(
                    id,
                    userID,
                    userName.getText().toString(),
                    dob.getText().toString(),
                    genderSelected,
                    permanentAddress.getText().toString(),
                    aadharNumber.getText().toString(),
                    panNumber.getText().toString(),
                    proofSelected,
                    "",
                    loanCategoryText.getText().toString(),
                    interestRate.getText().toString(),
                    "no",
                    loanAmount.getText().toString(),
                    tenure.getText().toString()
            );

            Common.loanRef.child(userID).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show();
                    String notificationId = UUID.randomUUID().toString();
                    NotificationModel model = new NotificationModel(
                            notificationId,
                            id,
                            getCurrentDate(),
                            getCurrentTime(),
                            userID,
                            "",
                            "Applied for loan",
                            "no"
                    );

                    adminNotificationRef.child(notificationId).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            requireActivity().finish();
                            startActivity(new Intent(requireActivity(), Home.class));
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(requireContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            //Toast.makeText(requireContext(), "Please select your proof of identity!", Toast.LENGTH_SHORT).show();
        }
    }


    private void selectDOB() {
        datePickerDialog = new DatePickerDialog(requireActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                int j = i1 + 1;

                date = i + " - " + j + " - " + i2;
                dob.setText(date);
            }
        }, day, month, year);

        //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        datePickerDialog.show();
    }

}