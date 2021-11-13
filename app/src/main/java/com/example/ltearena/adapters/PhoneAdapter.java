package com.example.ltearena.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ltearena.R;
import com.example.ltearena.models.PhoneModel;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.ListViewHolder> {
    private List<PhoneModel> dataList;
    private OnItemClickListener mListener;
    private Context mContext;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public PhoneAdapter(Context mContext, List<PhoneModel> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.rv_phone, parent, false);
        return new ListViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        holder.tv_title.setText(dataList.get(position).getPhoneName());
        Picasso.get()
                .load(dataList.get(position).getImage())
                .placeholder(R.drawable.ic_baseline_phone_android_24)
                .error(R.drawable.ic_baseline_phone_android_24)
                .into(holder.img_phone);
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        private MaterialCardView rv_layout;
        private TextView tv_title;
        private ImageView img_phone;

        public ListViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_phone_recycler);
            img_phone = itemView.findViewById(R.id.img_phone_recycler);
            rv_layout = itemView.findViewById(R.id.card_phone_recycler);

            rv_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
