package com.zeal4rea.doubanmoviedemo.util.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zeal4rea.doubanmoviedemo.R;

public class ExpandableTextView extends LinearLayout {
    private final static String EXPAND = "展开";
    private final static String COLLAPSE = "折叠";
    private Context mContext;
    private TextView mTextViewContent;
    private TextView mTextViewAction;
    private boolean isCollapsed = true;
    private boolean isChange = false;
    private boolean isAnimating = false;
    private int maxCollapsedLines;
    private int mTextHeightWithMaxLines;
    private int mCollapsedHeight;
    private int mMarginBetweenTextAndBottom;
    private int mExpandableTextId;
    private int mExpandableActionId;

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView, defStyleAttr, 0);
        if (a != null) {
            maxCollapsedLines = a.getInt(R.styleable.ExpandableTextView_maxCollapsedLines, Integer.MAX_VALUE);
            mExpandableTextId = a.getResourceId(R.styleable.ExpandableTextView_expandableTextId, R.id.expandable_content);
            mExpandableActionId = a.getResourceId(R.styleable.ExpandableTextView_expandableActionId, R.id.expandable_action);
            a.recycle();
        }

        setOrientation(VERTICAL);
        setVisibility(GONE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }

    private void initView() {
        mTextViewContent = findViewById(mExpandableTextId);
        mTextViewAction = findViewById(mExpandableActionId);
        mTextViewAction.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                isAnimating = true;
                ExpandCollapseAnimation animation;
                if (isCollapsed) {
                    animation = new ExpandCollapseAnimation(ExpandableTextView.this, getHeight(), getHeight() + mTextHeightWithMaxLines - mTextViewContent.getHeight());
                } else {
                    animation = new ExpandCollapseAnimation(ExpandableTextView.this, getHeight(), mCollapsedHeight);
                }
                animation.setFillAfter(true);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        clearAnimation();
                        isAnimating = false;
                        isCollapsed = !isCollapsed;
                        mTextViewContent.setMaxLines(isCollapsed ? maxCollapsedLines : Integer.MAX_VALUE);
                        mTextViewAction.setText(isCollapsed ? EXPAND : COLLAPSE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                clearAnimation();
                startAnimation(animation);
            }
        });

        mTextViewAction.setText(isCollapsed ? EXPAND : COLLAPSE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!isChange || getVisibility() == GONE) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        isChange = false;

        mTextViewAction.setVisibility(GONE);
        mTextViewContent.setMaxLines(Integer.MAX_VALUE);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mTextViewContent.getLineCount() <= maxCollapsedLines) {
            return;
        }

        mTextHeightWithMaxLines = getRealHeightWithMaxLines(mTextViewContent);

        if (isCollapsed) {
            mTextViewContent.setMaxLines(maxCollapsedLines);
        }
        mTextViewAction.setVisibility(VISIBLE);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (isCollapsed) {
            mTextViewContent.post(new Runnable() {
                @Override
                public void run() {
                    mMarginBetweenTextAndBottom = getHeight() - mTextViewContent.getHeight();
                }
            });
            mCollapsedHeight = getMeasuredHeight();
        }
    }

    public void setText(String text) {
        isChange = true;
        mTextViewContent.setText(text);
        setVisibility(TextUtils.isEmpty(text) ? GONE : VISIBLE);
        clearAnimation();
        getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        requestLayout();
    }

    private static int getRealHeightWithMaxLines(TextView textView) {
        int height = textView.getLayout().getLineTop(textView.getLineCount());
        int padding = textView.getCompoundPaddingTop() + textView.getCompoundPaddingBottom();
        return height + padding;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isAnimating;
    }

    private class ExpandCollapseAnimation extends Animation {
        private final View targetView;
        private final int startHeight;
        private final int endHeight;

        ExpandCollapseAnimation(View targetView, int startHeight, int endHeight) {
            this.targetView = targetView;
            this.startHeight = startHeight;
            this.endHeight = endHeight;
            setDuration(300);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int newHeight = (int) ((endHeight - startHeight) * interpolatedTime + startHeight);
            mTextViewContent.setMaxHeight(newHeight - mMarginBetweenTextAndBottom);
            targetView.getLayoutParams().height = newHeight;
            targetView.requestLayout();
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }
}
