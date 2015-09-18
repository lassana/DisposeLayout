package com.github.lassana.adaptable_layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.github.lassana.adaptable_layout.R;

/**
 * @author lassana
 * @since 1/10/14
 */
public class AdaptableLayout extends ViewGroup {

    /**
     * Vertical padding between elements
     */
    private int mPaddingV;
    /**
     * Horizontal padding between elements
     */
    private int mPaddingH;
    /**
     * Layout height
     */
    private int mHeight;

    public AdaptableLayout(Context context) {
        super(context);
        setPadding(0, 0);
    }

    public AdaptableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public AdaptableLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(context, attrs);
    }

    public void setPadding(int paddingH, int paddingV) {
        mPaddingH = paddingH;
        mPaddingV = paddingV;
    }

    protected void initAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AdaptableLayout);
        int h = array.getDimensionPixelSize(R.styleable.AdaptableLayout_paddingH, 0);
        int v = array.getDimensionPixelSize(R.styleable.AdaptableLayout_paddingV, 0);
        setPadding(h, v);
        array.recycle();
    }

    public int getPaddingV() {
        return mPaddingV;
    }

    public int getPaddingH() {
        return mPaddingH;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        assert MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.UNSPECIFIED;
        int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        int count = getChildCount();
        int xPos = getPaddingLeft();
        int yPos = getPaddingTop();
        int childHeightMeasureSpec;
        if ( MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST ) {
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST);
        } else {
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        mHeight = 0;
        for ( int i=0; i<count; ++i ) {
            View child = getChildAt(i);
            if ( child != null && child.getVisibility() != View.GONE ) {
                child.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST),
                        childHeightMeasureSpec);
                int childW = child.getMeasuredWidth();
                mHeight = Math.max(mHeight, child.getMeasuredHeight() + mPaddingV);
                if ( xPos + childW > width ) {
                    xPos = getPaddingLeft();
                    yPos += mHeight;
                }
                xPos += childW + mPaddingH;
            }
        }
        if ( MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            height = yPos + mHeight;
        } else if ( MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST ) {
            if ( yPos + mHeight < height ) {
                height = yPos + mHeight;
            }
        }
        height += 5;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = r-1;
        int xPos = getPaddingLeft();
        int yPos = getPaddingTop();
        for ( int i = 0; i <getChildCount(); ++i ) {
            View child = getChildAt(i);
            if ( child != null && child.getVisibility() != View.GONE ) {
                int childW = child.getMeasuredWidth();
                int childH = child.getMeasuredHeight();
                if ( xPos + childW > width ) {
                    xPos = getPaddingLeft();
                    yPos += mHeight;
                }
                child.layout(xPos, yPos, xPos + childW, yPos + childH);
                xPos += childW + mPaddingH;
            }
        }
    }
}

