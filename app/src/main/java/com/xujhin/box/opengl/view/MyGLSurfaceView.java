package com.xujhin.box.opengl.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView {
    private CubeRenderer mRender;
    private float mRate = 1f;
    private float mPreviousX;
    private float mPreviousY;

    public MyGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(3);
        mRender = new CubeRenderer(context);
        setRenderer(mRender);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(3);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPreviousX = x;
                mPreviousY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                requestRender(x, y);
                break;
            case MotionEvent.ACTION_UP:
                updateVertex();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    private void requestRender(float x, float y) {
        getRate();
        float dx = x - mPreviousX;
        float dy = mPreviousY - y; //屏幕坐标Y轴向下，模型坐标Y轴向上
        float[] axis = getRotationAxis(dx, dy);
        float angle = getRotationAngle(dx, dy);
        mRender.setRotateArrt(axis, angle);
        requestRender();
    }

    // 更新顶点坐标
    private void updateVertex() {
        mRender.updateVertex();
        float[] axis = new float[] {0, 0, 1};
        float angle = 0f;
        mRender.setRotateArrt(axis, angle);
        requestRender();
    }

    // 获取旋转轴
    private float[] getRotationAxis(float dx, float dy) {
        float[] axis = new float[] {0, 0, 1};
        float dis = (float) Math.sqrt(dx * dx + dy * dy);
        if (dis < 0.000001f) {
            return axis;
        }
        // axis = (dx, dy, 0) X (0, 0, -1)
        axis[0] = -dy;
        axis[1] = dx;
        axis[2] = 0;
        return axis;
    }

    // 获取旋转角度
    private float getRotationAngle(float dx, float dy) {
        float d = (float) Math.sqrt(dx * dx + dy * dy);
        float angle = d * mRate;
        return angle;
    }

    // 获取滑动比例
    private void getRate() {
        float w = getWidth();
        float h = getHeight();
        mRate = 360 / (float) Math.sqrt(w * w + h * h);
    }
}