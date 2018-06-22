/*
 * Copyright (C) 2014 kince
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.progressbar;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;




public class UploadProgressBar extends ProgressBar {

	// 滑动的图像
	private Drawable mSlidePic;

    // 进度条高度
    private int mProgressHeight;

	// 图像滑动距离与进度条比值
	private float mScale;

	// 进度条外边框线条
	private int mStrokeWidth;

	public UploadProgressBar(Context context) {
		this(context, null);
	}

	public UploadProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public UploadProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	/**
     * 初始化相关的值
	 * @param context
	 */
	private void init(Context context) {

		mScale = 0.95f;
		mProgressHeight = SizeUtil.dp2px(context,10);
		mStrokeWidth = SizeUtil.dp2px(context,  0.5f);

		Drawable slidePic = context.getResources().getDrawable(R.drawable.ic_slide_pic);
		Rect bounds = new Rect(0, 0, slidePic.getIntrinsicWidth(), slidePic.getIntrinsicHeight());
		slidePic.setBounds(bounds);
		setProgressIndicator(slidePic);
		setProgress(0);
	    setVisibility(View.VISIBLE);
	}

    /**
     * 设置滑动图片
	 * @param indicator
	 */
	public void setProgressIndicator(Drawable indicator) {
		mSlidePic = indicator;
	}

	/**
     * 设置View的高度为图片的高度
	 * @param widthMeasureSpec
     * @param heightMeasureSpec
	 */
	@Override
	protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (mSlidePic != null) {
			final int width = getMeasuredWidth();
			final int height = getIndicatorHeight() ;
			setMeasuredDimension(width, height);
		}
	}

	/**
     * 获得图片的宽度
	 * @return
     */
	private int getIndicatorWidth() {
		if (mSlidePic == null) {
			return 0;
		}

		Rect r = mSlidePic.copyBounds();
		int width = r.width();

		return width;
	}

    /**
     * 获得图片的高度
	 * @return
     */
	private int getIndicatorHeight() {
		if (mSlidePic == null) {
			return 0;
		}

		Rect r = mSlidePic.copyBounds();
		int height = r.height();

		return height;
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		Drawable progressDrawable = getProgressDrawable();
		if (mSlidePic != null) {
			if (progressDrawable != null && progressDrawable instanceof LayerDrawable) {
				LayerDrawable d = (LayerDrawable) progressDrawable;
				for (int i = 0; i < d.getNumberOfLayers(); i++) {
 					d.getDrawable(i).getBounds().top =getIndicatorHeight()  - mProgressHeight;
					d.getDrawable(i).getBounds().bottom = getIndicatorHeight();
				}
			} else if (progressDrawable != null) {
				progressDrawable.getBounds().top = getIndicatorHeight()  - mProgressHeight;
				progressDrawable.getBounds().bottom = getIndicatorHeight();
			}
		}

		// 更新进度条
		updateProgressBar(canvas);

		super.onDraw(canvas);

		// 更新图片的位移
		if (mSlidePic != null) {
			canvas.save();
			int dx = 0;

			if (progressDrawable != null && progressDrawable instanceof LayerDrawable) {
				LayerDrawable d = (LayerDrawable) progressDrawable;
				Drawable progressBar = d.findDrawableByLayerId(R.id.progress);
				dx = progressBar.getBounds().right;
			} else if (progressDrawable != null) {
				dx = progressDrawable.getBounds().right;
			}

			dx = (int) ((dx - getIndicatorWidth() / 2  + getPaddingLeft())*mScale+0.5);
			canvas.translate(dx, 0);
			mSlidePic.draw(canvas);
			canvas.restore();
		}
	}


	@Override
	public synchronized void setProgress(int progress) {
		super.setProgress(progress);
		postInvalidate();
	}

	private float getScale(int progress) {
		float scale = getMax() > 0 ? (float) progress / (float) getMax() : 0;
		return scale;
	}


	/**
     * 更新进度条
	 * @param canvas
	 */
	private void updateProgressBar(Canvas canvas) {
		Drawable progressDrawable = getProgressDrawable();
		if (progressDrawable != null && progressDrawable instanceof LayerDrawable) {

			LayerDrawable d = (LayerDrawable) progressDrawable;
			final float scale = getScale(getProgress());
			Drawable progressBar = d.findDrawableByLayerId(R.id.progress);
			final int width = d.getBounds().right - d.getBounds().left;

			if (progressBar != null) {
				Rect progressBarBounds = progressBar.getBounds();
				progressBarBounds.right = progressBarBounds.left + (int) (width * scale + 0.5f);
				progressBar.setBounds(progressBarBounds);
			}

			// 更新进度条上的斜线
		   Drawable patternOverlay = d.findDrawableByLayerId(R.id.pattern);

		   if (patternOverlay != null) {
			   if (progressBar != null) {
				Rect patternOverlayBounds = progressBar.copyBounds();
				final int left = patternOverlayBounds.left;
				final int right = patternOverlayBounds.right;
				patternOverlayBounds.left = (left + 1 > right) ? left + 10 : left + 1;
				patternOverlayBounds.top = patternOverlayBounds.top + mStrokeWidth;
				patternOverlayBounds.bottom = patternOverlayBounds.bottom - mStrokeWidth;
				patternOverlayBounds.right = (right > 0) ? right + 10 : right;
				patternOverlay.setBounds(patternOverlayBounds);
			   } else {
				Rect patternOverlayBounds = patternOverlay.getBounds();
				patternOverlayBounds.right = patternOverlayBounds.left + (int) (width * scale + 0.5f);
				patternOverlay.setBounds(patternOverlayBounds);
			  }
		   }
		}
	}


}
