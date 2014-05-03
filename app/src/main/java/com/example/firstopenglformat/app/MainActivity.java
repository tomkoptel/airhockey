package com.example.firstopenglformat.app;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import org.androidannotations.annotations.EActivity;

@EActivity
public class MainActivity extends ActionBarActivity {
    private GLSurfaceView glSurfaceView;
    private boolean renderSet = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        glSurfaceView = new GLSurfaceView(this);
        // Request an OpenGL ES 2.0 compatible context.
        glSurfaceView.setEGLContextClientVersion(2);

        // Assign our renderer.
        glSurfaceView.setRenderer(new FirstOpenGLProjectRenderer());
        renderSet = true;
        setContentView(glSurfaceView);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (renderSet) {
            glSurfaceView.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (renderSet) {
            glSurfaceView.onResume();
        }
    }
}
