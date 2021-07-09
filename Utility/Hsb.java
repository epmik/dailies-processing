package Utility;

public class Hsb 
{
  public double H;
  public double S;
  public double B;
  public double A;

  public static Hsb ToHsb(int color) 
  {
    var rgba = Color20210703.IntToRgba(color);

    return ToHsb(rgba[0], rgba[1], rgba[2], rgba[3]);
  }

  public static Hsb ToHsb(int r, int g, int b) 
  {
    return ToHsb(r, g, b, 255);
  }

  public static Hsb ToHsb(int r, int g, int b, int a) 
  {
      // mostly copied from
      // http://hg.openjdk.java.net/jdk8/jdk8/jdk/file/687fd7c7986d/src/share/classes/java/awt/Color.java

      var hsb = new Hsb();
  
      // double hue, saturation, brightness;
      // var hsbvals = new double[3];
  
      int cmax = (r > g) ? r : g;
      if (b > cmax) cmax = b;
      int cmin = (r < g) ? r : g;
      if (b < cmin) cmin = b;
  
      hsb.B = ((double) cmax) / 255.0f;
      if (cmax != 0)
        hsb.S = ((double) (cmax - cmin)) / ((double) cmax);
      else
        hsb.S = 0;
      if (hsb.S == 0)
        hsb.H = 0;
      else {
        double redc = ((double) (cmax - r)) / ((double) (cmax - cmin));
        double greenc = ((double) (cmax - g)) / ((double) (cmax - cmin));
        double bluec = ((double) (cmax - b)) / ((double) (cmax - cmin));
          if (r == cmax)
            hsb.H = bluec - greenc;
          else if (g == cmax)
            hsb.H = 2.0f + redc - bluec;
          else
            hsb.H = 4.0f + greenc - redc;
          hsb.H = hsb.H / 6.0f;
          if (hsb.H < 0)
            hsb.H = hsb.H + 1.0f;
      }
  
      return hsb;
    }

  public static Hsb ToHsb(float r, float g, float b) 
  {
    return ToHsb((double)r, (double)g, (double)b, 1);
  }

  public static Hsb ToHsb(float r, float g, float b, float a) 
  {
    return ToHsb(r, g, b, 1F);
  }

  public static Hsb ToHsb(double r, double g, double b) 
  {
    return ToHsb(r, g, b, 1D);
  }

  public static Hsb ToHsb(double r, double g, double b, double a) 
  {
    return ToHsb((int) (r * 255), (int) (g * 255), (int) (b * 255), (int) (a * 255));
  }
  
  // public static Hsb Interpolate(double t, Hsb a, Hsb b)
  // {
  //     // Hue interpolation
  //     float h;
  //     float d = b.H - a.H;
  //     if (a.H > b.H)
  //     {
  //         // Swap (a.H, b.H)
  //         var h3 = b.H2;
  //         b.H = a.H;
  //         a.H = h3;
  //         d = -d;
  //         t = 1 - t;
  //     }
  //     if (d > 0.5) // 180deg
  //     {
  //         a.H = a.H + 1; // 360deg
  //         h = ( a.H + t * (b.H - a.H) ) % 1; // 360deg
  //     }
  //     if (d <= 0.5) // 180deg
  //     {
  //         h = a.H + t * d
  //     }
  //     // Interpolates the rest
  //     return new Hsb
  //     (
  //         h,            // H
  //         a.s + t * (b.s-a.s),    // S
  //         a.v + t * (b.v-a.v),    // V
  //         a.a + t * (b.a-a.a)    // A
  //     );
  // }
 }