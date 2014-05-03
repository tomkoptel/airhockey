package com.example.firstopenglformat.app;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class FirstOpenGLProjectRenderer implements GLSurfaceView.Renderer {
    private static final float MIN_COLOR = 0.0f;
    private static final float MAX_COLOR = 1.0f;
    private Random random = new Random();

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // Set the OpengGL viewport to fill the entire surface.
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClearColor(getRandomColorValue(),
                getRandomColorValue(),
                getRandomColorValue(), 0.0f);
        // Clear the rendering surface.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }

    private float getRandomColorValue() {
        return random.nextFloat() * (MAX_COLOR - MIN_COLOR) + MIN_COLOR;
    }
}
