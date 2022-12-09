package com.zdww.mylibrary;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * 类名: CustomProgressDialog <br/>
 * 功能: 自定义进度提示控件. <br/>
 * 创建时间: 2015-02-16 上午11:39:25 <br/>
 *
 * @author wujd
 * @since Jdk 1.7
 */
public class CustomProgressDialog extends Dialog {

    @SuppressWarnings("unused")
    private Context mContext = null;

    private static com.gsww.jzfp.view.CustomProgressDialog customProgressDialog = null;

    public CustomProgressDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, R.style.CustomProgressDialog);
        this.mContext = context;
    }

    public static com.gsww.jzfp.view.CustomProgressDialog show(Context context) {
        customProgressDialog = new com.gsww.jzfp.view.CustomProgressDialog(context, R.style.CustomProgressDialog);
        customProgressDialog.setContentView(R.layout.common_loading_dialog);
        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        customProgressDialog.show();
        return customProgressDialog;
    }


    public static Dialog show(Context context, String title, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_vartical, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        if (msg != null) {
            tipTextView.setText(msg);// 设置加载信息
        }
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog_vartical);// 创建自定义样式dialog

        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));// 设置布局

    	/*customProgressDialog = new CustomProgressDialog(context,R.style.CustomProgressDialog);
        customProgressDialog.setContentView(R.layout.common_loading_dialog);
        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        customProgressDialog.setCancelable(cancelable);
    	TextView tvMsg = (TextView)customProgressDialog.findViewById(R.id.common_loading_dialog_tv);
        if (tvMsg != null){
            tvMsg.setText(msg);
        }*/
        loadingDialog.show();
        return loadingDialog;
    /*	customProgressDialog = new CustomProgressDialog(context,R.style.CustomProgressDialog);
        customProgressDialog.setContentView(R.layout.common_loading_dialog);
        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        customProgressDialog.setCancelable(false);
    	TextView tvMsg = (TextView)customProgressDialog.findViewById(R.id.common_loading_dialog_tv);
        if (tvMsg != null){
            tvMsg.setText(msg);
        }
        customProgressDialog.show();
    	return customProgressDialog;*/
    }

    public static Dialog show(Context context, String title, String msg, boolean cancelable) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_vartical, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        spaceshipImage.setImageResource(R.drawable.loading_jzfp_hn);
        AnimationDrawable animationDrawable = (AnimationDrawable) spaceshipImage.getDrawable();
        animationDrawable.start();


//        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
//                context, R.anim.loading);
//        // 使用ImageView显示动画
//        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        if (msg != null) {
            tipTextView.setText(msg);// 设置加载信息
        }
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog_vartical);// 创建自定义样式dialog

        loadingDialog.setCancelable(cancelable);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));// 设置布局
    	
    	/*customProgressDialog = new CustomProgressDialog(context,R.style.CustomProgressDialog);
        customProgressDialog.setContentView(R.layout.common_loading_dialog);
        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        customProgressDialog.setCancelable(cancelable);
    	TextView tvMsg = (TextView)customProgressDialog.findViewById(R.id.common_loading_dialog_tv);
        if (tvMsg != null){
            tvMsg.setText(msg);
        }*/
        loadingDialog.show();
        return loadingDialog;
    }

    public static Dialog horizontalShow(Context context, String title, String msg, boolean cancelable) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_horizontal, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        if (msg != null) {
            tipTextView.setText(msg);// 设置加载信息
        }
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog_horizontal);// 创建自定义样式dialog

        loadingDialog.setCancelable(cancelable);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
    	
    	/*customProgressDialog = new CustomProgressDialog(context,R.style.CustomProgressDialog);
        customProgressDialog.setContentView(R.layout.common_loading_dialog);
        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        customProgressDialog.setCancelable(cancelable);
    	TextView tvMsg = (TextView)customProgressDialog.findViewById(R.id.common_loading_dialog_tv);
        if (tvMsg != null){
            tvMsg.setText(msg);
        }*/
        loadingDialog.show();
        return loadingDialog;
    }

    public static com.gsww.jzfp.view.CustomProgressDialog show1(Context context, String title, String msg, boolean cancelable) {


        customProgressDialog = new com.gsww.jzfp.view.CustomProgressDialog(context, R.style.CustomProgressDialog);
        customProgressDialog.setContentView(R.layout.common_loading_dialog);
        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        customProgressDialog.setCancelable(cancelable);
        TextView tvMsg = (TextView) customProgressDialog.findViewById(R.id.common_loading_dialog_tv);
        if (tvMsg != null) {
            tvMsg.setText(msg);
        }
        customProgressDialog.show();
        return customProgressDialog;

    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if (customProgressDialog == null) {
            return;
        }
        ImageView imageView = (ImageView) customProgressDialog.findViewById(R.id.common_loading_dialog_iv);
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this.getContext(), R.anim.loading);
        imageView.startAnimation(hyperspaceJumpAnimation);
    }

    /**
     * setMessage 提示内容
     *
     * @param strMessage
     * @return
     */
    public com.gsww.jzfp.view.CustomProgressDialog setMessage(String strMessage) {
        TextView tvMsg = (TextView) customProgressDialog.findViewById(R.id.common_loading_dialog_tv);
        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }
        return customProgressDialog;
    }
}
