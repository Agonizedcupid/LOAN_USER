package com.aariyan.loan_user.Fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aariyan.loan_user.Adapter.TransactionAdapter;
import com.aariyan.loan_user.Common.Common;
import com.aariyan.loan_user.Home;
import com.aariyan.loan_user.Model.TransactionModel;
import com.aariyan.loan_user.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static com.aariyan.loan_user.Common.Common.DEFAULT;

public class TransactionFragment extends Fragment {

    private ImageView backBtn;

//    private EditText transactionName, transactionAmount, transactionFine;
//    private TextView submitTransaction, transactionRecordDate;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private RecyclerView recyclerView;
    private TransactionAdapter adapter;

    private FirebaseAuth userAuth;
    private String userId = "";
    private List<TransactionModel>list = new ArrayList<>();

//    private DatePickerDialog datePickerDialog;
//    private Calendar calendar;
//    int day, month, year;
//
//    String date = "";

    public TransactionFragment() {
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
        Common.root = inflater.inflate(R.layout.fragment_transaction, container, false);
        sharedPreferences = requireActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        userAuth = FirebaseAuth.getInstance();


        initUI();

        loadData();

        return Common.root;
    }

    private void loadData() {
        if (userAuth.getCurrentUser() == null) {
            userId = sharedPreferences.getString("phone",DEFAULT);
        } else {
            userId = userAuth.getCurrentUser().getUid();
        }


        Common.transactionRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    list.clear();
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                        TransactionModel model = snapshot.getValue(TransactionModel.class);
                        list.add(model);
                    }
                    adapter = new TransactionAdapter(requireContext(),list);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(requireContext(), "No transaction found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void initUI() {

        recyclerView = Common.root.findViewById(R.id.transactionRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        backBtn = Common.root.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Home.class));
            }
        });
    }

}