package Utility;

import Procedural.RandomGenerator;
import Procedural.Interfaces.IRandomGenerator;

public class Color20210703 
{
    private static final double ByteToFloatFactor = 0.0039215686274509803921568627451;

    public static double Hue(int color) 
  {
    var hsb = IntToHsb(color);

    return hsb[0];
  }
  
  public static double Saturation(int color) 
  {
    var hsb = IntToHsb(color);

    return hsb[1];
  }
  
  public static double Brightness(int color) 
  {
    var hsb = IntToHsb(color);

    return hsb[2];
  }
  
  public static double[] IntToHsb(int color) 
  {
    return RgbToHsb(IntToRgba(color));
  }
  
  public static double[] RgbToHsb(int [] rgba) 
  {
    return RgbToHsb(rgba[0], rgba[1], rgba[2]);
  }
  
    /**
     * Converts the components of a color, as specified by the HSB
     * model, to an equivalent set of values for the default RGB model.
     * <p>
     * The <code>saturation</code> and <code>brightness</code> components
     * should be floating-point values between zero and one
     * (numbers in the range 0.0-1.0).  The <code>hue</code> component
     * can be any floating-point number.  The floor of this number is
     * subtracted from it to create a fraction between 0 and 1.  This
     * fractional number is then multiplied by 360 to produce the hue
     * angle in the HSB color model.
     * <p>
     * The integer that is returned by <code>HSBtoRGB</code> encodes the
     * value of a color in bits 0-23 of an integer value that is the same
     * format used by the method {@link #getRGB() getRGB}.
     * This integer can be supplied as an argument to the
     * <code>Color</code> constructor that takes a single integer argument.
     * @param     hue   the hue component of the color
     * @param     saturation   the saturation of the color
     * @param     brightness   the brightness of the color
     * @return    the RGB value of the color with the indicated hue,
     *                            saturation, and brightness.
     * @see       java.awt.Color#getRGB()
     * @see       java.awt.Color#Color(int)
     * @see       java.awt.image.ColorModel#getRGBdefault()
     * @since     JDK1.0
     */
    public static int HsbToInt(double hue, double saturation, double brightness) 
    {
      // mostly copied from
      // http://hg.openjdk.java.net/jdk8/jdk8/jdk/file/687fd7c7986d/src/share/classes/java/awt/Color.java

      int r = 0, g = 0, b = 0;

      if (saturation == 0) 
      {
        r = g = b = (int) (brightness * 255.0 + 0.5);
      } 
      else 
      {
        double h = (hue - Math.Floor(hue)) * 6.0;
        double f = h - java.lang.Math.floor(h);
        double p = brightness * (1.0f - saturation);
        double q = brightness * (1.0f - saturation * f);
        double t = brightness * (1.0f - (saturation * (1.0f - f)));
          switch ((int) h) {
          case 0:
              r = (int) (brightness * 255.0f + 0.5f);
              g = (int) (t * 255.0f + 0.5f);
              b = (int) (p * 255.0f + 0.5f);
              break;
          case 1:
              r = (int) (q * 255.0f + 0.5f);
              g = (int) (brightness * 255.0f + 0.5f);
              b = (int) (p * 255.0f + 0.5f);
              break;
          case 2:
              r = (int) (p * 255.0f + 0.5f);
              g = (int) (brightness * 255.0f + 0.5f);
              b = (int) (t * 255.0f + 0.5f);
              break;
          case 3:
              r = (int) (p * 255.0f + 0.5f);
              g = (int) (q * 255.0f + 0.5f);
              b = (int) (brightness * 255.0f + 0.5f);
              break;
          case 4:
              r = (int) (t * 255.0f + 0.5f);
              g = (int) (p * 255.0f + 0.5f);
              b = (int) (brightness * 255.0f + 0.5f);
              break;
          case 5:
              r = (int) (brightness * 255.0f + 0.5f);
              g = (int) (p * 255.0f + 0.5f);
              b = (int) (q * 255.0f + 0.5f);
              break;
          }
      }
      return 0xff000000 | (r << 16) | (g << 8) | (b << 0);
  }

  public static double[] RgbToHsb(int r, int g, int b) 
  {
    // mostly copied from
    // http://hg.openjdk.java.net/jdk8/jdk8/jdk/file/687fd7c7986d/src/share/classes/java/awt/Color.java

    double hue, saturation, brightness;
    var hsbvals = new double[3];

    int cmax = (r > g) ? r : g;
    if (b > cmax) cmax = b;
    int cmin = (r < g) ? r : g;
    if (b < cmin) cmin = b;

    brightness = ((double) cmax) / 255.0f;
    if (cmax != 0)
        saturation = ((double) (cmax - cmin)) / ((double) cmax);
    else
        saturation = 0;
    if (saturation == 0)
        hue = 0;
    else {
      double redc = ((double) (cmax - r)) / ((double) (cmax - cmin));
      double greenc = ((double) (cmax - g)) / ((double) (cmax - cmin));
      double bluec = ((double) (cmax - b)) / ((double) (cmax - cmin));
        if (r == cmax)
            hue = bluec - greenc;
        else if (g == cmax)
            hue = 2.0f + redc - bluec;
        else
            hue = 4.0f + greenc - redc;
        hue = hue / 6.0f;
        if (hue < 0)
            hue = hue + 1.0f;
    }
    hsbvals[0] = hue;
    hsbvals[1] = saturation;
    hsbvals[2] = brightness;

    return hsbvals;
}  

  public static double[] RgbaToDouble(int r, int g, int b, int a) 
  {
    return RgbaToDouble(RgbaToInt(r, g, b, a));
  }

  public static double[] RgbaToDouble(int color) 
  {
    return new double[] {
      ByteToFloatFactor * (color >> 16 & 0xff),   // r
      ByteToFloatFactor * (color >> 8 & 0xff),    // g
      ByteToFloatFactor * (color & 0xff),         // b
      ByteToFloatFactor * (color >> 24 & 0xff),   // a
    };
  }

  public static int[] IntToRgba(int color) 
  {
    return new int[] {
      (color >> 16 & 0xff),   // r
      (color >> 8 & 0xff),    // g
      (color & 0xff),         // b
      (color >> 24 & 0xff),   // a
    };
  }

  public static int RgbaToInt(int r, int g, int b, int a) 
  {
    return (int)(
      a << 24 |
      r << 16 |
      g << 8 |
      b);
  }

  public static int RgbaToInt(float r, float g, float b, float a) 
  {
    return RgbaToInt(r * 255, g * 255, b * 255, a * 255);
  }

  private static IRandomGenerator RandomGenerator = new RandomGenerator();

  public static int Random()
  {
    return Random(RandomGenerator);
  }

  public static int Random(double s, double b)
  {
    return Random(s, b, RandomGenerator);
  }

  public static int Random(IRandomGenerator randomGenerator)
  {
    var h = randomGenerator.Value(0.0, 1.0);
    var s = randomGenerator.Value(0.0, 1.0);
    var b = randomGenerator.Value(0.0, 1.0);

    return Color20210703.HsbToInt(h, s, b);
  }

  public static int Random(double s, double b, IRandomGenerator randomGenerator)
  {
    var h = randomGenerator.Value(0.0, 1.0);

    return Color20210703.HsbToInt(h, s, b);
  }

  public static int[] Complementary(int color1)
  {
    // A complementary color scheme is a two-color combination consisting of a base color (H0) 
    // and another color (H1) that is 180 degrees apart from H0 on the color wheel.
    
    var hsb1 = Color20210703.IntToHsb(color1);

    var h2 = Wrap(hsb1[0] + DegreesToDouble(180));

    var color2 = Color20210703.HsbToInt(h2, hsb1[1], hsb1[2]);

    return new int[] { color1, color2 };
  }

  public static int[] SplitComplementary(int color1)
  {
    // A split-complementary color scheme is a three-color combination that consists of a base color (H0) and two colors (H1 and H2) 
    // that are 150 degrees and 210 degrees apart from H0 respectively.

    var hsb1 = Color20210703.IntToHsb(color1);

    var h2 = Wrap(hsb1[0] + DegreesToDouble(150));

    var color2 = Color20210703.HsbToInt(h2, hsb1[1], hsb1[2]);

    var h3 = Wrap(hsb1[0] + DegreesToDouble(210));

    var color3 = Color20210703.HsbToInt(h3, hsb1[1], hsb1[2]);

    return new int[] { color1, color2, color3 };
  }

  public static int[] Triadic(int color1)
  {
    // This is a three-color combination that consists of a base color (H0) and two colors (H1 and H2) 
    // that are 120 degrees and 240 degrees apart from H0 respectively.

    var hsb1 = Color20210703.IntToHsb(color1);

    var h2 = Wrap(hsb1[0] + DegreesToDouble(120));

    var color2 = Color20210703.HsbToInt(h2, hsb1[1], hsb1[2]);

    var h3 = Wrap(hsb1[0] + DegreesToDouble(240));

    var color3 = Color20210703.HsbToInt(h3, hsb1[1], hsb1[2]);

    return new int[] { color1, color2, color3 };
  }

  public static int[] Tetradic(int color1)
  {
    // A four-color combination that consists of a base color (H0) and three colors (H1, H2, and H3) 
    // that are 90 degrees, 180 degrees, and 270 degrees apart from H0 respectively.

    var hsb1 = Color20210703.IntToHsb(color1);

    var h2 = Wrap(hsb1[0] + DegreesToDouble(90));

    var color2 = Color20210703.HsbToInt(h2, hsb1[1], hsb1[2]);

    var h3 = Wrap(hsb1[0] + DegreesToDouble(180));

    var color3 = Color20210703.HsbToInt(h3, hsb1[1], hsb1[2]);

    var h4 = Wrap(hsb1[0] + DegreesToDouble(270));

    var color4 = Color20210703.HsbToInt(h4, hsb1[1], hsb1[2]);

    return new int[] { color1, color2, color3, color4 };
  }

  public static int[] Analagous(int color1)
  {
    return Analagous(color1, 5);
  }

  public static int[] Analagous(int color1, int numberOfColors)
  {
    return Analagous(color1, 15, numberOfColors);
  }

  public static int[] Analagous(int color1, int degreeVariation, int numberOfColors)
  {
    // Analagous color schemes use a combination consisting of a base color (H0) 
    // and one or more adjacent colors (30 degrees apart) on the color wheel.

    var d = (int)(-degreeVariation * ((numberOfColors - 1) / 2.0));

    var hsb1 = Color20210703.IntToHsb(color1);

    var colors = new int[numberOfColors];

    for (var i = 0; i < numberOfColors; i++) 
    {
      var h = Wrap(hsb1[0] + DegreesToDouble(d));

      colors[i] = Color20210703.HsbToInt(h, hsb1[1], hsb1[2]);

      d += degreeVariation;
    }

    return colors;
  }

  public static int[] Monochrome(int color1)
  {
    return Monochrome(color1, 5);
  }

  public static int[] Monochrome(int color1, int numberOfColors)
  {
    return Monochrome(color1, 0.02, 0.02, numberOfColors);
  }

  public static int[] Monochrome(int color1, double saturationVariation, double brightnessVariation, int numberOfColors)
  {
    // A Monochrome color scheme is is where all colors are derived from the base color and the hue value is not changed. 
    // Variations in shade or tint are achieved by changing the S-value and/or the B-value for your base color.

    var s = -saturationVariation * ((numberOfColors - 1) / 2.0);
    var b = -brightnessVariation * ((numberOfColors - 1) / 2.0);

    var hsb1 = Color20210703.IntToHsb(color1);

    var colors = new int[numberOfColors];

    for (var i = 0; i < numberOfColors; i++) 
    {
      colors[i] = Color20210703.HsbToInt(hsb1[0], Clamp(hsb1[1] + s), Clamp(hsb1[2] + b));
      
      s += saturationVariation;
      b += brightnessVariation;
    }

    return colors;
  }
  
  private static double DegreesToDoubleFactor = 1.0 / 360.0;

  private static double DegreesToDouble(int degrees) 
  {
    return (double)degrees * DegreesToDoubleFactor;
  }
  
  private static double Wrap(double value) 
  {
    if (value < 0.0) {
      return Wrap(value + 1.0);
    }
    if (value >= 1.0) {
      return Wrap(value - 1.0);
    }
    return value;
  }
 
  private static double Clamp(double value) 
  {
    if (value < 0.0) {
      return 0.0;
    }
    if (value > 1.0) {
      return 1.0;
    }
    return value;
  }
}