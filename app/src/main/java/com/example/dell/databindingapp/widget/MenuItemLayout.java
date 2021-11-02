package com.example.dell.databindingapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.dell.databindingapp.R;

/**
 * Created by WuShengjun on 2017/10/17.
 */

public class MenuItemLayout extends LinearLayout {
    private static final String TAG = MenuItemLayout.class.getSimpleName();

    private int itemPadding;
    private int itemPaddingLeft;
    private int itemPaddingRight;
    private int itemPaddingTop;
    private int itemPaddingBottom;
    private int topSpaceHeight;
    private int topSpaceColor;
    private int bottomSpaceHeight;
    private int bottomSpaceColor;
    private Drawable startIcon;
    private int startIconPadding;
    private int startIconPaddingLeft;
    private int startIconPaddingRight;
    private int startIconPaddingTop;
    private int startIconPaddingBottom;
    private Drawable endIcon;
    private int endIconPadding;
    private int endIconPaddingLeft;
    private int endIconPaddingRight;
    private int endIconPaddingTop;
    private int endIconPaddingBottom;
    private CharSequence text;
    private int textSize;
    private int textColor;
    private int descriptionPaddingLeft;
    private CharSequence description;
    private CharSequence descriptionHint;
    private boolean descriptionEnabled;
    private int descriptionTextSize;
    private int descriptionGravity;
    private int descriptionTextColor;
    private Drawable descriptionIcon;
    private CharSequence bottomDescription;
    private int bottomDescriptionTextSize;
    private int bottomDescriptionGravity;
    private int bottomDescriptionTextColor;
    private int bottomDescriptionPaddingLeft;
    private int bottomDescriptionPaddingRight;
    private int bottomDescriptionPaddingTop;
    private int lineColor;
    private int lineHeight;
    private int lineMarginStart;
    private int lineMarginEnd;

    public MenuItemLayout(Context context) {
        this(context, null);
    }

    public MenuItemLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuItemLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initDefaultAttrs(context);
        if (attrs != null) {
            initCustomAttrs(context, attrs);
        }
        addContent(context);
    }

    private void initDefaultAttrs(Context context) {
        itemPadding = dp2px(context, 0);
        itemPaddingLeft = dp2px(context, 10);
        itemPaddingRight = dp2px(context, 10);
        itemPaddingTop = dp2px(context, 10);
        itemPaddingBottom = dp2px(context, 10);
        topSpaceHeight = dp2px(context, 0);
        topSpaceColor = Color.parseColor("#f0f0f0");
        bottomSpaceHeight = dp2px(context, 0);
        bottomSpaceColor = Color.parseColor("#f0f0f0");
        startIconPadding = dp2px(context, 0);
        startIconPaddingLeft = dp2px(context, 0);
        startIconPaddingRight = dp2px(context, 0);
        startIconPaddingTop = dp2px(context, 0);
        startIconPaddingBottom = dp2px(context, 0);
        endIconPadding = dp2px(context, 0);
        endIconPaddingLeft = dp2px(context, 0);
        endIconPaddingRight = dp2px(context, 0);
        endIconPaddingTop = dp2px(context, 0);
        endIconPaddingBottom = dp2px(context, 0);
        text = "";
        textColor = Color.parseColor("#404040");
        textSize = sp2px(context, 14);
        description = "";
        descriptionHint = "";
        descriptionEnabled = false;
        descriptionGravity = Gravity.RIGHT;
        descriptionTextColor = Color.parseColor("#808080");
        descriptionTextSize = sp2px(context, 14);
        bottomDescription = "";
        bottomDescriptionGravity = Gravity.LEFT;
        bottomDescriptionTextColor = Color.parseColor("#808080");
        bottomDescriptionTextSize = sp2px(context, 14);
        lineColor = Color.parseColor("#d0d0d0");
        lineHeight = context.getResources().getDimensionPixelSize(R.dimen.sw_1dp); // px
        lineMarginStart = dp2px(context, 12);
        lineMarginEnd = dp2px(context, 0);
    }

    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Common_MenuItemLayout);
        final int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            initAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    protected void initAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.Common_MenuItemLayout_common_itemPadding) {
            if (typedArray.hasValue(attr)) {
                itemPadding = typedArray.getDimensionPixelSize(attr, itemPadding);
                itemPaddingLeft = itemPadding;
                itemPaddingTop = itemPadding;
                itemPaddingRight = itemPadding;
                itemPaddingBottom = itemPadding;
            }
        } else if (attr == R.styleable.Common_MenuItemLayout_common_itemPaddingLeft) {
            itemPaddingLeft = typedArray.getDimensionPixelSize(attr, itemPaddingLeft);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_itemPaddingRight) {
            itemPaddingRight = typedArray.getDimensionPixelSize(attr, itemPaddingRight);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_itemPaddingTop) {
            itemPaddingTop = typedArray.getDimensionPixelSize(attr, itemPaddingTop);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_itemPaddingBottom) {
            itemPaddingBottom = typedArray.getDimensionPixelSize(attr, itemPaddingBottom);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_topSpaceHeight) {
            topSpaceHeight = typedArray.getDimensionPixelSize(attr, topSpaceHeight);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_topSpaceColor) {
            topSpaceColor = typedArray.getColor(attr, topSpaceColor);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_bottomSpaceHeight) {
            bottomSpaceHeight = typedArray.getDimensionPixelSize(attr, bottomSpaceHeight);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_bottomSpaceColor) {
            bottomSpaceColor = typedArray.getColor(attr, bottomSpaceColor);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_startIcon) {
            startIcon = typedArray.getDrawable(attr);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_startIconPadding) {
            if (typedArray.hasValue(attr)) {
                startIconPadding = typedArray.getDimensionPixelSize(attr, startIconPadding);
                startIconPaddingLeft = startIconPadding;
                startIconPaddingTop = startIconPadding;
                startIconPaddingRight = startIconPadding;
                startIconPaddingBottom = startIconPadding;
            }
        } else if (attr == R.styleable.Common_MenuItemLayout_common_startIconPaddingLeft) {
            startIconPaddingLeft = typedArray.getDimensionPixelSize(attr, startIconPaddingLeft);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_startIconPaddingRight) {
            startIconPaddingRight = typedArray.getDimensionPixelSize(attr, startIconPaddingRight);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_startIconPaddingTop) {
            startIconPaddingTop = typedArray.getDimensionPixelSize(attr, startIconPaddingTop);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_startIconPaddingBottom) {
            startIconPaddingBottom = typedArray.getDimensionPixelSize(attr, startIconPaddingBottom);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_endIcon) {
            endIcon = typedArray.getDrawable(attr);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_endIconPadding) {
            if (typedArray.hasValue(attr)) {
                endIconPadding = typedArray.getDimensionPixelSize(attr, endIconPadding);
                endIconPaddingLeft = endIconPadding;
                endIconPaddingTop = endIconPadding;
                endIconPaddingRight = endIconPadding;
                endIconPaddingBottom = endIconPadding;
            }
        } else if (attr == R.styleable.Common_MenuItemLayout_common_endIconPaddingLeft) {
            endIconPaddingLeft = typedArray.getDimensionPixelSize(attr, endIconPaddingLeft);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_endIconPaddingRight) {
            endIconPaddingRight = typedArray.getDimensionPixelSize(attr, endIconPaddingRight);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_endIconPaddingTop) {
            endIconPaddingTop = typedArray.getDimensionPixelSize(attr, endIconPaddingTop);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_endIconPaddingBottom) {
            endIconPaddingBottom = typedArray.getDimensionPixelSize(attr, endIconPaddingBottom);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_mainText) {
            text = typedArray.getString(attr);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_mainTextSize) {
            textSize = typedArray.getDimensionPixelSize(attr, textSize);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_mainTextColor) {
            textColor = typedArray.getColor(attr, textColor);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_descriptionPaddingLeft) {
            descriptionPaddingLeft = typedArray.getDimensionPixelSize(attr, 0);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_description) {
            description = typedArray.getString(attr);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_descriptionHint) {
            descriptionHint = typedArray.getString(attr);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_descriptionEnabled) {
            descriptionEnabled = typedArray.getBoolean(attr, descriptionEnabled);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_descriptionGravity) {
            descriptionGravity = typedArray.getInt(attr, descriptionGravity);
            if (descriptionGravity == 1) {
                descriptionGravity = Gravity.LEFT;
            } else if (descriptionGravity == 2) {
                descriptionGravity = Gravity.RIGHT;
            } else if (descriptionGravity == 3) {
                descriptionGravity = Gravity.CENTER;
            }
        } else if (attr == R.styleable.Common_MenuItemLayout_common_descriptionTextSize) {
            descriptionTextSize = typedArray.getDimensionPixelSize(attr, descriptionTextSize);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_descriptionTextColor) {
            descriptionTextColor = typedArray.getColor(attr, descriptionTextColor);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_descriptionIcon) {
            descriptionIcon = typedArray.getDrawable(attr);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_bottomDescription) {
            bottomDescription = typedArray.getString(attr);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_bottomDescriptionGravity) {
            bottomDescriptionGravity = typedArray.getInt(attr, bottomDescriptionGravity);
            if (bottomDescriptionGravity == 1) {
                bottomDescriptionGravity = Gravity.LEFT;
            } else if (bottomDescriptionGravity == 2) {
                bottomDescriptionGravity = Gravity.RIGHT;
            } else if (bottomDescriptionGravity == 3) {
                bottomDescriptionGravity = Gravity.CENTER;
            }
        } else if (attr == R.styleable.Common_MenuItemLayout_common_bottomDescriptionTextSize) {
            bottomDescriptionTextSize = typedArray.getDimensionPixelSize(attr, bottomDescriptionTextSize);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_bottomDescriptionTextColor) {
            bottomDescriptionTextColor = typedArray.getColor(attr, bottomDescriptionTextColor);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_bottomDescriptionPaddingLeft) {
            bottomDescriptionPaddingLeft = typedArray.getDimensionPixelSize(attr, bottomDescriptionPaddingLeft);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_bottomDescriptionPaddingRight) {
            bottomDescriptionPaddingRight = typedArray.getDimensionPixelSize(attr, bottomDescriptionPaddingRight);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_bottomDescriptionPaddingTop) {
            bottomDescriptionPaddingTop = typedArray.getDimensionPixelSize(attr, bottomDescriptionPaddingTop);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_lineColor) {
            lineColor = typedArray.getColor(attr, lineColor);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_lineHeight) {
            lineHeight = typedArray.getDimensionPixelSize(attr, lineHeight);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_lineMarginStart) {
            lineMarginStart = typedArray.getDimensionPixelSize(attr, lineMarginStart);
        } else if (attr == R.styleable.Common_MenuItemLayout_common_lineMarginEnd) {
            lineMarginEnd = typedArray.getDimensionPixelSize(attr, lineMarginEnd);
        }
    }

    private View topSpace, bottomSpace;
    private LinearLayout linearLayout;
    private ImageView startIconView, endIconView, descriptionIconView;
    private TextView textView, bottomDescriptionTextView;
    private TextView descriptionView;
    private View lineView;

    private void addContent(Context context) {
        setOrientation(LinearLayout.VERTICAL);
        if (getBackground() == null) {
            // 获取波纹点击效果resourceId
            int resourceId = getResources().getIdentifier("item_background_material", "drawable", "android");
            setBackgroundResource(resourceId); // 默认背景
        }

        if (topSpaceHeight > 0) {
            topSpace = new View(context);
            ViewGroup.LayoutParams topSpaceParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, topSpaceHeight);
            topSpace.setLayoutParams(topSpaceParams);
            topSpace.setBackgroundColor(topSpaceColor);
            addView(topSpace);
        }

        linearLayout = new LinearLayout(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(params);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);
        setLinearLayoutPadding();

        startIconView = new ImageView(context);
        setStartIconView();

        textView = new TextView(context);
        setMainText();

        if(descriptionEnabled) {
            descriptionView = new EditText(context);
        } else {
            descriptionView = new TextView(context);
        }
        descriptionView.setBackgroundColor(Color.TRANSPARENT);
        LayoutParams descriptionParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        descriptionView.setLayoutParams(descriptionParams);
        setDescriptionView();

        descriptionIconView = new ImageView(context);
        setDescriptionIconView();

        endIconView = new ImageView(context);
        setEndIconView();

        linearLayout.addView(startIconView);
        linearLayout.addView(textView);
        linearLayout.addView(descriptionView);
        linearLayout.addView(descriptionIconView);
        linearLayout.addView(endIconView);
        addView(linearLayout);

        bottomDescriptionTextView = new TextView(context);
        setBottomDescription();
        addView(bottomDescriptionTextView);
        if (TextUtils.isEmpty(bottomDescription)) {
            bottomDescriptionTextView.setVisibility(View.GONE);
        }

        lineView = new View(context);
        setLineView();
        addView(lineView);

        if (bottomSpaceHeight > 0) {
            bottomSpace = new View(context);
            ViewGroup.LayoutParams bottomSpaceParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, bottomSpaceHeight);
            bottomSpace.setLayoutParams(bottomSpaceParams);
            bottomSpace.setBackgroundColor(bottomSpaceColor);
            addView(bottomSpace);
        }
    }

    private void setLinearLayoutPadding() {
        if (linearLayout != null) {
            if (!TextUtils.isEmpty(bottomDescription)) {
                linearLayout.setPadding(itemPaddingLeft, itemPaddingTop, itemPaddingRight, 0);
            } else {
                linearLayout.setPadding(itemPaddingLeft, itemPaddingTop, itemPaddingRight, itemPaddingBottom);
            }
        }
    }

    private void setStartIconView() {
        if (startIconView != null) {
            startIconView.setImageDrawable(startIcon);
            startIconView.setPadding(startIconPaddingLeft, startIconPaddingTop, startIconPaddingRight, startIconPaddingBottom);
        }
    }

    private void setMainText() {
        if (textView != null) {
            textView.setText(text);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            textView.setTextColor(textColor);
        }
    }

    public TextView getTextView() {
        return textView;
    }

    public TextView getDescriptionView() {
        return descriptionView;
    }

    private void setDescriptionView() {
        if (descriptionView != null) {
            descriptionView.setEnabled(descriptionEnabled);
            descriptionView.setText(description);
            descriptionView.setHint(descriptionHint);
            descriptionView.setSingleLine();
            descriptionView.setTextSize(TypedValue.COMPLEX_UNIT_PX, descriptionTextSize);
            descriptionView.setTextColor(descriptionTextColor);
            descriptionView.setGravity(descriptionGravity);
            descriptionView.setPadding(descriptionPaddingLeft, 0, 0, 0);
        }
    }

    private void setBottomDescription() {
        if (bottomDescriptionTextView != null) {
            if (!TextUtils.isEmpty(bottomDescription)) {
                bottomDescriptionTextView.setText(bottomDescription);
                bottomDescriptionTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, bottomDescriptionTextSize);
                bottomDescriptionTextView.setTextColor(bottomDescriptionTextColor);
                bottomDescriptionTextView.setGravity(bottomDescriptionGravity);
                bottomDescriptionTextView.setPadding(bottomDescriptionPaddingLeft, bottomDescriptionPaddingTop, bottomDescriptionPaddingRight, itemPaddingBottom);
                bottomDescriptionTextView.setVisibility(View.VISIBLE);
            } else {
                bottomDescriptionTextView.setPadding(bottomDescriptionPaddingLeft, 0, itemPaddingRight, 0);
                bottomDescriptionTextView.setVisibility(View.GONE);
            }
        }
        setLinearLayoutPadding();
    }

    public TextView getBottomDescriptionTextView() {
        return bottomDescriptionTextView;
    }

    public ImageView getEndIconView() {
        return endIconView;
    }

    private void setEndIconView() {
        if (endIconView != null) {
            if (endIcon != null) {
                endIconView.setImageDrawable(endIcon);
            } else {
                endIconView.setImageBitmap(null);
            }
            endIconView.setPadding(endIconPaddingLeft, endIconPaddingTop, endIconPaddingRight, endIconPaddingBottom);
        }
    }

    private void setDescriptionIconView() {
        if (descriptionIconView != null) {
            descriptionIconView.setImageDrawable(descriptionIcon);
        }
    }

    private void setLineView() {
        if (lineView != null) {
            lineView.setBackgroundColor(lineColor);
            LayoutParams lineParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, lineHeight);
            lineParams.setMargins(lineMarginStart, 0, lineMarginEnd, 0);
            lineView.setLayoutParams(lineParams);
        }
    }

    public void setItemPadding(int itemPaddingLeft, int itemPaddingTop, int itemPaddingRight, int itemPaddingBottom) {
        this.itemPaddingLeft = itemPaddingLeft;
        this.itemPaddingTop = itemPaddingTop;
        this.itemPaddingRight = itemPaddingRight;
        this.itemPaddingBottom = itemPaddingBottom;
        setLinearLayoutPadding();
    }

    public int getItemPaddingLeft() {
        return itemPaddingLeft;
    }

    public int getItemPaddingRight() {
        return itemPaddingRight;
    }

    public int getItemPaddingTop() {
        return itemPaddingTop;
    }

    public int getItemPaddingBottom() {
        return itemPaddingBottom;
    }

    public Drawable getStartIcon() {
        return startIcon;
    }

    public void setStartIcon(Drawable startIcon) {
        this.startIcon = startIcon;
        setStartIconView();
    }

    public void setStartIconPadding(int startIconPaddingLeft, int startIconPaddingTop, int startIconPaddingRight, int startIconPaddingBottom) {
        this.startIconPaddingLeft = startIconPaddingLeft;
        this.startIconPaddingTop = startIconPaddingTop;
        this.startIconPaddingRight = startIconPaddingRight;
        this.startIconPaddingBottom = startIconPaddingBottom;
        setStartIconView();
    }

    public int getStartIconPaddingLeft() {
        return startIconPaddingLeft;
    }

    public int getStartIconPaddingRight() {
        return startIconPaddingRight;
    }

    public int getStartIconPaddingTop() {
        return startIconPaddingTop;
    }

    public int getStartIconPaddingBottom() {
        return startIconPaddingBottom;
    }

    public Drawable getEndIcon() {
        return endIcon;
    }

    public void setEndIcon(Drawable endIcon) {
        this.endIcon = endIcon;
        setEndIconView();
    }

    public int getEndIconPadding() {
        return endIconPadding;
    }

    public void setEndIconPadding(int endIconPaddingLeft, int endIconPaddingTop, int endIconPaddingRight, int endIconPaddingBottom) {
        this.endIconPaddingLeft = endIconPaddingLeft;
        this.endIconPaddingTop = endIconPaddingTop;
        this.endIconPaddingRight = endIconPaddingRight;
        this.endIconPaddingBottom = endIconPaddingBottom;
        setEndIconView();
    }

    public int getEndIconPaddingLeft() {
        return endIconPaddingLeft;
    }

    public int getEndIconPaddingRight() {
        return endIconPaddingRight;
    }

    public int getEndIconPaddingTop() {
        return endIconPaddingTop;
    }

    public int getEndIconPaddingBottom() {
        return endIconPaddingBottom;
    }

    public CharSequence getText() {
        return text;
    }

    public void setText(int resId) {
        setText(getContext().getString(resId));
    }

    public void setText(CharSequence text) {
        this.text = text;
        setMainText();
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        setMainText();
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        setMainText();
    }

    public CharSequence getDescription() {
        return description;
    }

    public void setDescription(int resId) {
        setDescription(getContext().getString(resId));
    }

    public void setDescription(CharSequence description) {
        this.description = description;
        setDescriptionView();
    }

    public void setBottomDescription(int resId) {
        setBottomDescription(getContext().getString(resId));
    }

    public void setBottomDescription(CharSequence description) {
        this.bottomDescription = description;
        setBottomDescription();
    }

    public void setBottomDescriptionLines(int lineNum){
        if (bottomDescriptionTextView != null){
            bottomDescriptionTextView.setMaxLines(2);
            bottomDescriptionTextView.setEllipsize(TextUtils.TruncateAt.END);
        }
    }

    public int getDescriptionTextSize() {
        return descriptionTextSize;
    }

    public void setDescriptionTextSize(int descriptionTextSize) {
        this.descriptionTextSize = descriptionTextSize;
        setDescriptionView();
    }

    public int getDescriptionTextColor() {
        return descriptionTextColor;
    }

    public void setDescriptionTextColor(int descriptionTextColor) {
        this.descriptionTextColor = descriptionTextColor;
        setDescriptionView();
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
        setLineView();
    }

    public int getLineHeight() {
        return lineHeight;
    }

    public void setLineHeight(int lineHeight) {
        this.lineHeight = lineHeight;
        setLineView();
    }

    public void setDescriptionIcon(int resources) {
        this.descriptionIcon = ContextCompat.getDrawable(getContext(), resources);
        setDescriptionIconView();
    }

    public void setLineMargin(int lineMarginStart, int lineMarginEnd) {
        this.lineMarginStart = lineMarginStart;
        this.lineMarginEnd = lineMarginEnd;
        setLineView();
    }

    public int getLineMarginStart() {
        return lineMarginStart;
    }

    public int getLineMarginEnd() {
        return lineMarginEnd;
    }

    public static int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }
}
