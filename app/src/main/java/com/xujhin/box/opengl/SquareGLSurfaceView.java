package com.xujhin.box.opengl;

import android.content.Context;
import com.xujhin.box.opengl.base.BaseGLSurfaceView;
import com.xujhin.box.opengl.squre.Cube;
import com.xujhin.box.opengl.squre.Square;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 绘制正方形的GLSurfaceView
 */
public class SquareGLSurfaceView extends BaseGLSurfaceView {

    public SquareGLSurfaceView(Context context) {
        super(context);

         setRenderer(new SquareRenderer()); // 绘制正方形
        // setRenderer(new CubeRenderer());  // 绘制立方体

//        setRenderer(new VaryMatrixCubeRenderer());
    }

    class SquareRenderer implements Renderer {

        Square square;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            square = new Square();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            square.onSurfaceChanged(width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            square.draw();
        }
    }

    class CubeRenderer implements Renderer {

        Cube cube;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            cube = new Cube();
            cube.onSurfaceCreated();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            cube.onSurfaceChanged(width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            cube.draw();
        }
    }


}