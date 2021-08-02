package com.aariyan.loan_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aariyan.loan_user.Adapter.TransactionAdapter;
import com.aariyan.loan_user.Common.Common;
import com.aariyan.loan_user.Model.TransactionModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class TransactionDetailsActivity extends AppCompatActivity {

    private ImageView backBtn;

    private TextView transactionAmount, transactionTenure;
    private TextView transactionRecordDate, transactionMonth;
    private TextView transactionInterest,fineOrWithoutFine;

    private TextView transactionName;
    private RadioButton memberBtn, nonMemberBtn;

    private String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);

        if (getIntent() != null) {
            id = getIntent().getStringExtra("id");
        }

        initUI();

        loadData();
    }

    private void loadData() {

        Common.transactionRef.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                        TransactionModel model = snapshot.getValue(TransactionModel.class);
                        transactionInterest.setText(model.getTransactionInterest());

                        String memberString = model.getMemberType();
                        boolean isFound = memberString.indexOf("MEMB") !=-1? true: false;

                        if (model.getMemberType().equals("Member")) {
                            memberBtn.setChecked(true);
                            nonMemberBtn.setChecked(false);
                        }
                        if (model.getMemberType().equals("Non - Member")) {
                            memberBtn.setChecked(false);
                            nonMemberBtn.setChecked(true);
                        }
                        transactionName.setText(model.getTransactionName());
                        transactionRecordDate.setText(model.getTransactionRecordDate());
                        transactionAmount.setText(model.getTransactionAmount());
                        transactionMonth.setText(model.getTransactionMonth());
                        fineOrWithoutFine.setText(model.getTransactionFine());

                    }


                } else {
                    Toast.makeText(TransactionDetailsActivity.this, "No transaction found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    private void initUI() {

        transactionInterest = findViewById(R.id.transactionInterestRate);
        transactionTenure = findViewById(R.id.transactionTenure);

        memberBtn = findViewById(R.id.memberRadioBtn);
        nonMemberBtn = findViewById(R.id.nonMemberRadioBtn);

        transactionName = findViewById(R.id.transactionName);
        transactionRecordDate = findViewById(R.id.transactionRecordDate);
        transactionAmount = findViewById(R.id.transactionAmount);
        transactionMonth = findViewById(R.id.transactionMonth);
        fineOrWithoutFine = findViewById(R.id.fineOrWithoutFine);
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });
    }
}