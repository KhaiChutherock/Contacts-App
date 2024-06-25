package com.example.contactapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_Recent_Calls extends RecyclerView.Adapter<Adapter_Recent_Calls.ViewHolder> {
    private final ArrayList<ModelCallRecent> recentCallsList;
    private final Context context;

    public Adapter_Recent_Calls(Context context, ArrayList<ModelCallRecent> recentCallsList) {
        this.context = context;
        this.recentCallsList = recentCallsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recents_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelCallRecent callRecent = recentCallsList.get(position);

        // Thay đổi hình ảnh biểu tượng cuộc gọi dựa trên loại cuộc gọi
        switch (callRecent.getCallType()) {
            case 1:
                holder.imageViewCallType.setImageResource(R.drawable.baseline_call_received_24);
                break;
            case 2:
                holder.imageViewCallType.setImageResource(R.drawable.baseline_call_made_24);
                break;
            case 3:
                holder.imageViewCallType.setImageResource(R.drawable.baseline_call_missed_24);
                break;
        }

        holder.textViewCallerId.setText(callRecent.getCallerId());
    }

    @Override
    public int getItemCount() {
        return recentCallsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageViewCallType;
        TextView textViewCallerId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewCallType = itemView.findViewById(R.id.calltype_image);
            textViewCallerId = itemView.findViewById(R.id.textViewCallerId);
        }
    }
}
