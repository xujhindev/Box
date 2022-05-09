package com.xujhin.box.opengl.view;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import com.xujhin.box.R;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.nio.FloatBuffer;

public class CubeRenderer implements GLSurfaceView.Renderer {
    private CubeModel model;
    private volatile FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer;
    private MyGLUtils mGLUtils;
    private int mProgramId;
    private float mRatio;


    public CubeRenderer(Context context) {
        model = new CubeModel();
        mGLUtils = new MyGLUtils(context);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig) {
        //设置背景颜色
        GLES30.glClearColor(0.1f, 0.2f, 0.3f, 0.4f);
        //启动深度测试
        gl.glEnable(GLES30.GL_DEPTH_TEST);
        //编译着色器
        final int vertexShaderId = mGLUtils.compileShader(GLES30.GL_VERTEX_SHADER, R.raw.vertex_shader);
        final int fragmentShaderId = mGLUtils.compileShader(GLES30.GL_FRAGMENT_SHADER, R.raw.fragment_shader);
        //链接程序片段
        mProgramId = mGLUtils.linkProgram(vertexShaderId, fragmentShaderId);
        GLES30.glUseProgram(mProgramId);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //设置视图窗口
        GLES30.glViewport(0, 0, width, height);
        getFloatBuffer();
        mRatio = 1.0f * width / height;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //将颜色缓冲区设置为预设的颜色
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
        mGLUtils.transform(mProgramId, mRatio); //计算MVP变换矩阵
        //启用顶点的数组句柄
        GLES30.glEnableVertexAttribArray(0);
        GLES30.glEnableVertexAttribArray(1);
        //准备顶点坐标和颜色数据
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 0, vertexBuffer);
        GLES30.glVertexAttribPointer(1, 4, GLES30.GL_FLOAT, false, 0, colorBuffer);
        //绘制正方体的表面（6个面，每面2个三角形，每个三角形3个顶点）
        draw();
        //禁止顶点数组句柄
        GLES30.glDisableVertexAttribArray(0);
        GLES30.glDisableVertexAttribArray(1);
    }

    private void draw() {
        int count = 4;
        for (int i = 0; i < model.getPlaneNum(); i++) {
            int first = i * count;
            GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, first, count);
        }
    }

    private void getFloatBuffer() {
        vertexBuffer = mGLUtils.getFloatBuffer(model.getVertex());
        colorBuffer = mGLUtils.getFloatBuffer(model.getColor());
    }

    // 设置旋转轴和旋转角度
    public void setRotateArrt(float[] axis, float angle) {
        mGLUtils.setRotateArrt(axis, angle);
    }

    // 更新顶点坐标
    public void updateVertex() {
        float[] newVertex = mGLUtils.updateVertex(model.getVertex());
        model.updateVertex(newVertex);
        vertexBuffer = mGLUtils.getFloatBuffer(model.getVertex());
    }
}


