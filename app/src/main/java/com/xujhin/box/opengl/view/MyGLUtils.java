package com.xujhin.box.opengl.view;

import android.content.Context;
import android.opengl.GLES30;


import android.content.Context;
import android.opengl.GLES30;
import android.opengl.Matrix;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class MyGLUtils {
    private Context mContext;
    private volatile float[] mModelMatrix; //模型变换矩阵
    private float mAngle = 0;
    private float[] mAxis = new float[]{0, 0, 1};

    public MyGLUtils(Context context) {
        mContext = context;
    }

    public FloatBuffer getFloatBuffer(float[] floatArr) {
        FloatBuffer fb = ByteBuffer.allocateDirect(floatArr.length * Float.BYTES)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        fb.put(floatArr);
        fb.position(0);
        return fb;
    }

    public ByteBuffer getByteBuffer(byte[] byteArr) {
        ByteBuffer bb = ByteBuffer.allocateDirect(byteArr.length * Byte.BYTES)
                .order(ByteOrder.nativeOrder());
        bb.put(byteArr);
        bb.position(0);
        return bb;
    }

    //通过代码片段编译着色器
    public int compileShader(int type, String shaderCode) {
        int shader = GLES30.glCreateShader(type);
        GLES30.glShaderSource(shader, shaderCode);
        GLES30.glCompileShader(shader);
        return shader;
    }

    //通过外部资源编译着色器
    public int compileShader(int type, int shaderId) {
        String shaderCode = readShaderFromResource(shaderId);
        return compileShader(type, shaderCode);
    }

    //链接到着色器
    public int linkProgram(int vertexShaderId, int fragmentShaderId) {
        final int programId = GLES30.glCreateProgram();
        //将顶点着色器加入到程序
        GLES30.glAttachShader(programId, vertexShaderId);
        //将片元着色器加入到程序
        GLES30.glAttachShader(programId, fragmentShaderId);
        //链接着色器程序
        GLES30.glLinkProgram(programId);
        return programId;
    }

    //从shader文件读出字符串
    private String readShaderFromResource(int shaderId) {
        InputStream is = mContext.getResources().openRawResource(shaderId);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    //计算MVP变换矩阵
    public synchronized void transform(int programId, float ratio) {
        //初始化modelMatrix, viewMatrix, projectionMatrix
        mModelMatrix = getIdentityMatrix(16, 0); //模型变换矩阵
        float[] viewMatrix = getIdentityMatrix(16, 0); //观测变换矩阵
        float[] projectionMatrix = getIdentityMatrix(16, 0); //投影变换矩阵
        //获取modelMatrix, viewMatrix, projectionMatrix
        Matrix.rotateM(mModelMatrix, 0, mAngle, mAxis[0], mAxis[1], mAxis[2]); //获取模型旋转变换矩阵
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, 10, 0, 0, 0, 0, 1, 0); //获取观测变换矩阵
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 20); //获取投影变换矩阵
        //计算MVP变换矩阵: mvpMatrix = projectionMatrix * viewMatrix * modelMatrix
        float[] tempMatrix = new float[16];
        float[] mvpMatrix = new float[16];
        Matrix.multiplyMM(tempMatrix, 0, viewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, tempMatrix, 0);
        //设置MVP变换矩阵
        int mvpMatrixHandle = GLES30.glGetUniformLocation(programId, "mvpMatrix");
        GLES30.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);
    }

    private float[] getIdentityMatrix(int size, int offset) {
        float[] matrix = new float[size];
        Matrix.setIdentityM(matrix, offset);
        return matrix;
    }

    // 设置旋转轴和旋转角度
    public void setRotateArrt(float[] axis, float angle) {
        mAxis = axis;
        mAngle = angle;
    }

    // 更新顶点坐标
    public synchronized float[] updateVertex(float[] oldVertex) {
        float[] newVertex = new float[oldVertex.length];
        int num = oldVertex.length / 3;
        for (int i = 0; i < num; i++) {
            int offset = i * 3;
            float[] in = new float[4];
            for (int j = 0; j < 3; j++) {
                in[j] = oldVertex[offset + j];
            }
            in[3] = 1;
            float[] out = new float[4];
            Matrix.multiplyMV(out, 0, mModelMatrix, 0, in, 0);
            for (int j = 0; j < 3; j++) {
                newVertex[offset + j] = out[j];
            }
        }
        return newVertex;
    }
}