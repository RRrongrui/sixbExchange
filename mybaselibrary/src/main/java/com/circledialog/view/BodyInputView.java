package com.circledialog.view;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.circledialog.params.ButtonParams;
import com.circledialog.params.CircleParams;
import com.circledialog.params.DialogParams;
import com.circledialog.params.InputParams;
import com.circledialog.params.TitleParams;
import com.circledialog.res.drawable.CircleDrawable;
import com.circledialog.res.drawable.InputDrawable;
import com.circledialog.res.values.CircleColor;
import com.fivefivelike.mybaselibrary.R;

/**
 * Created by hupei on 2017/3/31.
 */

class BodyInputView extends ScaleLinearLayout {
    private ScaleEditText mEditText;

    public BodyInputView(Context context, CircleParams params) {
        super(context);
        init(context, params);
    }

    private void init(Context context, CircleParams params) {
        DialogParams dialogParams = params.dialogParams;
        TitleParams titleParams = params.titleParams;
        InputParams inputParams = params.inputParams;
        ButtonParams negativeParams = params.negativeParams;
        ButtonParams positiveParams = params.positiveParams;

        //如果标题没有背景色，则使用默认色
        int backgroundColor = inputParams.backgroundColor != 0 ? inputParams.backgroundColor :
                CircleColor.getBgDialog();

        //有标题没按钮则底部圆角
        if (titleParams != null && negativeParams == null && positiveParams == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setBackground(new CircleDrawable(backgroundColor, 0, 0, dialogParams.radius,
                        dialogParams.radius));
            } else {
                setBackgroundDrawable(new CircleDrawable(backgroundColor, 0, 0, dialogParams
                        .radius, dialogParams.radius));
            }
        }
        //没标题有按钮则顶部圆角
        else if (titleParams == null && (negativeParams != null || positiveParams != null)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setBackground(new CircleDrawable(backgroundColor, dialogParams.radius, dialogParams
                        .radius, 0, 0));
            } else {
                setBackgroundDrawable(new CircleDrawable(backgroundColor, dialogParams.radius,
                        dialogParams.radius, 0, 0));
            }
        }
        //没标题没按钮则全部圆角
        else if (titleParams == null && negativeParams == null && positiveParams == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setBackground(new CircleDrawable(backgroundColor, dialogParams.radius));
            } else {
                setBackgroundDrawable(new CircleDrawable(backgroundColor, dialogParams.radius));
            }
        }
        //有标题有按钮则不用考虑圆角
        else
            setBackgroundColor(backgroundColor);


        mEditText = new ScaleEditText(context);
        mEditText.setHint(inputParams.hintText);
        mEditText.setHintTextColor(inputParams.hintTextColor);
        mEditText.setTextSize(inputParams.textSize);
        mEditText.setTextColor(inputParams.textColor);
        mEditText.setHeight(inputParams.inputHeight);
        mEditText.setPadding(
                getResources().getDimensionPixelOffset(R.dimen.trans_15px),
                getResources().getDimensionPixelOffset(R.dimen.trans_15px),
                getResources().getDimensionPixelOffset(R.dimen.trans_15px),
                getResources().getDimensionPixelOffset(R.dimen.trans_15px)
        );

        if (!TextUtils.isEmpty(inputParams.defaultText)) {
            mEditText.setText(inputParams.defaultText);
        }

        int backgroundResourceId = inputParams.inputBackgroundResourceId;
        if (backgroundResourceId == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mEditText.setBackground(new InputDrawable(inputParams.strokeWidth, inputParams
                        .strokeColor, inputParams.inputBackgroundColor));
            } else {
                mEditText.setBackgroundDrawable(new InputDrawable(inputParams.strokeWidth,
                        inputParams.strokeColor, inputParams.inputBackgroundColor));
            }
        } else {
            mEditText.setBackgroundResource(backgroundResourceId);
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        int[] margins = inputParams.margins;
        if (margins != null) {
            layoutParams.setMargins(margins[0], margins[1], margins[2], margins[3]);
        }
        addView(mEditText, layoutParams);
    }

    public EditText getInput() {
        return mEditText;
    }
}
