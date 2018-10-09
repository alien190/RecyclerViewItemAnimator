package com.example.alien.recyclerviewitemanimator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MainActivity extends AppCompatActivity implements SampleAdapter.Callback {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.ItemAnimator mItemAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SampleAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mItemAnimator = new SampleItemAnimator();
        mRecyclerView.setItemAnimator(mItemAnimator);
    }

    @Override
    public void onItemClick(View view) {
        int itemPosition = mRecyclerView.getChildAdapterPosition(view);
        if (itemPosition != RecyclerView.NO_POSITION) {
            ((SampleAdapter) mAdapter).changeItemNumber(itemPosition);
        }
    }
}