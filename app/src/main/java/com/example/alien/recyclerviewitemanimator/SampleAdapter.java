package com.example.alien.recyclerviewitemanimator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.SampleViewHolder> {

    private Callback mCallback;
    private ArrayList<String> mDataset;

    public SampleAdapter(Context context) {
        mDataset = dataset();

        if (context instanceof Callback) {
            mCallback = (Callback) context;
        }
    }

    private ArrayList<String> dataset() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            list.add("Item " + i);
        }

        return list;
    }

    @NonNull
    @Override
    public SampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        TextView textView = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sample_item, parent, false);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onItemClick(v);
            }
        });

        return new SampleViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(@NonNull SampleViewHolder holder, int position) {
        holder.mTextView.setText(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void changeItemNumber(int position) {
        int number = new Random().nextInt(100);
        mDataset.set(position, "Item " + number);
        notifyItemChanged(position);
    }

    static class SampleViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;

        public SampleViewHolder(TextView textView) {
            super(textView);
            mTextView = textView;
        }
    }

    interface Callback {

        void onItemClick(View view);
    }
}