package com.example.alien.recyclerviewitemanimator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

public class SampleItemAnimator extends DefaultItemAnimator {

    @Override
    public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
        return true;
    }

    @NonNull
    @Override
    public ItemHolderInfo recordPreLayoutInformation(@NonNull RecyclerView.State state,
                                                     @NonNull RecyclerView.ViewHolder viewHolder,
                                                     int changeFlags,
                                                     @NonNull List<Object> payloads) {
        TextInfo textInfo = new TextInfo();
        textInfo.setFrom(viewHolder);
        return textInfo;
    }

    @NonNull
    @Override
    public ItemHolderInfo recordPostLayoutInformation(@NonNull RecyclerView.State state,
                                                      @NonNull RecyclerView.ViewHolder viewHolder) {
        TextInfo textInfo = new TextInfo();
        textInfo.setFrom(viewHolder);
        return textInfo;
    }

    @Override
    public boolean animateChange(@NonNull RecyclerView.ViewHolder oldHolder,
                                 @NonNull final RecyclerView.ViewHolder newHolder,
                                 @NonNull ItemHolderInfo preInfo,
                                 @NonNull ItemHolderInfo postInfo) {
        final SampleAdapter.SampleViewHolder holder = (SampleAdapter.SampleViewHolder) newHolder;
        final TextInfo preColorTextInfo = (TextInfo) preInfo;
        final TextInfo postColorTextInfo = (TextInfo) postInfo;

        ObjectAnimator fadeToBlack = ObjectAnimator.ofArgb(holder.mTextView, "backgroundColor", Color.TRANSPARENT, Color.GRAY);
        ObjectAnimator fadeFromBlack = ObjectAnimator.ofArgb(holder.mTextView, "backgroundColor", Color.GRAY, Color.TRANSPARENT);
        AnimatorSet bgAnim = new AnimatorSet();
        bgAnim.playSequentially(fadeToBlack, fadeFromBlack);

        ObjectAnimator oldTextRotate = ObjectAnimator.ofFloat(holder.mTextView, View.ROTATION_X, 0, 90);
        ObjectAnimator newTextRotate = ObjectAnimator.ofFloat(holder.mTextView, View.ROTATION_X, -90, 0);
        oldTextRotate.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                holder.mTextView.setText(preColorTextInfo.text);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                holder.mTextView.setText(postColorTextInfo.text);
            }
        });
        AnimatorSet textAnim = new AnimatorSet();
        textAnim.playSequentially(oldTextRotate, newTextRotate);

        AnimatorSet overallAnim = new AnimatorSet();
        overallAnim.playTogether(bgAnim, textAnim);
        overallAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                dispatchAnimationFinished(newHolder);
            }
        });

        overallAnim.start();

        return super.animateChange(oldHolder, newHolder, preInfo, postInfo);
    }

    private class TextInfo extends ItemHolderInfo {

        private String text;

        @Override
        public ItemHolderInfo setFrom(RecyclerView.ViewHolder holder) {
            if (holder instanceof SampleAdapter.SampleViewHolder) {
                SampleAdapter.SampleViewHolder viewHolder = (SampleAdapter.SampleViewHolder) holder;
                text = (String) viewHolder.mTextView.getText();
            }
            return super.setFrom(holder);
        }
    }
}
