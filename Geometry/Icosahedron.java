package Geometry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Icosahedron
{
    // private MeshGeometry3D geometry;

    private int index;

    private Map<Long, Integer> middlePointIndexCache;
    public List<Point3D> Positions = new ArrayList<Point3D>();
    // private List<TriangleIndices> Faces = new ArrayList<TriangleIndices>();
    public List<Integer> TriangleIndices = new ArrayList<Integer>();

    // add vertex to mesh, fix position to be on unit sphere, return index
    public int AddVertex(Point3D p)
    {
        double length = Math.sqrt(p.X * p.X + p.Y * p.Y + p.Z * p.Z);
        
        Positions.add(new Point3D(p.X / length, p.Y / length, p.Z / length));
        
        return index++;
    }

    // return index of point in the middle of p1 and p2
    private int InsertMiddlePoint(int p1, int p2)
    {
        // first check if we have it already
        boolean firstIsSmaller = p1 < p2;
        long smallerIndex = firstIsSmaller ? p1 : p2;
        long greaterIndex = firstIsSmaller ? p2 : p1;
        long key = (smallerIndex << 32) + greaterIndex;

        var v = this.middlePointIndexCache.get(key);

        if (v != null)
        {
            return v;
        }

        // not in cache, calculate it
        Point3D point1 = Positions.get(p1);
        Point3D point2 = Positions.get(p2);
        Point3D middle = new Point3D(
            (point1.X + point2.X) / 2.0, 
            (point1.Y + point2.Y) / 2.0, 
            (point1.Z + point2.Z) / 2.0);

        // add vertex makes sure point is on unit sphere
        int i = AddVertex(middle); 

        // store it, return index
        this.middlePointIndexCache.put(key, i);

        return i;
    }

    public static Icosahedron Create(int recursionLevel)
    {
        var icosahedron = new Icosahedron();
        icosahedron.middlePointIndexCache = new HashMap<Long, Integer>();
        icosahedron.index = 0;

        // create 12 vertices of a icosahedron
        var t = (1.0 + Math.sqrt(5.0)) / 2.0;

        icosahedron.AddVertex(new Point3D(-1,  t,  0));
        icosahedron.AddVertex(new Point3D( 1,  t,  0));
        icosahedron.AddVertex(new Point3D(-1, -t,  0));
        icosahedron.AddVertex(new Point3D( 1, -t,  0));

        icosahedron.AddVertex(new Point3D( 0, -1,  t));
        icosahedron.AddVertex(new Point3D( 0,  1,  t));
        icosahedron.AddVertex(new Point3D( 0, -1, -t));
        icosahedron.AddVertex(new Point3D( 0,  1, -t));

        icosahedron.AddVertex(new Point3D( t,  0, -1));
        icosahedron.AddVertex(new Point3D( t,  0,  1));
        icosahedron.AddVertex(new Point3D(-t,  0, -1));
        icosahedron.AddVertex(new Point3D(-t,  0,  1));


        // create 20 triangles of the icosahedron
        var faces = new ArrayList<TriangleIndices>();

        // 5 faces around point 0
        faces.add(new TriangleIndices(0, 11, 5));
        faces.add(new TriangleIndices(0, 5, 1));
        faces.add(new TriangleIndices(0, 1, 7));
        faces.add(new TriangleIndices(0, 7, 10));
        faces.add(new TriangleIndices(0, 10, 11));

        // 5 adjacent faces 
        faces.add(new TriangleIndices(1, 5, 9));
        faces.add(new TriangleIndices(5, 11, 4));
        faces.add(new TriangleIndices(11, 10, 2));
        faces.add(new TriangleIndices(10, 7, 6));
        faces.add(new TriangleIndices(7, 1, 8));

        // 5 faces around point 3
        faces.add(new TriangleIndices(3, 9, 4));
        faces.add(new TriangleIndices(3, 4, 2));
        faces.add(new TriangleIndices(3, 2, 6));
        faces.add(new TriangleIndices(3, 6, 8));
        faces.add(new TriangleIndices(3, 8, 9));

        // 5 adjacent faces 
        faces.add(new TriangleIndices(4, 9, 5));
        faces.add(new TriangleIndices(2, 4, 11));
        faces.add(new TriangleIndices(6, 2, 10));
        faces.add(new TriangleIndices(8, 6, 7));
        faces.add(new TriangleIndices(9, 8, 1));


        // refine triangles
        for (int i = 0; i < recursionLevel; i++)
        {
            var faces2 = new ArrayList<TriangleIndices>();

            for (var tri : faces)
            {
                // replace triangle by 4 triangles
                int a = icosahedron.InsertMiddlePoint(tri.v1, tri.v2);
                int b = icosahedron.InsertMiddlePoint(tri.v2, tri.v3);
                int c = icosahedron.InsertMiddlePoint(tri.v3, tri.v1);

                faces2.add(new TriangleIndices(tri.v1, a, c));
                faces2.add(new TriangleIndices(tri.v2, b, a));
                faces2.add(new TriangleIndices(tri.v3, c, b));
                faces2.add(new TriangleIndices(a, b, c));
            }

            faces = faces2;
        }

        // done, now add triangles to mesh
        for (var tri : faces)
        {
            icosahedron.TriangleIndices.add(tri.v1);
            icosahedron.TriangleIndices.add(tri.v2);
            icosahedron.TriangleIndices.add(tri.v3);
        }

        return icosahedron;        
    }    
}