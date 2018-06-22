package com.example.progressbar;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.text.NumberFormat;

/**
 * 上传加载dialog
 *
 */

public class UploadingDialog extends Dialog {

    private Context mContext;

    // 加载进度
    private UploadProgressBar mProgressBar;

    // 百分比
    private TextView mProgressNum;

    // 加载描述
    private TextView mUploadDesp;

    private DisplayMetrics mDisplayMetrics;

    private int maxProgress;

    public UploadingDialog(@NonNull Context context) {
        super(context);
        setDialogTheme();
        initDialogView(context);
    }

    /**
     * 初始化dialog参数
     * @param context
     */
    private void initDialogView(Context context) {
        mContext = context;
        mDisplayMetrics = mContext.getResources().getDisplayMetrics();
        maxProgress = 100;
        setContentView(R.layout.layout_upload_progressbar_dialog);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        mProgressBar = findViewById(R.id.upload_progress);
        mProgressNum = findViewById(R.id.tv_progress_num);
        mUploadDesp = findViewById(R.id.tv_upload_desp);
        Window window = getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.dialogWindowAnim);
        }
    }

    /**
     * 设置加载进度
     * @param progress
     */
    public void setProgress(int progress) {
        int progressTemp = 0;
        if(progress <maxProgress){
            progressTemp = progress;
        }else{
            progressTemp = maxProgress;
            mUploadDesp.setText(R.string.publish_require_upload_progress_complete);
        }
        mProgressBar.setProgress(progressTemp);
        mProgressNum.setText(getLabelText((float) progressTemp, (float) maxProgress).toString());
    }

    /**
     * 设置最大数值，默认是100
     * @param maxProgress
     */
    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
        mProgressBar.setMax(maxProgress);
    }

    /**
     * 百分比计算
     * @param progress
     * @param max
     * @return
     */
    private CharSequence getLabelText(float progress, float max) {
        return NumberFormat.getPercentInstance().format(progress / max);
    }

    /**
     * 设置dialog 原有的宽度
     */
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window dialogWindow = getWindow();
        if (dialogWindow != null) {
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = (int) (mDisplayMetrics.widthPixels);
            dialogWindow.setAttributes(lp);
        }
    }

    /**
     * set dialog theme(设置对话框主题)
     */
    private void setDialogTheme() {
        Window window = getWindow();
        if (window != null) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
    }
}
