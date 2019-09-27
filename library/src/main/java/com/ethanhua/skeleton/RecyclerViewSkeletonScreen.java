package com.ethanhua.skeleton;

import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;

import com.facebook.shimmer.Shimmer;

/**
 * Created by ethanhua on 2017/7/29.
 */

public class RecyclerViewSkeletonScreen implements SkeletonScreen {

    private final RecyclerView mRecyclerView;
    private final RecyclerView.Adapter mActualAdapter;
    private final SkeletonAdapter mSkeletonAdapter;
    private final boolean mRecyclerViewFrozen;

    private RecyclerViewSkeletonScreen(Builder builder) {
        mRecyclerView = builder.mRecyclerView;
        mActualAdapter = builder.mActualAdapter;
        mSkeletonAdapter = new SkeletonAdapter();
        mSkeletonAdapter.setItemCount(builder.mItemCount);
        mSkeletonAdapter.setLayoutReference(builder.mItemResID);
        mSkeletonAdapter.setArrayOfLayoutReferences(builder.mItemsResIDArray);
        mSkeletonAdapter.shimmerAnimate(builder.mShimmerAnimate);
        mSkeletonAdapter.setShimmer(builder.generateShimmer());
        mRecyclerViewFrozen = builder.mFrozen;
    }

    @Override
    public void show() {
        mRecyclerView.setAdapter(mSkeletonAdapter);
        if (!mRecyclerView.isComputingLayout() && mRecyclerViewFrozen) {
            mRecyclerView.setLayoutFrozen(true);
        }
    }

    @Override
    public void hide() {
        mRecyclerView.setAdapter(mActualAdapter);
    }

    public static class Builder {
        private final RecyclerView mRecyclerView;
        private RecyclerView.Adapter mActualAdapter;
        private int mItemCount = 10;
        private int mItemResID = R.layout.layout_default_item_skeleton;
        private int[] mItemsResIDArray;
        private boolean mShimmerAnimate = true;
        private boolean mAutoStart;
        private int mShimmerBaseColor;
        private int mShimmerHighlightColor;
        private boolean mShimmerColored;
        private int mDuration = 1000;
        private int mShimmerShape;
        private int mDirection;
        private Shimmer mShimmer;
        private boolean mFrozen = true;

        public Builder(RecyclerView recyclerView) {

            this(recyclerView, null);
        }

        public Builder(RecyclerView recyclerView, Shimmer shimmer) {
            this.mRecyclerView = recyclerView;
            this.mShimmerHighlightColor = ContextCompat.getColor(recyclerView.getContext(), R.color.shimmer_highlight_color);
            this.mShimmerBaseColor = ContextCompat.getColor(recyclerView.getContext(), R.color.shimmer_base_color);
            mShimmerColored = true;
            mShimmer = shimmer;
            mAutoStart = true;
        }

        /**
         * @param adapter the target recyclerView actual adapter
         */
        public Builder adapter(RecyclerView.Adapter adapter) {
            this.mActualAdapter = adapter;
            return this;
        }

        /**
         * @param itemCount the child item count in recyclerView
         */
        public Builder count(int itemCount) {
            this.mItemCount = itemCount;
            return this;
        }

        /**
         * the duration of the animation , the time it will take for the highlight to move from one end of the layout
         * to the other.
         *
         * @param duration Duration of the shimmer animation, in milliseconds
         */
        public Builder duration(int duration) {
            this.mDuration = duration;
            return this;
        }

        /**
         * @param shimmerColor the shimmer color
         */
        public Builder color(@ColorRes int shimmerColor) {
            this.mShimmerHighlightColor = ContextCompat.getColor(mRecyclerView.getContext(), shimmerColor);
            return this;
        }

        /**
         * @param shimmer whether show shimmer animation
         */
        public Builder shimmerAnimate(boolean shimmer) {
            this.mShimmerAnimate = shimmer;
            return this;
        }

        public Builder shape(@Shimmer.Shape int shape) {
            this.mShimmerShape = shape;
            return this;
        }

        public Builder direction(@Shimmer.Direction int direction) {
            this.mDirection = direction;
            return this;
        }

        public Builder autoStart(boolean autoStart) {
            this.mAutoStart = autoStart;
            return this;
        }

        public Builder shimmerColored(boolean shimmerColored) {
            this.mShimmerColored = shimmerColored;
            return this;
        }

        public Builder shimmerBaseColor(@ColorRes int shimmerBaseColor) {
            this.mShimmerBaseColor = ContextCompat.getColor(mRecyclerView.getContext(), shimmerBaseColor);
            return this;
        }

        public Shimmer generateShimmer() {
            if (mShimmer == null) {
                Shimmer.Builder shimmerBuilder;
                if (mShimmerColored) {
                    shimmerBuilder = new Shimmer.ColorHighlightBuilder()
                            .setBaseColor(mShimmerBaseColor)
                            .setHighlightColor(mShimmerHighlightColor);
                } else {
                    shimmerBuilder = new Shimmer.AlphaHighlightBuilder();
                }
                shimmerBuilder.setAutoStart(mAutoStart);
                shimmerBuilder.setShape(mShimmerShape);
                shimmerBuilder.setDuration(mDuration);
                shimmerBuilder.setDirection(mDirection);


                mShimmer = shimmerBuilder.build();
            }

            return mShimmer;
        }
        //


        /**
         * @param skeletonLayoutResID the loading skeleton layoutResID
         */
        public Builder load(@LayoutRes int skeletonLayoutResID) {
            this.mItemResID = skeletonLayoutResID;
            return this;
        }

        /**
         * @param skeletonLayoutResIDs the loading array of skeleton layoutResID
         */
        public Builder loadArrayOfLayouts(@ArrayRes int[] skeletonLayoutResIDs) {
            this.mItemsResIDArray = skeletonLayoutResIDs;
            return this;
        }

        /**
         * @param frozen whether frozen recyclerView during skeleton showing
         * @return
         */
        public Builder frozen(boolean frozen) {
            this.mFrozen = frozen;
            return this;
        }

        public RecyclerViewSkeletonScreen show() {
            RecyclerViewSkeletonScreen recyclerViewSkeleton = new RecyclerViewSkeletonScreen(this);
            recyclerViewSkeleton.show();
            return recyclerViewSkeleton;
        }
    }
}
