package com.example.nativedemo;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_FALSE;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TEXTURE0;

public class ScreenFilter {
    private int program;
    private   int vPosition;  // 句柄  gpu中的vPosition
    private   int vCoord;
    private   int vTexture; // 采样器
    private   int vMatrix;
    private int mWidth;
    private int mHeight;
    private float[] mtx;
    // gpu顶点缓冲区
    FloatBuffer vertexBuffer; // 顶点坐标缓存区
    FloatBuffer textureBuffer;  // 纹理坐标缓冲区
    float[] VERTEX = {
            -1.0f, -1.0f,
            1.0f, -1.0f,
            -1.0f, 1.0f,
            1.0f, 1.0f
    };
    float[] TEXTURE = {
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f
    };
    public ScreenFilter(Context context) {
        // 1.处理缓冲区 vertexBuffer
        vertexBuffer = ByteBuffer.allocateDirect(4 * 4 * 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexBuffer.clear();
        vertexBuffer.put(VERTEX);

        textureBuffer = ByteBuffer.allocateDirect(4 * 4 * 2).order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        textureBuffer.clear();
        textureBuffer.put(TEXTURE);

        //  1.处理shader、program
        String vertexSharder = OpenGLUtils.readRawTextFile(context, R.raw.camera_vert);  // 读取vertexshader为字符串
        String fragSharder = OpenGLUtils.readRawTextFile(context, R.raw.camera_frag);  // 读取fragmentshader为字符串
        program = OpenGLUtils.loadProgram(vertexSharder, fragSharder);

        // 2.处理各种坐标
        // 接收顶点坐标
        vPosition = GLES20.glGetAttribLocation(program, "vPosition");
        // 接收纹理坐标，接收采样器采样图片的坐标
        vCoord = GLES20.glGetAttribLocation(program, "vCoord");
        // 采样点的坐标
        vTexture = GLES20.glGetUniformLocation(program, "vTexture");
        // 变换矩阵， 需要将原本的vCoord（01,11,00,10） 与矩阵相乘
        vMatrix = GLES20.glGetUniformLocation(program, "vMatrix");
        // 构造 的时候 给 数据  vPosition gpu 是1  不是 2
    }
    public void setSize(int width, int height) {
        mWidth = width;
        mHeight = height;

    }
    public void setTransformMatrix(float[] mtx) {
        this.mtx = mtx;
    }
    // 渲染摄像头数据
    public void onDraw(int texture) {
        /**
         * 给 vPosition 传递数据
         */
        // View 的大小
        GLES20.glViewport(0, 0, mWidth, mHeight);
        // 使用之前处理好的 program
        GLES20.glUseProgram(program);
        // 从索引位0的地方读
        vertexBuffer.position(0);
        //  index 指定要修改的通用顶点属性的索引
        //  size  指定每个通用顶点属性的组件数
        //  type  指定数组中每个组件的数据类型
        //        接受符号常量GL_FLOAT  GL_BYTE、GL_UNSIGNED_BYTE、GL_SHORT、GL_UNSIGNED_SHORT或GL_FIXED  初始值为GL_FLOAT
        //  normalized  指定在访问定点数据值时是应将其标准化（GL_TRUE）还是直接转换为定点值（GL_FALSE）
        // 目的是实现cpu与gpu的通信  1.获取缓冲区 vertexBuffer 2.获取变量的句柄 vPosition
        GLES20.glVertexAttribPointer(vPosition,2,GL_FLOAT, false,0,vertexBuffer);
        // 生效  CPU传数据到GPU，默认情况下着色器无法读取到这个数据。 需要我们启用一下才可以读取
        GLES20.glEnableVertexAttribArray(vPosition);

        /**
         * 给 vCoord 传递数据
         */
        textureBuffer.position(0);
        GLES20.glVertexAttribPointer(vCoord, 2, GLES20.GL_FLOAT, false, 0, textureBuffer);
        GLES20.glEnableVertexAttribArray(vCoord);

        // 形状就确定了
        GLES20.glActiveTexture(GL_TEXTURE0);

        // 生成一个采样器
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
        GLES20.glUniform1i(vTexture, 0);
        GLES20.glUniformMatrix4fv(vMatrix, 1, false, mtx, 0);
        // 通知画画
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);
    }
}
