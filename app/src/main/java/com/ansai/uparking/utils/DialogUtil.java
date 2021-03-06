package com.ansai.uparking.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;

import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.flyco.dialog.widget.NormalListDialog;
import com.ansai.libcommon.util.AppUtils;
import com.ansai.uparking.R;

/**
 * 对话框使用类
 * User: PAPA
 * Date: 2016-08-05
 */
public class DialogUtil {

    public static int titleTextSize = 18;
    public static int btnTextSize = 16;
    private static int titleColor = ContextCompat.getColor(AppUtils.getAppContext(), R.color
            .color_333333);
    private static int contentColor = ContextCompat.getColor(AppUtils.getAppContext(), R.color
            .color_666666);
    private static int btnTextColor = ContextCompat.getColor(AppUtils.getAppContext(), R.color
            .color_333333);

//    private static BaseAnimatorSet mBasIn = new BounceTopEnter();
//    private static BaseAnimatorSet mBasOut = new SlideBottomExit();

    public static NormalDialog showNormalDialog(Context context, String title, String content,
                                                final String
                                                        leftBtn, String rightBtn, final View
            .OnClickListener leftListener, final View
            .OnClickListener rightListener) {
        final NormalDialog dialog = new NormalDialog(context);
        dialog.style(NormalDialog.STYLE_TWO).titleTextColor(titleColor).contentTextColor
                (contentColor).title(title).titleTextSize(titleTextSize).btnTextSize(btnTextSize)
                .content(content).btnTextColor(btnTextColor,
                btnTextColor).btnText(leftBtn, rightBtn)
                //.showAnim(mBasIn).dismissAnim(mBasOut)
                .show();
        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.superDismiss();
                if (leftListener != null)
                    leftListener.onClick(null);
            }
        }, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.superDismiss();
                if (rightListener != null)
                    rightListener.onClick(null);
            }
        });
        return dialog;
    }

    public static NormalDialog showNormalDialog(Context context, String content,
                                                String
                                                        leftBtn, String rightBtn, final View
            .OnClickListener leftListener, final View
            .OnClickListener rightListener) {
        final NormalDialog dialog = new NormalDialog(context);
        dialog.style(NormalDialog.STYLE_TWO).titleTextSize(titleTextSize).btnTextSize
                (btnTextSize).titleTextColor(titleColor).contentTextColor
                (contentColor).content(content).btnTextColor(btnTextColor,
                btnTextColor).btnText(leftBtn, rightBtn)
                //.showAnim(mBasIn).dismissAnim(mBasOut)
                .show();


        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.superDismiss();
                if (leftListener != null)
                    leftListener.onClick(null);
            }
        }, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.superDismiss();
                if (rightListener != null)
                    rightListener.onClick(null);
            }
        });
        return dialog;
    }

    public static NormalDialog showTipsDialog(Context context, String title, String content, String
            btnText, final View.OnClickListener clickListener) {
        final NormalDialog dialog = new NormalDialog(context);
        dialog.style(NormalDialog.STYLE_TWO).titleTextSize(titleTextSize).btnTextSize
                (btnTextSize).titleTextColor(titleColor).contentTextColor
                (contentColor).title(title).content(content).btnTextColor(btnTextColor).btnNum(1)
                .btnText
                        (btnText)
//                .showAnim(mBasIn)//ceshi
//                .dismissAnim(mBasOut)
                .show();
        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.superDismiss();
                if (clickListener != null)
                    clickListener.onClick(null);
            }
        });
        return dialog;
    }

    public static NormalDialog showTipsDialog(Context context, String title, String content, String
            btnText) {
        final NormalDialog dialog = new NormalDialog(context);
        dialog.style(NormalDialog.STYLE_TWO).titleTextSize(titleTextSize).btnTextSize
                (btnTextSize).titleTextColor(titleColor).titleTextSize(16).contentTextColor
                (contentColor).contentTextSize(14).contentGravity(Gravity.LEFT).title(title)
                .content(content).btnTextColor(btnTextColor).btnNum(1)
                .btnText
                        (btnText)
//                .showAnim(mBasIn)
//                .dismissAnim(mBasOut)
                .show();
        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.superDismiss();
            }
        });
        return dialog;
    }

    public static NormalDialog showTipsDialog(Context context, String title, String content, String
            btnText, int titleSize, int contentSize, int btnSize) {
        final NormalDialog dialog = new NormalDialog(context);
        dialog.style(NormalDialog.STYLE_TWO).titleTextSize(titleTextSize).btnTextSize
                (btnTextSize).titleTextColor(titleColor).titleTextSize(titleSize).contentTextColor
                (contentColor).contentTextSize(contentSize).contentGravity(Gravity.CENTER).title
                (title)
                .content(content).btnTextColor(btnTextColor).btnTextSize(btnSize).btnNum(1)
                .btnText(btnText)
                .show();
        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.superDismiss();
            }
        });
        return dialog;
    }


    public static NormalDialog getBaseDialog(Context context) {
        NormalDialog dialog = new NormalDialog(context);
        dialog.style(NormalDialog.STYLE_TWO).titleTextSize(titleTextSize).btnTextSize
                (btnTextSize).titleTextColor(titleColor).contentTextColor
                (contentColor).btnTextColor(btnTextColor);
//                .showAnim(mBasIn)//ceshi
//                .dismissAnim(mBasOut);
        return dialog;
    }

    public static NormalDialog showTipsDialog(Context context, String content, String
            btnText, final View.OnClickListener clickListener) {
        final NormalDialog dialog = new NormalDialog(context);
        dialog.style(NormalDialog.STYLE_TWO).titleTextSize(titleTextSize).btnTextSize
                (btnTextSize).titleTextColor(titleColor).contentTextColor
                (contentColor).content(content).btnTextColor(btnTextColor).btnNum(1).btnText
                (btnText)
//                .showAnim(mBasIn)//
//                .dismissAnim(mBasOut)
                .show();
        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.superDismiss();
                if (clickListener != null)
                    clickListener.onClick(null);
            }
        });
        return dialog;
    }

    public static NormalDialog showTipsDialog(Context context, String content, String
            btnText) {
        final NormalDialog dialog = new NormalDialog(context);

        dialog.style(NormalDialog.STYLE_TWO).titleTextSize(titleTextSize).btnTextSize
                (btnTextSize).titleTextColor(titleColor).contentTextColor
                (contentColor).content(content).btnTextColor(btnTextColor).btnNum(1).btnText
                (btnText)
//                .showAnim(mBasIn)//
//                .dismissAnim(mBasOut)
                .show();
        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.superDismiss();
            }
        });
        return dialog;
    }


    public static NormalListDialog showListDialog(Context context, String title, final String[]
            items, final
                                                  AdapterView
                                                          .OnItemClickListener listener) {
        final NormalListDialog dialog = new NormalListDialog(context, items);

        dialog.dividerColor(ContextCompat.getColor(context, R.color.color_divide));
        dialog.titleBgColor(ContextCompat.getColor(context, R.color.white)).titleTextColor
                (titleColor).titleTextSize_SP
                (titleTextSize).title(title)//
                //.showAnim(mBasIn)//
                //.dismissAnim(mBasOut)//
                .show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                if (listener != null)
                    listener.onItemClick(parent, view, position, id);

            }
        });
        return dialog;
    }

}
