package com.xujhin.box.opengl.base;

import android.opengl.GLES30;
import android.opengl.GLES30;
import android.util.Log;

/**
 * 形状基类
 */
public abstract class BaseGLSL {

    private static final String TAG = "BaseGLSL";

    public static final int COORDS_PER_VERTEX = 3; // 每个顶点的坐标数
    public static final int vertexStride = COORDS_PER_VERTEX * 4; // 每个顶点四个字节

    /**
     * 加载着色器
     *
     * @param type       加载着色器类型
     * @param shaderCode 加载着色器的代码
     */
    public static int loadShader(int type, String shaderCode) {
        //根据type创建顶点着色器或者片元着色器
        int shader = GLES30.glCreateShader(type);
        //将着色器的代码加入到着色器中
        GLES30.glShaderSource(shader, shaderCode);
        //编译着色器
        GLES30.glCompileShader(shader);
        return shader;
    }


    /**
     * 生成OpenGL Program
     *
     * @param vertexSource   顶点着色器代码
     * @param fragmentSource 片元着色器代码
     * @return 生成的OpenGL Program，如果为0，则表示创建失败
     */
    public static int createOpenGLProgram(String vertexSource, String fragmentSource) {
        int vertex = loadShader(GLES30.GL_VERTEX_SHADER, vertexSource);
        if (vertex == 0) {
            Log.e(TAG, "loadShader vertex failed");
            return 0;
        }
        int fragment = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentSource);
        if (fragment == 0) {
            Log.e(TAG, "loadShader fragment failed");
            return 0;
        }
        int program = GLES30.glCreateProgram();
        if (program != 0) {
            GLES30.glAttachShader(program, vertex);
            GLES30.glAttachShader(program, fragment);
            GLES30.glLinkProgram(program);
            int[] linkStatus = new int[1];
            GLES30.glGetProgramiv(program, GLES30.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] != GLES30.GL_TRUE) {
                Log.e(TAG, "Could not link program:" + GLES30.glGetProgramInfoLog(program));
                GLES30.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }

}