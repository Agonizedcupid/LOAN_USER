package com.aariyan.loan_user.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aariyan.loan_user.Common.Common;
import com.aariyan.loan_user.Home;
import com.aariyan.loan_user.R;

public class CalculatorFragment extends Fragment {

    private EditText loanAmount, interestRate, tenure;
    private TextView calculateLoan;
    private TextView loans, emi, totalPayment;

    private CardView cardView;

    private ImageView backBtn;

    public CalculatorFragment() {
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
        Common.root = inflater.inflate(R.layout.fragment_calculator, container, false);
        initUI();
        return Common.root;
    }

    private void initUI() {

        loanAmount = Common.root.findViewById(R.id.loanAmountEditText);
        interestRate = Common.root.findViewById(R.id.interestRateEditText);
        tenure = Common.root.findViewById(R.id.tenureEditTxt);

        calculateLoan = Common.root.findViewById(R.id.calculateLoanTextView);

        loans = Common.root.findViewById(R.id.loanAmountText);
        emi = Common.root.findViewById(R.id.loanEmiText);
        totalPayment = Common.root.findViewById(R.id.totalPaymentText);

        cardView = Common.root.findViewById(R.id.cardView);

        calculateLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                performLoanCalculate();
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

    private void performLoanCalculate() {

        cardView.setVisibility(View.VISIBLE);

        double inputInterest = Double.parseDouble(interestRate.getText().toString());
        double interest = (inputInterest / 100);
        double finalInterest = interest + 1;

        //Ai porjonto thik ase:

        int month = Integer.parseInt(tenure.getText().toString());

        int loan = Integer.parseInt(loanAmount.getText().toString());

        double firstPart = loan * interest;
        double result;


        result = Math.pow(finalInterest, month);

        double numerator = result;
        double denominator = numerator - 1;

        Log.d("Test_Result", "Numerator: " + numerator);

        if (denominator != 0) {
            double EMI_TOTAL = (firstPart * numerator) / denominator;
            double totalPayable = EMI_TOTAL + loan;

            emi.setText("INR "+EMI_TOTAL);
            totalPayment.setText("INR "+totalPayable);
            loans.setText("INR "+loanAmount.getText().toString());
        } else {
            Toast.makeText(getContext(), "Invalid Data!", Toast.LENGTH_SHORT).show();
        }

    }
}