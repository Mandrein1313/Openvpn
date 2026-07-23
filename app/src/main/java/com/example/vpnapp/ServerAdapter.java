package com.example.vpnapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ServerAdapter extends RecyclerView.Adapter<ServerAdapter.ViewHolder> {

    private List<ServerItem> serverList;
    private int selectedPosition = 0;
    private OnServerSelectListener listener;

    public interface OnServerSelectListener {
        void onServerSelected(ServerItem server);
    }

    public ServerAdapter(List<ServerItem> serverList, OnServerSelectListener listener) {
        this.serverList = serverList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_server, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServerItem server = serverList.get(position);
        
        holder.tvName.setText(server.getName());
        holder.tvStatus.setText("ออนไลน์: " + server.getOnline() + " / จำกัด: " + server.getMax());
        holder.tvPercentage.setText(server.getPercentage() + "%");
        holder.imgFlag.setImageResource(server.getFlagResId());

        holder.radioButton.setChecked(position == selectedPosition);

        holder.itemView.setOnClickListener(v -> {
            selectedPosition = position;
            notifyDataSetChanged();
            if (listener != null) listener.onServerSelected(server);
        });
    }

    @Override
    public int getItemCount() {
        return serverList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFlag;
        TextView tvName, tvStatus, tvPercentage;
        RadioButton radioButton;

        ViewHolder(View itemView) {
            super(itemView);
            imgFlag = itemView.findViewById(R.id.imgFlag);
            tvName = itemView.findViewById(R.id.tvServerName);
            tvStatus = itemView.findViewById(R.id.tvServerStatus);
            tvPercentage = itemView.findViewById(R.id.tvPercentage);
            radioButton = itemView.findViewById(R.id.radioButton);
        }
    }
}