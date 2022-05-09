package com.xujhin.box.opengl.view;

public class CubeModel {
    private final int PLANE_NUM = 6; // 面数
    private final int VERTEXT_NUM_PER_PLANE = 4; // 每个面顶点数
    private final int DIMENSION_PER_VERTEXT = 3; // 每个顶点坐标维度
    private final int DIMENSION_PER_COLOR = 4; // 每个顶点颜色维度
    private final float HALF_SIDE = 1.0f; // 立方体边长的一半
    private final float[][] COLORS = new float[][]{ // 颜色饱和度
            {0.5f, 0.5f, 0.5f, 0.5f}, // white
            {0.6f, 0.6f, 0.6f, 0.6f}, // white
            {0.7f, 0.7f, 0.7f, 0.7f}, // white
            {0.8f, 0.8f, 0.8f, 0.8f}, // white
            {0.1f, 0.1f, 0.1f, 0.1f}, // white
            {0.2f, 0.2f, 0.2f, 0.2f}, // white
    };
    // 顶点坐标数组
    private volatile float[][][] vertex = new float[PLANE_NUM][VERTEXT_NUM_PER_PLANE][DIMENSION_PER_VERTEXT];
    // 顶点颜色数组
    private float[][][] color = new float[PLANE_NUM][VERTEXT_NUM_PER_PLANE][DIMENSION_PER_COLOR];

    public CubeModel() {
        initModel();
    }

    // 初始化模型顶点坐标和颜色
    private void initModel() {
        for (int i = 0; i < 3; i++) { // 遍历三视图
            initView(i);
        }
    }

    // 初始化三视图顶点坐标和颜色
    private void initView(int direction) {
        int baseIndex = direction * 2;
        int axis = 2 - direction; // 固定的坐标轴
        for (int i = 0; i < 2; i++) {
            int planeIndex = baseIndex + i;
            float value = HALF_SIDE * (1 - 2 * i);
            initPlane(planeIndex, axis, value);
        }
    }

    // 初始化一个面顶点坐标和颜色
    private void initPlane(int planeIndex, int axis, float value) {
        float row = HALF_SIDE;
        float col = -HALF_SIDE;
        float side = 2 * HALF_SIDE;
        switch (axis) {
            case 0: // 右视图
                for (int i = 0; i < 4; i++) {
                    vertex[planeIndex][i][0] = value;
                    vertex[planeIndex][i][1] = row - side * (i / 2);
                    vertex[planeIndex][i][2] = col + side * (i % 2);
                }
                break;
            case 1: // 俯视图
                for (int i = 0; i < 4; i++) {
                    vertex[planeIndex][i][0] = col + side * (i % 2);
                    vertex[planeIndex][i][1] = value;
                    vertex[planeIndex][i][2] = row - side * (i / 2);
                }
                break;
            case 2: // 正视图
                for (int i = 0; i < 4; i++) {
                    vertex[planeIndex][i][0] = col + side * (i % 2);
                    vertex[planeIndex][i][1] = row - side * (i / 2);
                    vertex[planeIndex][i][2] = value;
                }
                break;
        }
        for (int i = 0; i < 4; i++) {
            color[planeIndex][i][0] = COLORS[planeIndex][0];
            color[planeIndex][i][1] = COLORS[planeIndex][1];
            color[planeIndex][i][2] = COLORS[planeIndex][2];
            color[planeIndex][i][3] = COLORS[planeIndex][3];
        }
    }

    // 获取顶点坐标
    public float[] getVertex() {
        int length = PLANE_NUM * VERTEXT_NUM_PER_PLANE * DIMENSION_PER_VERTEXT;
        float[] res = new float[length];
        int index = 0;
        for (int i = 0; i < PLANE_NUM; i++) {
            for (int j = 0; j < VERTEXT_NUM_PER_PLANE; j++) {
                for (int k = 0; k < DIMENSION_PER_VERTEXT; k++) {
                    res[index++] = vertex[i][j][k];
                }
            }
        }
        return res;
    }

    // 获取颜色
    public float[] getColor() {
        int length = PLANE_NUM * VERTEXT_NUM_PER_PLANE * DIMENSION_PER_COLOR;
        float[] res = new float[length];
        int index = 0;
        for (int i = 0; i < PLANE_NUM; i++) {
            for (int j = 0; j < VERTEXT_NUM_PER_PLANE; j++) {
                for (int k = 0; k < DIMENSION_PER_COLOR; k++) {
                    res[index++] = color[i][j][k];
                }
            }
        }
        return res;
    }

    public int getPlaneNum() {
        return PLANE_NUM;
    }

    // 将变换后的顶点坐标刷新到模型中
    public void updateVertex(float[] newVertex) {
        int index = 0;
        for (int i = 0; i < PLANE_NUM; i++) {
            for (int j = 0; j < VERTEXT_NUM_PER_PLANE; j++) {
                for (int k = 0; k < DIMENSION_PER_VERTEXT; k++) {
                    vertex[i][j][k] = newVertex[index++];
                }
            }
        }
    }
}
