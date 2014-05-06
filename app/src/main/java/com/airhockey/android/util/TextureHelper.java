package com.airhockey.android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static com.airhockey.android.util.LogUtils.LOGW;
import static com.airhockey.android.util.LogUtils.makeLogTag;

public class TextureHelper {
    private static final String TAG = makeLogTag(TextureHelper.class);

    private TextureHelper() {
    }

    /**
     * Helper functions for loading image to OpenGL from resources.
     * @param context Android context.
     * @param resourceId id of loading resource.
     * @return texture object id if loaded otherwise o
     */
    public static int loadTexture(Context context, int resourceId) {
        final int[] textureObjectIds = new int[1];
        GLES20.glGenTextures(1, textureObjectIds, 0);

        if (textureObjectIds[0] == 0) {
            LOGW(TAG, "Could not generate a new OpenGL texture object.");
            return 0;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        final Bitmap bitmap = BitmapFactory.decodeResource(
                context.getResources(), resourceId, options);

        if (bitmap == null) {
            LOGW(TAG, "Resource ID " + resourceId + " could not be decoded");
            GLES20.glDeleteTextures(1, textureObjectIds, 0);
            return 0;
        }

        // Bind to the texture in OpenGL
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjectIds[0]);

        // Set options for textures minification case
        GLES20.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        // Set options for textures magnification case
        GLES20.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        // Loading bitmap data in OpenGL.
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);

        // Immediate call to Mipmap generation.
        GLES20.glGenerateMipmap(GL_TEXTURE_2D);

        // Free resources of loaded bitmap.
        // Note: Following code may cause an error to be reported in the
        // ADB log as follows: E/IMGSRV(20095): :0: HardwareMipGen:
        // Failed to generate texture mipmap levels (error=3)
        // No OpenGL error will be encountered (glGetError() will return
        // 0). If this happens, just squash the source image to be
        // square. It will look the same because of texture coordinates,
        // and mipmap generation will work.
        bitmap.recycle();

        // Unbind textures.
        GLES20.glBindTexture(GL_TEXTURE_2D, 0);

        return textureObjectIds[0];
    }
}
