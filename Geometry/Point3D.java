package Geometry;

public class Point3D {
    public float X;
    public float Y;
    public float Z;

    public Point3D(float x, float y, float z) {
        X = x;
        Y = y;
        Z = z;
    }

    public Point3D(double x, double y, double z) {
        X = (float)x;
        Y = (float)y;
        Z = (float)z;
    }
}