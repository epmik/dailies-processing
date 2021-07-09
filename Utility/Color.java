package Utility;

import Procedural.RandomGenerator;
import Procedural.Interfaces.IRandomGenerator;

public class Color
{
  private static final double ByteToFloatFactor = 0.0039215686274509803921568627451;
  private static IRandomGenerator RandomGenerator = new RandomGenerator();
  private static ColorPicker DefaultColorPicker = new ColorPicker();
  
  public static double Hue(int color) 
  {
    var hsb = IntToHsba(color);

    return hsb[0];
  }
  
  public static double Saturation(int color) 
  {
    var hsb = IntToHsba(color);

    return hsb[1];
  }
  
  public static double Brightness(int color) 
  {
    var hsb = IntToHsba(color);

    return hsb[2];
  }
  
  public static double Alpha(int color) 
  {
    var hsb = IntToHsba(color);

    return hsb[3];
  }
  
  public static double[] IntToHsba(int color) 
  {
    var rgba = IntToRgba(color);

    return RgbaToHsba(rgba[0], rgba[1], rgba[2], rgba[3]);
  }
    
  public static double[] RgbaToHsba(int [] rgba) 
  {
    return RgbaToHsba(rgba[0], rgba[1], rgba[2], rgba[3]);
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
      return HsbToInt(hue, saturation, brightness, 1.0);
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
     * @param     alpha   the alpha value of the color
     * @return    the RGB value of the color with the indicated hue,
     *                            saturation, and brightness.
     * @see       java.awt.Color#getRGB()
     * @see       java.awt.Color#Color(int)
     * @see       java.awt.image.ColorModel#getRGBdefault()
     * @since     JDK1.0
     */
    public static int HsbToInt(double hue, double saturation, double brightness, double alpha) 
    {
      // mostly copied from
      // http://hg.openjdk.java.net/jdk8/jdk8/jdk/file/687fd7c7986d/src/share/classes/java/awt/Color.java

      int r = 0, g = 0, b = 0, a = (int)(alpha * 255);

      if (saturation == 0) 
      {
        r = g = b = (int) (brightness * 255.0 + 0.5);
      } 
      else 
      {
        double h = (hue - java.lang.Math.floor(hue)) * 6.0;
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
      return RgbToInt(r, g, b, a);
  }

  public static double[] RgbaToHsba(int r, int g, int b, int a) 
  {
    // mostly copied from
    // http://hg.openjdk.java.net/jdk8/jdk8/jdk/file/687fd7c7986d/src/share/classes/java/awt/Color.java

    double hue, saturation, brightness, alpha = (double)a / 255.0;
    var hsbvals = new double[4];

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
    hsbvals[3] = alpha;

    return hsbvals;
}  

  public static double[] RgbaToDouble(int r, int g, int b, int a) 
  {
    return RgbaToDouble(RgbToInt(r, g, b, a));
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

  public static int RgbToInt(int r, int g, int b, int a) 
  {
    return (int)(
      a << 24 |
      r << 16 |
      g << 8 |
      b);
  }

  public static int RgbToInt(float r, float g, float b, float a) 
  {
    return RgbToInt(r * 255, g * 255, b * 255, a * 255);
  }

  public static int RgbToInt(double r, double g, double b, double a) 
  {
    return RgbToInt(r * 255, g * 255, b * 255, a * 255);
  }

  public static int RgbToInt(int r, int g, int b) 
  {
    return RgbToInt(r, g, b, 255);
  }

  public static int RgbToInt(float r, float g, float b) 
  {
    return RgbToInt(r, g, b, 1.0F);
  }

  public static int RgbToInt(double r, double g, double b) 
  {
    return RgbToInt(r, g, b, 1.0);
  }

  public static double HueAdjust(double hue, int degrees) 
  {
    return HueAdjust(hue, DegreesToDouble(degrees));
  }

  public static double HueAdjust(double hue, int mindegrees, int maxdegrees) 
  {
    return HueAdjust(hue, DegreesToDouble(RandomGenerator.Value(mindegrees, maxdegrees)));
  }

  public static double HueAdjust(double hue, double factor) 
  {
    return WrapHue(hue + factor);
  }

  public static double HueAdjust(double hue, double minfactor, double maxfactor) 
  {
    return WrapHue(hue + RandomGenerator.Value(minfactor, maxfactor));
  }

  public static double BrightnessAdjust(double brightness, int percentage) 
  {
    return BrightnessAdjust(brightness, (double)percentage * 0.01);
  }

  public static double BrightnessAdjust(double brightness, double percentage) 
  {
    return ClampBrightness(brightness + percentage);
  }

  public static double BrightnessAdjust(double brightness, double minpercentage, double maxpercentage) 
  {
    return ClampBrightness(brightness + RandomGenerator.Value(minpercentage, maxpercentage));
  }

  public static double SaturationAdjust(double saturation, int percentage) 
  {
    return SaturationAdjust(saturation, (double)percentage * 0.01);
  }

  public static double SaturationAdjust(double saturation, double percentage) 
  {
    return ClampSaturation(saturation + percentage);
  }

  public static double SaturationAdjust(double saturation, double minpercentage, double maxpercentage) 
  {
    return ClampSaturation(saturation + RandomGenerator.Value(minpercentage, maxpercentage));
  }

  private static double DegreesToDoubleFactor = 1.0 / 360.0;

  public static double DegreesToDouble(int degrees) 
  {
    return (double)degrees * DegreesToDoubleFactor;
  }
  
  public static double WrapHue(int degrees) 
  {
    if (degrees < 0) {
      return WrapHue(degrees + 360);
    }
    if (degrees > 359) {
      return WrapHue(degrees - 360);
    }
    return degrees;
  }
  
  public static double WrapHue(double value) 
  {
    if (value < 0.0) {
      return WrapHue(value + 1.0);
    }
    if (value >= 1.0) {
      return WrapHue(value - 1.0);
    }
    return value;
  }
  
  public static double ClampSaturation(int percentage) 
  {
    if (percentage < 0) {
      return 0;
    }
    if (percentage > 100) {
      return 100;
    }
    return percentage;
  }
  
  public static double ClampSaturation(double value) 
  {
    return Clamp(value);
  }
  
  public static double ClampBrightness(int percentage) 
  {
    if (percentage < 0) {
      return 0;
    }
    if (percentage > 100) {
      return 100;
    }
    return percentage;
  }
  
  public static double ClampBrightness(double value) 
  {
    return Clamp(value);
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

  public static int RandomColor()
  {
    return DefaultColorPicker.RandomColor();
  }

  // public static int RandomColor(IRandomGenerator randomGenerator)
  // {
  //   return DefaultColorPicker.RandomColor(randomGenerator);
  // }

  public static int RandomColor(int hue)
  {
    return RandomColor(hue, RandomGenerator);
  }

  public static int RandomColor(int hue, IRandomGenerator randomGenerator)
  {
    return RandomColor(Color.DegreesToDouble(hue), randomGenerator);
  }

  public static int RandomColor(double hue)
  {
    return RandomColor(hue, RandomGenerator);
  }

  public static int RandomColor(double hue, IRandomGenerator randomGenerator)
  {
    return HsbToInt(hue, RandomSaturation(randomGenerator), RandomBrightness(randomGenerator), 1.0);
  }

  public static int RandomColor(int saturation, int brightness)
  {
    return RandomColor(saturation, brightness, RandomGenerator);
  }

  public static int RandomColor(int saturation, int brightness, IRandomGenerator randomGenerator)
  {
    return RandomColor(saturation * 0.01, brightness * 0.01, randomGenerator);
  }

  public static int RandomColor(double saturation, double brightness)
  {
    return RandomColor(saturation, brightness, RandomGenerator);
  }

  public static int RandomColor(double saturation, double brightness, IRandomGenerator randomGenerator)
  {
    return HsbToInt(RandomHue(randomGenerator), saturation, brightness, 1.0);
  }

  public static double RandomHue(IRandomGenerator randomGenerator) {
    return randomGenerator.Value();
  }

  public static double RandomSaturation(IRandomGenerator randomGenerator) {
    return randomGenerator.Value();
  }

  public static double RandomBrightness(IRandomGenerator randomGenerator) {
    return randomGenerator.Value();
  }

  public static double RandomAlpha(IRandomGenerator randomGenerator) {
    return randomGenerator.Value();
  }

  public double RandomHue() {
    return RandomHue(RandomGenerator);
  }

  public double RandomSaturation() {
    return RandomSaturation(RandomGenerator);
  }

  public double RandomBrightness() {
    return RandomBrightness(RandomGenerator);
  }

  public double RandomAlpha() {
    return RandomAlpha(RandomGenerator);
  }


  // public static int RandomColorFromSb(double s, double b)
  // {
  //   return ColorPicker.RandomColorFromSb(s, b);
  // }

  // public static int RandomColorFromSb(double s, double b, IRandomGenerator randomGenerator)
  // {
  //   return ColorPicker.RandomColorFromSb(s, b, randomGenerator);
  // }

  // public static int RandomFromH(int hue, int variation)
  // {
  //   return ColorPicker.RandomFromH(hue, variation);
  // }

  // public static int RandomFromH(int hue, int variation, IRandomGenerator randomGenerator)
  // {
  //   return ColorPicker.RandomFromH(hue, variation, randomGenerator);
  // }

  // public static int RandomFromH(double hue, double variation)
  // {
  //   return ColorPicker.RandomFromH(hue, variation);
  // }

  // public static int RandomFromH(double hue, double variation, IRandomGenerator randomGenerator)
  // {
  //   return ColorPicker.RandomFromH(hue, variation, randomGenerator);
  // }
}