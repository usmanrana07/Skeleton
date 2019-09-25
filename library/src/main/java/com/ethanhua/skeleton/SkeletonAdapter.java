package com.ethanhua.skeleton;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;

/**
 * Created by ethanhua on 2017/7/29.
 */

public class SkeletonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int mItemCount;
    private int mLayoutReference;
    private int[] mLayoutArrayReferences;
    private boolean mShimmerAnimate;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (doesArrayOfLayoutsExist()) {
            mLayoutReference = viewType;
        }
        if (mShimmerAnimate) {
            return new ShimmerViewHolder(inflater, parent, mLayoutReference);
        }

        return new RecyclerView.ViewHolder(inflater.inflate(mLayoutReference, parent, false)) {
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mShimmerAnimate) {
            ShimmerFrameLayout layout = (ShimmerFrameLayout) holder.itemView;

            layout.setShimmer(mShimmer);
            layout.startShimmer();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(doesArrayOfLayoutsExist()) {
            return getCorrectLayoutItem(position);
        }
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    public void setLayoutReference(int layoutReference) {
        this.mLayoutReference = layoutReference;
    }

    public void setArrayOfLayoutReferences(int[] layoutReferences) {
        this.mLayoutArrayReferences = layoutReferences;
    }

    public void setItemCount(int itemCount) {
        this.mItemCount = itemCount;
    }

    private Shimmer mShimmer;
    public void setShimmer(Shimmer shimmer) {
        this.mShimmer = shimmer;
    }

    public void shimmerAnimate(boolean shimmer) {
        this.mShimmerAnimate = shimmer;
    }


    public int getCorrectLayoutItem(int position) {
        if(doesArrayOfLayoutsExist()) {
            return mLayoutArrayReferences[position % mLayoutArrayReferences.length];
        }
        return mLayoutReference;
    }

    private boolean doesArrayOfLayoutsExist() {
        return mLayoutArrayReferences != null && mLayoutArrayReferences.length != 0;
    }
}
