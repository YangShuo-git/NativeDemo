package com.example.nativedemo;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

// GLSurfaceView   代码运行在 glthread线程
public class CameraView extends GLSurfaceView {
    private  CameraRender renderer;
    public CameraView(Context context) {
        super(context);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);

        renderer = new CameraRender(this);

        setRenderer(renderer);
        /**
         *  刷新方式：
         *  RENDERMODE_WHEN_DIRTY   手动刷新，调用requestRender();
         *  RENDERMODE_CONTINUOUSLY 自动刷新，大概16ms自动回调一次onDrawFrame()
         */
        // 必须在setRenderer() 后面
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
