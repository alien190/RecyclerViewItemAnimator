package com.example.alien.recyclerviewitemanimator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.support.animation.DynamicAnimation;
import android.support.animation.FlingAnimation;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

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


        int colorPrimary = oldHolder.itemView.getContext().getResources().getColor(R.color.colorPrimary);
        AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator(0.8f);
        DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator(0.8f);

        ObjectAnimator fadeToBlack = ObjectAnimator.ofArgb(holder.mTextView,
                "textColor", colorPrimary, Color.TRANSPARENT);
        fadeToBlack.setInterpolator(accelerateInterpolator);
        fadeToBlack.setDuration(1500);
        ObjectAnimator fadeFromBlack = ObjectAnimator.ofArgb(holder.mTextView,
                "textColor", Color.TRANSPARENT, colorPrimary);
        fadeFromBlack.setInterpolator(decelerateInterpolator);
        fadeFromBlack.setDuration(1500);
        AnimatorSet bgAnim = new AnimatorSet();
        bgAnim.playSequentially(fadeToBlack, fadeFromBlack);

        int width = oldHolder.itemView.getWidth();

        ObjectAnimator oldTextMove = ObjectAnimator.ofFloat(holder.mTextView, View.X, 0, width);
        oldTextMove.setInterpolator(accelerateInterpolator);
        oldTextMove.setDuration(1500);
        ObjectAnimator newTextMove = ObjectAnimator.ofFloat(holder.mTextView, View.X, -width, 0);
        newTextMove.setInterpolator(decelerateInterpolator);
        newTextMove.setDuration(1500);
        oldTextMove.addListener(new AnimatorListenerAdapter() {
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
        textAnim.playSequentially(oldTextMove, newTextMove);

        AnimatorSet overallAnim = new AnimatorSet();
        overallAnim.playTogether(bgAnim, textAnim);
        //overallAnim.playTogether(textAnim);
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
