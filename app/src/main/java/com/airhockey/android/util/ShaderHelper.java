package com.airhockey.android.util;

import android.opengl.GLES20;

import static com.airhockey.android.util.LogUtils.LOGI;
import static com.airhockey.android.util.LogUtils.LOGV;
import static com.airhockey.android.util.LogUtils.LOGW;
import static com.airhockey.android.util.LogUtils.makeLogTag;

public class ShaderHelper {
    private static final String TAG = makeLogTag(ShaderHelper.class);

    private ShaderHelper() {
    }

    /**
     * Loads and compiles a vertex shader.
     * @param shaderCode Shader source code.
     * @return OpenGL object ID.
     */
    public static int compileVertexShader(String shaderCode) {
        return compileShader(GLES20.GL_VERTEX_SHADER, shaderCode);
    }

    /**
     * Loads and compiles a fragment shader.
     * @param shaderCode Shader source code.
     * @return OpenGL object ID.
     */
    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GLES20.GL_FRAGMENT_SHADER, shaderCode);
    }

    /**
     * Compiles a shader.
     * @param type Shader type.
     * @param shaderCode Shader source code.
     * @return Shader OpenGL object id after successful load.
     */
    private static int compileShader(int type, String shaderCode) {
        // Create a new shader object.
        final int shaderObjectId = GLES20.glCreateShader(type);
        if (shaderObjectId == 0) {
            LOGW(TAG, "Could not create new shader.");
            return 0;
        }
        // Pass in the shader source.
        GLES20.glShaderSource(shaderObjectId, shaderCode);

        // Compile the shader.
        GLES20.glCompileShader(shaderObjectId);

        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shaderObjectId, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

        LOGI(TAG, "Results of compiling source: \n" + shaderCode
                + "\n:" + GLES20.glGetShaderInfoLog(shaderObjectId));

        // Verify the compile status.
        if (compileStatus[0] == 0) {
            // If it failed, delete the shader object.
            GLES20.glDeleteShader(shaderObjectId);

            LOGW(TAG, "Compilation of shader failed.");

            return 0;
        }

        return shaderObjectId;
    }

    /**
     * Creates and links program with loaded shaders.
     * @param vertexShaderId Vertex shader id.
     * @param fragmentShaderId Fragment shader id.
     * @return If program linked successfully returns its id.
     */
    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        final int programObjectId = GLES20.glCreateProgram();
        if (programObjectId == 0) {
            LOGW(TAG, "Could not create new program");
            return 0;
        }

        GLES20.glAttachShader(programObjectId, vertexShaderId);
        GLES20.glAttachShader(programObjectId, fragmentShaderId);

        GLES20.glLinkProgram(programObjectId);

        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(programObjectId, GLES20.GL_LINK_STATUS, linkStatus, 0);

        LOGV(TAG, "Results of linking program:\n"
                + GLES20.glGetProgramInfoLog(programObjectId));

        if (linkStatus[0] == 0) {
            GLES20.glDeleteProgram(programObjectId);
            LOGW(TAG, "Linking of program failed.");
            return 0;
        }

        return programObjectId;
    }
}
