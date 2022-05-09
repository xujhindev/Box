package com.xujhin.box.opengl.squre;

import android.opengl.GLES30;
import android.opengl.Matrix;
import com.xujhin.box.opengl.base.BaseGLSL;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static com.xujhin.box.opengl.base.BaseGLSL.createOpenGLProgram;

/**
 * 立方体
 */
public class Cube extends BaseGLSL {

    private FloatBuffer vertexBuffer, colorBuffer;
    private ShortBuffer indexBuffer;
    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "uniform mat4 vMatrix;" +
                    "varying  vec4 vColor;" +
                    "attribute vec4 aColor;" +
                    "void main() {" +
                    "  gl_Position = vMatrix*vPosition;" +
                    "  vColor=aColor;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "varying vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private int mProgram;

    final int COORDS_PER_VERTEX = 3;
    final float cubePositions[] = {
            -1.0f, 1.0f, 1.0f,    //正面左上0
            -1.0f, -1.0f, 1.0f,   //正面左下1
            1.0f, -1.0f, 1.0f,    //正面右下2
            1.0f, 1.0f, 1.0f,     //正面右上3
            -1.0f, 1.0f, -1.0f,    //反面左上4
            -1.0f, -1.0f, -1.0f,   //反面左下5
            1.0f, -1.0f, -1.0f,    //反面右下6
            1.0f, 1.0f, -1.0f,     //反面右上7
    };
    final short index[] = {
            6, 7, 4, 6, 4, 5,    //后面
            6, 3, 7, 6, 2, 3,    //右面
            6, 5, 1, 6, 1, 2,    //下面
            0, 3, 2, 0, 2, 1,    //正面
            0, 1, 5, 0, 5, 4,    //左面
            0, 7, 3, 0, 4, 7,    //上面
    };

    float color[] = {
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
    };

    private int mPositionHandle;
    private int mColorHandle;

    private float[] mViewMatrix = new float[16];
    private float[] mProjectMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];

    private int mMatrixHandler;

    public Cube() {
        ByteBuffer bb = ByteBuffer.allocateDirect(cubePositions.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(cubePositions);
        vertexBuffer.position(0);

        ByteBuffer dd = ByteBuffer.allocateDirect(
                color.length * 4);
        dd.order(ByteOrder.nativeOrder());
        colorBuffer = dd.asFloatBuffer();
        colorBuffer.put(color);
        colorBuffer.position(0);

        ByteBuffer cc = ByteBuffer.allocateDirect(index.length * 2);
        cc.order(ByteOrder.nativeOrder());
        indexBuffer = cc.asShortBuffer();
        indexBuffer.put(index);
        indexBuffer.position(0);
        mProgram = createOpenGLProgram(vertexShaderCode, fragmentShaderCode);
    }

    public void onSurfaceCreated() {
        //开启深度测试
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
    }

    public void onSurfaceChanged(int width, int height) {
        //计算宽高比
        float ratio = (float) width / height;
        //设置透视投影
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 3, 20);
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 5.0f, 5.0f, 10.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
    }

    public void setMatrix(float[] matrix) {
        this.mMVPMatrix = matrix;
    }

    public void draw() {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
        //将程序加入到OpenGLES2.0环境
        GLES30.glUseProgram(mProgram);
        //获取变换矩阵vMatrix成员句柄
        mMatrixHandler = GLES30.glGetUniformLocation(mProgram, "vMatrix");
        //指定vMatrix的值
        if (mMVPMatrix != null) {
            GLES30.glUniformMatrix4fv(mMatrixHandler, 1, false, mMVPMatrix, 0);
        }
        //获取顶点着色器的vPosition成员句柄
        mPositionHandle = GLES30.glGetAttribLocation(mProgram, "vPosition");
        //启用三角形顶点的句柄
        GLES30.glEnableVertexAttribArray(mPositionHandle);
        //准备三角形的坐标数据
        GLES30.glVertexAttribPointer(mPositionHandle, 3, GLES30.GL_FLOAT, false, 0, vertexBuffer);
        //获取片元着色器的vColor成员的句柄
        mColorHandle = GLES30.glGetAttribLocation(mProgram, "aColor");
        //设置绘制三角形的颜色
//        GLES30.glUniform4fv(mColorHandle, 2, color, 0);
        GLES30.glEnableVertexAttribArray(mColorHandle);
        GLES30.glVertexAttribPointer(mColorHandle, 4, GLES30.GL_FLOAT, false, 0, colorBuffer);
        //索引法绘制正方体
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, index.length, GLES30.GL_UNSIGNED_SHORT, indexBuffer);
        //禁止顶点数组的句柄
        GLES30.glDisableVertexAttribArray(mPositionHandle);
    }


}