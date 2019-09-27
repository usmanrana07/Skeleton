package com.ethanhua.skeleton;

import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;

/**
 * Created by ethanhua on 2017/7/29.
 */

public class ViewSkeletonScreen implements SkeletonScreen {
    private static final String TAG = ViewSkeletonScreen.class.getName();
    private final ViewReplacer mViewReplacer;
    private final View mActualView;
    private final int mSkeletonResID;
    private final boolean mShimmerAnimate;
    private Shimmer mShimmer;

    private ViewSkeletonScreen(Builder builder) {
        mActualView = builder.mView;
        mSkeletonResID = builder.mSkeletonLayoutResID;
        mShimmerAnimate = builder.mShimmerAnimate;
        mShimmer = builder.generateShimmer();
        mViewReplacer = new ViewReplacer(builder.mView);
    }

    private ShimmerFrameLayout generateShimmerContainerLayout(ViewGroup parentView) {
        final ShimmerFrameLayout shimmerLayout = (ShimmerFrameLayout) LayoutInflater.from(mActualView.getContext()).inflate(R.layout.layout_shimmer, parentView, false);

        if (mShimmer != null) {
            shimmerLayout.setShimmer(mShimmer);
        }
        View innerView = LayoutInflater.from(mActualView.getContext()).inflate(mSkeletonResID, shimmerLayout, false);
        ViewGroup.LayoutParams lp = innerView.getLayoutParams();
        if (lp != null) {
            shimmerLayout.setLayoutParams(lp);
        }
        shimmerLayout.addView(innerView);
        shimmerLayout.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                shimmerLayout.startShimmer();
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                shimmerLayout.stopShimmer();
            }
        });
        shimmerLayout.startShimmer();
        return shimmerLayout;
    }

    private View generateSkeletonLoadingView() {
        ViewParent viewParent = mActualView.getParent();
        if (viewParent == null) {
            Log.e(TAG, "the source view have not attach to any view");
            return null;
        }
        ViewGroup parentView = (ViewGroup) viewParent;
        if (mShimmerAnimate) {
            return generateShimmerContainerLayout(parentView);
        }
        return LayoutInflater.from(mActualView.getContext()).inflate(mSkeletonResID, parentView, false);
    }

    @Override
    public void show() {
        View skeletonLoadingView = generateSkeletonLoadingView();
        if (skeletonLoadingView != null) {
            mViewReplacer.replace(skeletonLoadingView);
        }
    }

    @Override
    public void hide() {
        if (mViewReplacer.getTargetView() instanceof ShimmerFrameLayout) {
            ((ShimmerFrameLayout) mViewReplacer.getTargetView()).stopShimmer();
        }
        mViewReplacer.restore();
    }

    public static class Builder {
        private final View mView;
        private int mSkeletonLayoutResID;
        private boolean mShimmerAnimate = true;
        private boolean mAutoStart;
        private int mShimmerBaseColor;
        private int mShimmerHighlightColor;
        private boolean mShimmerColored;
        private int mDuration = 1000;
        private int mShimmerShape;
        private int mDirection;
        private Shimmer mShimmer;

        public Builder(View view) {

            this(view, null);
        }

        public Builder(View view, Shimmer shimmer) {
            this.mView = view;
            this.mShimmerBaseColor = ContextCompat.getColor(mView.getContext(), R.color.shimmer_base_color);
            this.mShimmerHighlightColor = ContextCompat.getColor(mView.getContext(), R.color.shimmer_highlight_color);
            mShimmerColored = true;
            mShimmer = shimmer;
            mAutoStart = true;
        }

        /**
         * @param skeletonLayoutResID the loading skeleton layoutResID
         */
        public Builder load(@LayoutRes int skeletonLayoutResID) {
            this.mSkeletonLayoutResID = skeletonLayoutResID;
            return this;
        }

        /**
         * @param shimmerColor the shimmer color
         */
        public Builder color(@ColorRes int shimmerColor) {
            this.mShimmerHighlightColor = ContextCompat.getColor(mView.getContext(), shimmerColor);
            return this;
        }

        /**
         * @param shimmer whether show shimmer animation
         */
        public ViewSkeletonScreen.Builder shimmerAnimate(boolean shimmer) {
            this.mShimmerAnimate = shimmer;
            return this;
        }

        /**
         * the duration of the animation , the time it will take for the highlight to move from one end of the layout
         * to the other.
         *
         * @param duration Duration of the shimmer animation, in milliseconds
         */
        public ViewSkeletonScreen.Builder duration(int duration) {
            this.mDuration = duration;
            return this;
        }

        public ViewSkeletonScreen.Builder shape(@Shimmer.Shape int shape) {
            this.mShimmerShape = shape;
            return this;
        }

        public ViewSkeletonScreen.Builder direction(@Shimmer.Direction int direction) {
            this.mDirection = direction;
            return this;
        }

        public ViewSkeletonScreen.Builder autoStart(boolean autoStart) {
            this.mAutoStart = autoStart;
            return this;
        }

        public ViewSkeletonScreen.Builder shimmerColored(boolean shimmerColored) {
            this.mShimmerColored = shimmerColored;
            return this;
        }

        public ViewSkeletonScreen.Builder shimmerBaseColor(@ColorRes int shimmerBaseColor) {
            this.mShimmerBaseColor = ContextCompat.getColor(mView.getContext(), shimmerBaseColor);
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

        public ViewSkeletonScreen show() {
            ViewSkeletonScreen skeletonScreen = new ViewSkeletonScreen(this);
            skeletonScreen.show();
            return skeletonScreen;
        }

    }
}
