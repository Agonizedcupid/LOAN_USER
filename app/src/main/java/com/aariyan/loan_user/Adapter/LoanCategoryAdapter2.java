package com.aariyan.loan_user.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aariyan.loan_user.Fragment.UserDetailsFragment;
import com.aariyan.loan_user.Model.LoanCategoryModel;
import com.aariyan.loan_user.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.aariyan.loan_user.Fragment.ApplyForLoanFragment.bottomSheetBehavior;
import static com.aariyan.loan_user.Fragment.ApplyForLoanFragment.interestRate;
import static com.aariyan.loan_user.Fragment.ApplyForLoanFragment.loanCategoryText;
import static com.aariyan.loan_user.Fragment.UserDetailsFragment.loanCategory;

public class LoanCategoryAdapter2 extends RecyclerView.Adapter<LoanCategoryAdapter2.ViewHolder> {

    private Context context;
    private List<LoanCategoryModel> list;

    public LoanCategoryAdapter2(Context context, List<LoanCategoryModel>list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_loan_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull LoanCategoryAdapter2.ViewHolder holder, int position) {
        LoanCategoryModel model = list.get(position);
        holder.loanName.setText(model.getTypeOfLoan());
        holder.memberPercentage.setText(model.getMemberPercentage()+" %");
        holder.nonMemberPercentage.setText(model.getNonMemberPercentage()+" %");

        holder.expandLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loanCategory.setText(model.getTypeOfLoan());
                UserDetailsFragment.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
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
