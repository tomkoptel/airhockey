package com.airhockey.android;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.airhockey.android.util.ShaderHelper;
import com.airhockey.android.util.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class AirHockeyRenderer implements GLSurfaceView.Renderer {
    private static final int BYTES_PER_FLOAT = 4;

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private static final String A_COLOR = "a_Color";
    private static final String A_POSITION = "a_Position";

    private final FloatBuffer verterxData;
    private final Context mContext;
    private int program;
    private int aColorLocation;
    private int aPositionLocation;

    float[] tableVerticesWithTriangles = {
            // Order of coordinates: X, Y, R, G, B
            // Triangle Fan
            0f, 0f, 1f, 1f, 1f,
            -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
            0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
            0.5f, 0.5f, 0.7f, 0.7f, 0.7f,
            -0.5f, 0.5f, 0.7f, 0.7f, 0.7f,
            -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,

            // Line 1
            -0.5f, 0f, 1f, 0f, 0f,
            0.5f, 0f, 1f, 0f, 0f,

            // Mallets
            0f, -0.25f, 0f, 0f, 1f,
            0f, 0.25f, 1f, 0f, 0f
    };

    public AirHockeyRenderer(Context context) {
        this.mContext = context;
        this.verterxData = ByteBuffer
                .allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        verterxData.put(tableVerticesWithTriangles);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        String vertexShaderSource = TextResourceReader
                .readTextFileFromResource(mContext, R.raw.simple_vertex_shader);
        String fragmentShaderSource = TextResourceReader
                .readTextFileFromResource(mContext, R.raw.simple_fragment_shader);
        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
        int program = ShaderHelper.linkProgram(vertexShader, fragmentShader);

        if (BuildConfig.DEBUG) {
            ShaderHelper.validateProgram(program);
        }

        GLES20.glUseProgram(program);

        aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR);
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);

        verterxData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT,
                GLES20.GL_FLOAT, false, STRIDE, verterxData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);

        verterxData.position(POSITION_COMPONENT_COUNT);
        GLES20.glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT,
                GLES20.GL_FLOAT, false, STRIDE, verterxData);
        GLES20.glEnableVertexAttribArray(aColorLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // Set the OpengGL viewport to fill the entire surface.
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // Clear the rendering surface.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Draw board.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);

        // Draw crossing line.
        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2);

        // Draw the first mallet blue.
        GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1);

        // Draw the second mallet red.
        GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1);
    }
}
