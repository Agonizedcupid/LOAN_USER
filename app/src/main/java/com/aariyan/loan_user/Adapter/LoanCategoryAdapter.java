package com.aariyan.loan_user.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.aariyan.loan_user.Common.Common;
import com.aariyan.loan_user.Model.LoanCategoryModel;
import com.aariyan.loan_user.Model.UserDetailsModel;
import com.aariyan.loan_user.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.aariyan.loan_user.Common.Common.DEFAULT;
import static com.aariyan.loan_user.Common.Common.userId;
import static com.aariyan.loan_user.Fragment.ApplyForLoanFragment.bottomSheetBehavior;
import static com.aariyan.loan_user.Fragment.ApplyForLoanFragment.interestRate;
import static com.aariyan.loan_user.Fragment.ApplyForLoanFragment.loanCategoryText;

public class LoanCategoryAdapter extends RecyclerView.Adapter<LoanCategoryAdapter.ViewHolder> {

    private Context context;
    private List<LoanCategoryModel> list;
    SharedPreferences sharedPreferences;
    private String phone,userID;
    private FirebaseAuth userAuth;

    public LoanCategoryAdapter(Context context, List<LoanCategoryModel>list) {
        this.context = context;
        this.list = list;

        sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        phone = sharedPreferences.getString("phone", DEFAULT);
        userAuth = FirebaseAuth.getInstance();

        if (userAuth.getCurrentUser() == null) {
            userID = phone;
            //Common.userRef.child(phone).child("imageUrl").setValue(uri.toString());
        } else {
            userID = userId();
            //Common.userRef.child(Common.userId()).child("imageUrl").setValue(uri.toString());
        }
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_loan_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull LoanCategoryAdapter.ViewHolder holder, int position) {
        LoanCategoryModel model = list.get(position);
        holder.loanName.setText(model.getTypeOfLoan());
        holder.memberPercentage.setText(model.getMemberPercentage()+" %");
        holder.nonMemberPercentage.setText(model.getNonMemberPercentage()+" %");

        holder.expandLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Common.userDetailsRef.orderByChild("userId").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                                UserDetailsModel models = snapshot.getValue(UserDetailsModel.class);
                                CharSequence member = "MEMB";
                                boolean bool = models.getMemberId().contains(member);
                                if (bool) {
                                    interestRate.setText(model.getMemberPercentage() +" %");
                                } else {
                                    interestRate.setText(model.getNonMemberPercentage() +" %");
                                }
                            }

                        } else {
                            Toast.makeText(context, "User Details missing", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

                loanCategoryText.setText(model.getTypeOfLoan());

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView loanName,memberPercentage,nonMemberPercentage;

        private LinearLayout expandLoan;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            loanName = itemView.findViewById(R.id.loanName);
            memberPercentage = itemView.findViewById(R.id.memberPercentage);
            nonMemberPercentage = itemView.findViewById(R.id.nonMemberPercentage);
            expandLoan = itemView.findViewById(R.id.expandLoan);
        }
    }
}
