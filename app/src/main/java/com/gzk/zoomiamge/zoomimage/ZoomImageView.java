package com.gzk.zoomiamge.zoomimage;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by gzhenkai on 16/1/19.
 */
public class ZoomImageView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener ,
        ScaleGestureDetector.OnScaleGestureListener{

    private Matrix mMatrix;//图片矩阵

    private boolean mOnce;//初始化一次

    private float mInitScale = 1.0f;
    private float mMaxScale = 4.0f;
    private float mMinScale = 0.5f;


    private ScaleGestureDetector mScaleGestureDetector;




    public ZoomImageView(Context context) {
        this(context, null);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mMatrix = new Matrix();
        setScaleType(ScaleType.MATRIX);

        mScaleGestureDetector = new ScaleGestureDetector(context,this);




    }




    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void removeOnAttachStateChangeListener(OnAttachStateChangeListener listener) {
        super.removeOnAttachStateChangeListener(listener);
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        if (!mOnce) {
            Drawable d = getDrawable();
            if (d == null) {
                return;
            }

            //控件大小
            int width = getWidth();
            int height = getHeight();

            //图片大小
            int wd = d.getIntrinsicWidth();
            int hd = d.getIntrinsicHeight();

            float scale = 1.0f;

            if ((wd > width) && (hd < height)) {

                scale = (width * 1.0f) / wd;
            }

            if ((wd > width) && (hd > height)) {
                scale = Math.min(width * 1.0f / wd, height * 1.0f / hd);
            }
            if ((wd < width) && (hd > height)) {
                scale = (height * 1.0f) / hd;
            }

            if ((wd < width) && (hd < height)) {
                scale = Math.min(width * 1.0f / wd, height * 1.0f / hd);
            }


            mInitScale = scale;
            mMaxScale = mInitScale * 4;
            mMinScale = mInitScale / 2;

            mMatrix.postTranslate((getWidth() - wd) * 1.0f / 2, (getHeight() - hd) * 1.0f / 2);
            mMatrix.postScale(scale, scale, getWidth() * 1.0f / 2, getHeight() * 1.0f / 2);
            setImageMatrix(mMatrix);

            mOnce = true;
        }

    }



    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
    }
}
