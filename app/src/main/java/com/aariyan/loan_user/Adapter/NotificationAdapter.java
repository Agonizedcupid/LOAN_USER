package com.aariyan.loan_user.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aariyan.loan_user.Model.NotificationModel;
import com.aariyan.loan_user.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context context;
    private List<NotificationModel> list;

    public NotificationAdapter(Context context,List<NotificationModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_notification_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NotificationAdapter.ViewHolder holder, int position) {
        NotificationModel model = list.get(position);

        holder.notificationDate.setText(model.getDate());
        holder.notificationTitle.setText(model.getTitle());
        holder.notificationTime.setText(model.getTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView notificationTitle,notificationDate,notificationTime;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            notificationTitle = itemView.findViewById(R.id.notificationTitle);
            notificationDate = itemView.findViewById(R.id.notificationDate);
            notificationTime = itemView.findViewById(R.id.notificationTime);
        }
    }
}
