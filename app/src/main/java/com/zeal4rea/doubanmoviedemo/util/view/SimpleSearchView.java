package com.zeal4rea.doubanmoviedemo.util.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zeal4rea.doubanmoviedemo.R;
import com.zeal4rea.doubanmoviedemo.util.Utils;

public class SimpleSearchView extends ConstraintLayout implements View.OnClickListener {

    private final static int ANIMATION_DURATION = 400;
    private boolean mIsSearchOpen = false;
    private Context mContext;
    private ImageButton mButBack;
    private ImageButton mButEmpty;
    private EditText mEditText;
    private OnQueryTextListener mOnQueryTextListener;
    private View mSearchLayout;
    private boolean mClearingFocus;

    public SimpleSearchView(Context context) {
        this(context, null);
    }

    public SimpleSearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleSearchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;

        initView();
        init(attrs, defStyle);
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.layout_search_view, this, true);

        mSearchLayout = findViewById(R.id.search_view_layout);
        mButBack = mSearchLayout.findViewById(R.id.search_view_action_back);
        mButEmpty = mSearchLayout.findViewById(R.id.search_view_action_empty);
        mEditText = mSearchLayout.findViewById(R.id.search_view_edit_text);

        mButBack.setOnClickListener(this);
        mButEmpty.setOnClickListener(this);

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                onSubmitQuery();
                return true;
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Editable text = mEditText.getText();
                boolean hasText = !TextUtils.isEmpty(text);
                mButEmpty.setVisibility(hasText ? VISIBLE : GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    showKeyboard(mEditText);
                }
            }
        });
    }

    private void onSubmitQuery() {
        String query = mEditText.getText().toString();
        if (!Utils.isTextEmpty(query)) {
            if (mOnQueryTextListener == null || !mOnQueryTextListener.onQueryTextSubmit(query)) {
                mEditText.setText(null);
                hideSearch();
            }
        }
    }

    @SuppressWarnings("unused")
    private void init(AttributeSet attrs, int defStyle) {
        //todo
    }

    private void showSearch() {
        showSearch(true);
    }

    private void showSearch(boolean animate) {
        if (mIsSearchOpen) {
            return;
        }

        mEditText.setText(null);
        mEditText.requestFocus();

        if (animate) {
            AnimationListener listener = new AnimationListener() {
                @Override
                public boolean onAnimationStart(View view) {
                    return false;
                }

                @Override
                public boolean onAnimationEnd(View view) {
                    return false;
                }
            };

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fadeIn(mSearchLayout, listener);
            } else {
                fadeInCompat(mSearchLayout, ANIMATION_DURATION, listener);
            }
        } else {
            mSearchLayout.setVisibility(VISIBLE);
        }

        mIsSearchOpen = true;
    }

    public void hideSearch() {
        if (!mIsSearchOpen) {
            return;
        }

        AnimationListener listener = new AnimationListener() {
            @Override
            public boolean onAnimationStart(View view) {
                return false;
            }

            @Override
            public boolean onAnimationEnd(View view) {
                mEditText.setText(null);
                clearFocus();
                mSearchLayout.setVisibility(GONE);
                mIsSearchOpen = false;
                return false;
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fadeOut(mSearchLayout, listener);
        } else {
            fadeOutCompat(mSearchLayout, ANIMATION_DURATION, listener);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void fadeIn(final View view, final AnimationListener listener) {
        int cx = view.getWidth() - (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 24, view.getResources().getDisplayMetrics());
        int cy = view.getHeight() / 2;
        int finalRadius = Math.max(view.getWidth(), view.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
        view.setVisibility(View.VISIBLE);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                listener.onAnimationStart(view);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                listener.onAnimationEnd(view);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        anim.start();
    }

    private void fadeInCompat(View view, int duration, final AnimationListener listener) {
        view.setVisibility(VISIBLE);
        view.setAlpha(0f);

        ViewCompat.animate(view).alpha(1f).setDuration(duration).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {
                if (listener == null || !listener.onAnimationStart(view)) {
                    view.setDrawingCacheEnabled(true);
                }
            }

            @Override
            public void onAnimationEnd(View view) {
                if (listener == null || !listener.onAnimationEnd(view)) {
                    view.setDrawingCacheEnabled(false);
                }
            }

            @Override
            public void onAnimationCancel(View view) {
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void fadeOut(final View view, final AnimationListener listener) {
        int cx = view.getWidth() - (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 24, view.getResources().getDisplayMetrics());
        int cy = view.getHeight() / 2;
        int finalRadius = Math.max(view.getWidth(), view.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, finalRadius, 0);
        view.setVisibility(View.VISIBLE);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                listener.onAnimationStart(view);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                listener.onAnimationEnd(view);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        anim.start();
    }

    private void fadeOutCompat(View view, int duration, final AnimationListener listener) {
        view.setVisibility(VISIBLE);
        ViewCompat.animate(view).alpha(0f).setDuration(duration).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {
                if (listener == null || !listener.onAnimationStart(view)) {
                    view.setDrawingCacheEnabled(true);
                }
            }

            @Override
            public void onAnimationEnd(View view) {
                if (listener == null || !listener.onAnimationEnd(view)) {
                    view.setVisibility(View.GONE);
                    view.setDrawingCacheEnabled(false);
                }
            }

            @Override
            public void onAnimationCancel(View view) {
            }
        });
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState savedState = new SavedState(superState);
        Editable text = mEditText.getText();
        savedState.query = text != null ? text.toString() : null;
        savedState.isSearchOpen = mIsSearchOpen;

        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            return;
        }
        SavedState savedState = (SavedState) state;
        if (savedState.isSearchOpen) {
            showSearch(false);
            mEditText.setText(savedState.query);
        }
        super.onRestoreInstanceState(savedState.getSuperState());
    }

    static class SavedState extends BaseSavedState {
        String query;
        boolean isSearchOpen;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.query = in.readString();
            this.isSearchOpen = in.readInt() == 1;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(query);
            out.writeInt(isSearchOpen ? 1 : 0);
        }

        //required field that makes Parcelables from a Parcel
        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showKeyboard(View view) {
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }
    }

    public boolean isSearchOpen() {
        return mIsSearchOpen;
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        return !mClearingFocus && isFocusable() && super.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    public void clearFocus() {
        mClearingFocus = true;
        hideKeyboard(this);
        super.clearFocus();
        mEditText.clearFocus();
        mClearingFocus = false;
    }

    public void setMenuItem(MenuItem menuItem) {
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                showSearch();
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == mButBack) {
            hideSearch();
        } else if (view == mButEmpty) {
            mEditText.setText(null);
        }
    }

    public void setOnQueryTextListener(OnQueryTextListener listener) {
        mOnQueryTextListener = listener;
    }

    public interface OnQueryTextListener {
        boolean onQueryTextSubmit(String query);
    }

    public interface AnimationListener {
        boolean onAnimationStart(View view);

        boolean onAnimationEnd(View view);
    }
}
