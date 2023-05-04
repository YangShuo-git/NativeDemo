package com.example.nativedemo;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TriangleRender implements GLSurfaceView.Renderer {
    private int mProgram;
    private FloatBuffer vertexBuffer;

    // CPU的数据往vPosition里塞，vPosition再传给GPU的内置变量
    private final String vertexShaderCode =
            "attribute vec4 vPosition; " +
                    "void main() {" +
                    "gl_Position = vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    // 顶点形状
    static float triangleCoords[] = {
            0.5f,  0.5f, 0.0f,  // top
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f   // bottom right
    };
    //设置颜色，依次为红绿蓝和透明通道
    float color[] = { 1.0f, 0f, 0f, 1.0f };

    public int loadShader(int type, String shaderCode){
        //根据type创建顶点着色器或者片元着色器
        int shader = GLES20.glCreateShader(type);
        //将资源加入到着色器中，并编译
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // 将背景设置为灰色
        GLES20.glClearColor(0.5f,0.5f,0.5f,1.0f);

        // CPU--->GPU  是通过ByteBuffer 实现数据从cpu到gpu的转换
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        // GPU重新整理内存
        byteBuf.order(ByteOrder.nativeOrder());

        // 在GPU中，需要将ByteBuffer转换为FloatBuffer，用以传入OpenGL ES程序
        vertexBuffer = byteBuf.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);

        // 1.创建一个顶点程序  2.将顶点程序从CPU传入GPU  3.编译顶点程序
        int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vertexShader, vertexShaderCode);
        GLES20.glCompileShader(vertexShader);

        // 1.创建一个片元程序  2.将片元程序从CPU传入GPU  3.编译片元程序
        int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShader, fragmentShaderCode);
        GLES20.glCompileShader(fragmentShader);

        // 顶点程序、片元程序，本质上都是一个在GPU中运行的可执行程序
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);

        // 链接顶点、片元程序，并且生成一个可执行的程序
        GLES20.glLinkProgram(mProgram);

//        int vertexShader =  loadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode);
//        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode);
//
//        // 创建一个空的OpenGLES程序
//        mProgram = GLES20.glCreateProgram();
//        // 将顶点着色器加入到程序
//        GLES20.glAttachShader(mProgram,vertexShader);
//        // 将片元着色器加入到程序中
//        GLES20.glAttachShader(mProgram,fragmentShader);
//        // 连接到着色器程序
//        GLES20.glLinkProgram(mProgram);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    private int mPositionHandle;
    private int mColorHandle;
    @Override
    public void onDrawFrame(GL10 gl) {
        // 将程序加入到OpenGLES2.0环境
        GLES20.glUseProgram(mProgram);

        // 获取顶点着色器的vPosition成员句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        // 启用三角形顶点的句柄  vPosition 能够允许 cpu 往 gpu写
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        // 准备三角形的坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle, 3,
                GLES20.GL_FLOAT, false,
                12, vertexBuffer);

        // 获取片元着色器的vColor成员的句柄
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        // 设置绘制三角形的颜色
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        // 绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
        // 禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
