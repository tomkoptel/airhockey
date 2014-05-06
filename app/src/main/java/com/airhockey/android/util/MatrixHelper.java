package com.airhockey.android.util;

public class MatrixHelper {
    private MatrixHelper() {
    }

    /**
     * Defines a projection matrix in terms of a field of view angle, an
     * aspect ratio, and z clip planes.
     *
     * <p>| a / aspect  0   0               0            |</p>
     * <p>| 0           a   0               0            |</p>
     * <p>| 0           0   -((f+n)/(f-n))  -(2fn/(f-n)) |</p>
     * <p>| 0           0   -1              0            |</p>
     *
     * @param matrix        the float array that holds the perspective matrix.
     * @param yFovInDegrees degree for field of vision
     * @param aspect        ratio, which is equal to width/height
     * @param nearPlane     distance to the near plane and must be positive. If this is set to 1, near plane will be located at <strong>Z<strong/> of -1
     * @param farPlane      distance to the far plane. Must be positive and Greater than the distance to near plave.
     */
    public static void perspectiveM(float[] matrix, float yFovInDegrees,
                                    float aspect, float nearPlane, float farPlane) {
        final float angleInRadians = (float) (yFovInDegrees * Math.PI / 180.0);
        final float a = (float) (1.0 / Math.tan(angleInRadians / 2.0));
        matrix[0] = a / aspect;
        matrix[1] = 0f;
        matrix[2] = 0f;
        matrix[3] = 0f;

        matrix[4] = 0f;
        matrix[5] = a;
        matrix[6] = 0f;
        matrix[7] = 0f;

        matrix[8] = 0f;
        matrix[9] = 0f;
        matrix[10] = -((farPlane + nearPlane) / (farPlane - nearPlane));
        matrix[11] = -1f;

        matrix[12] = 0f;
        matrix[13] = 0f;
        matrix[14] = -((2 * farPlane * nearPlane) / (farPlane - nearPlane));
        matrix[15] = 0f;
    }
}
