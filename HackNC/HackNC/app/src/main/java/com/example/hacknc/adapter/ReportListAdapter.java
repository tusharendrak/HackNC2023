package com.example.hacknc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hacknc.R;
import com.example.hacknc.model.MPothole;

import java.util.List;

public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.ViewHolder> {
    private Context context;
    private List<MPothole> potholeReportList;
    private OnItemClickListener mListener;
    private double lat,longi;

    public ReportListAdapter(Context context, List<MPothole> potholeReportList) {
        this.context = context;
        this.potholeReportList = potholeReportList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_report,parent,false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MPothole potholeCurrent = potholeReportList.get(position);
        holder.tv_description.setText(potholeCurrent.getDescription());
        holder.tv_time.setText(potholeCurrent.getDate());
        holder.tv_location.setText(potholeCurrent.getAddressLine1());
        Glide.with(context).load(potholeCurrent.getImageUrl()).centerCrop().placeholder(R.mipmap.ic_launcher_round).into(holder.iv_img);
        holder.card_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(holder.getAdapterPosition(),potholeCurrent.getLattitude(),potholeCurrent.getLongitude(),potholeCurrent.getAddressLine1());

            }
        });

    }

/*
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position) {
        final MPothole potholeCurrent = potholeReportList.get(position);
        holder.tv_description.setText(potholeCurrent.getDescription());
        holder.tv_time.setText(potholeCurrent.getDate());
        holder.tv_location.setText(potholeCurrent.getAddressLine1());
        Glide.with(context).load(potholeCurrent.getImageUrl()).centerCrop().placeholder(R.mipmap.ic_launcher_round).into(holder.iv_img);
        holder.card_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(position,potholeCurrent.getLattitude(),potholeCurrent.getLongitude(),potholeCurrent.getAddressLine1());
                    }
                }
            }
        });


    }*/

    @Override
    public int getItemCount() {
        return potholeReportList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView tv_description,tv_location,tv_time;
        CardView card_report;
        ImageView iv_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            tv_description = itemView.findViewById(R.id.tv_description);
            tv_location = itemView.findViewById(R.id.tv_location);
            tv_time = itemView.findViewById(R.id.tv_time);
            iv_img = itemView.findViewById(R.id.iv_image);
            card_report = itemView.findViewById(R.id.card_report);

        }


    }
    public interface OnItemClickListener {
        void onItemClick(int position,double lat,double longi,String name);

        void onWhatEverClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
