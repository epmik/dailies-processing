package Utility;

public class Easing 
{
    public interface IEasingFunction {
        double Ease(double time);
    }
    
    private static Easing.IEasingFunction[] _allFunctions = new IEasingFunction[] { 
        Easing::Linear,
        
        Easing::InQuadratic,
        Easing::InCubic,
        Easing::InQuartic,
        Easing::InQuintic,
        Easing::InExpo,
        Easing::InSine,
        Easing::InCircular,
        Easing::InBack,
        Easing::InElastic,
        Easing::InBounce,
        
        Easing::OutQuadratic,
        Easing::OutCubic,
        Easing::OutQuartic,
        Easing::OutQuintic,
        Easing::OutExpo,
        Easing::OutSine,
        Easing::OutCircular,
        Easing::OutBack,
        Easing::OutElastic,
        Easing::OutBounce,
        
        Easing::InOutQuadratic,
        Easing::InOutCubic,
        Easing::InOutQuartic,
        Easing::InOutQuintic,
        Easing::InOutExpo,
        Easing::InOutSine,
        Easing::InOutCircular,
        Easing::InOutBack,
        Easing::InOutElastic,
        Easing::InOutBounce,
    };

    private static Easing.IEasingFunction[] _inFunctions = new IEasingFunction[] { 
        Easing::InQuadratic,
        Easing::InCubic,
        Easing::InQuartic,
        Easing::InQuintic,
        Easing::InExpo,
        Easing::InSine,
        Easing::InCircular,
        Easing::InBack,
        Easing::InElastic,
        Easing::InBounce,
    };

    private static Easing.IEasingFunction[] _outFunctions = new IEasingFunction[] { 
        Easing::OutQuadratic,
        Easing::OutCubic,
        Easing::OutQuartic,
        Easing::OutQuintic,
        Easing::OutExpo,
        Easing::OutSine,
        Easing::OutCircular,
        Easing::OutBack,
        Easing::OutElastic,
        Easing::OutBounce,
    };

    private static Easing.IEasingFunction[] _inOutFunctions = new IEasingFunction[] {
        Easing::InOutQuadratic,
        Easing::InOutCubic,
        Easing::InOutQuartic,
        Easing::InOutQuintic,
        Easing::InOutExpo,
        Easing::InOutSine,
        Easing::InOutCircular,
        Easing::InOutBack,
        Easing::InOutElastic,
        Easing::InOutBounce,
    };

    public static Easing.IEasingFunction[] Functions()
    {
        return _allFunctions;
    }

    public static Easing.IEasingFunction[] InFunctions()
    {
        return _inFunctions;
    }

    public static Easing.IEasingFunction[] OutFunctions()
    {
        return _outFunctions;
    }

    public static Easing.IEasingFunction[] InOutFunctions()
    {
        return _inOutFunctions;
    }

    // static Easing()
    // {

    // }

    // http://gizma.com/easing/
    // https://github.com/jesusgollonet/processing-penner-easing/blob/master/src/Linear.java
    // http://vitiy.info/easing-functions-for-your-animations/
    // https://joshondesign.com/2013/03/01/improvedEasingEquations
    // https://gist.github.com/gre/1650294

    // https://easings.net/ !!!!
    
    public static double Linear(double time){
        return time;
    }
    
    public static double Linear(double time, double from, double to){
        return from + (to - from) * time;
    }




    // quadratic easing in - accelerating from zero velocity
    public static double InQuadratic (double time) {
        return time * time;
    };		

    // quadratic easing out - decelerating to zero velocity
    public static double OutQuadratic (double time) {
        return 1 - InQuadratic(1-time);
    };        

    // quadratic easing in/out - acceleration until halfway, then deceleration
    public static double InOutQuadratic (double time) {
        if(time < 0.5) 
        {
            return InQuadratic(time*2.0)/2.0;
        }
        return 1-InQuadratic((1-time)*2)/2;            
    };




    // cubic easing in - accelerating from zero velocity
    public static double InCubic (double time) {
        return java.lang.Math.pow(time, 3);
    };        

    // cubic easing out - decelerating to zero velocity
    public static double OutCubic (double time) {
        return 1 - InCubic(1-time);
    };

    // cubic easing in/out - acceleration until halfway, then deceleration
    public static double InOutCubic (double time) {
        if(time < 0.5) 
        {
            return InCubic(time*2.0)/2.0;
        }
        return 1-InCubic((1-time)*2)/2;            
    };
    
    


    // quartic easing in - accelerating from zero velocity
    public static double InQuartic (double time) {
        return java.lang.Math.pow(time, 4);
    };

    // quartic easing out - decelerating to zero velocity
    public static double OutQuartic (double time) {
        return 1 - InQuartic(1-time);
    };		

    // quartic easing in/out - acceleration until halfway, then deceleration
    public static double InOutQuartic (double time) {
        if(time < 0.5) 
        {
            return InQuartic(time*2.0)/2.0;
        }
        return 1-InQuartic((1-time)*2)/2;            
    };




    // quintic easing in - accelerating from zero velocity
    public static double InQuintic (double time) {
        return java.lang.Math.pow(time, 5);
    };        

    // quintic easing out - decelerating to zero velocity
    public static double OutQuintic (double time) {
        return 1 - InQuintic(1-time);
    };            

    // quintic easing in/out - acceleration until halfway, then deceleration
    public static double InOutQuintic (double time) {
        if(time < 0.5) 
        {
            return InQuintic(time*2.0)/2.0;
        }
        return 1-InQuintic((1-time)*2)/2;            
    };
		



    public static double InExpo (double time) {
        return time == 0 ? 0 : java.lang.Math.pow(2, 10 * time - 10);
    };

    public static double OutExpo (double time) {
        return 1 - InExpo(1-time);
    };

    public static double InOutExpo (double time) {
        if(time < 0.5) 
        {
            return InExpo(time*2.0)/2.0;
        }
        return 1-InExpo((1-time)*2)/2;            
    };



    // sinusoidal easing in - accelerating from zero velocity
    public static double InSine (double time) {
        return -java.lang.Math.cos(time/1 * (Math.Pi/2)) + 1;
    };

    // sinusoidal easing out - decelerating to zero velocity
    public static double OutSine (double time) {
        return java.lang.Math.sin(time/1 * (Math.Pi/2));
    };

    // sinusoidal easing out - decelerating to zero velocity
    public static double InOutSine (double time) {
        return -(java.lang.Math.cos(Math.Pi * time) - 1) / 2;
    };




    public static double InCircular (double time) {
        return 1 - java.lang.Math.sqrt(1 - java.lang.Math.pow(time, 2));
    };

    public static double OutCircular (double time) {
        return 1 - InCircular(1-time);
    };

    public static double InOutCircular (double time) {
        if(time < 0.5) 
        {
            return InCircular(time*2.0)/2.0;
        }
        return 1-InCircular((1-time)*2)/2;            
    };




    public static double InBack (double time) {
        final double c1 = 1.70158;
        final double c3 = c1 + 1;
        
        return c3 * time * time * time - c1 * time * time;
    };

    public static double OutBack (double time) {
        return 1 - InBack(1-time);
    };

    public static double InOutBack (double time) {
        if(time < 0.5) 
        {
            return InBack(time*2.0)/2.0;
        }
        return 1-InBack((1-time)*2)/2;            
    };




    public static double InElastic (double time) {
        final double c4 = (2 * Math.Pi) / 3;

        return time == 0
            ? 0
            : time == 1
            ? 1
            : -java.lang.Math.pow(2, 10 * time - 10) * java.lang.Math.sin((time * 10 - 10.75) * c4);
    };

    public static double OutElastic (double time) {
        return 1 - InElastic(1-time);
    };

    public static double InOutElastic (double time) {
        if(time < 0.5) 
        {
            return InElastic(time*2.0)/2.0;
        }
        return 1-InElastic((1-time)*2)/2;            
    };




    public static double InBounce (double time) {
        return 1 - OutBounce(1 - time);
    };

    public static double OutBounce (double time) {
        final double n1 = 7.5625;
        final double d1 = 2.75;

        if (time < 1 / d1) {
            return n1 * time * time;
        } else if (time < 2 / d1) {
            return n1 * (time -= 1.5 / d1) * time + 0.75;
        } else if (time < 2.5 / d1) {
            return n1 * (time -= 2.25 / d1) * time + 0.9375;
        } else {
            return n1 * (time -= 2.625 / d1) * time + 0.984375;
        }
    };

    public static double InOutBounce (double time) {
        return time < 0.5
            ? (1 - OutBounce(1 - 2 * time)) / 2
            : (1 + OutBounce(2 * time - 1)) / 2;   
    };



    // public static double EaseInOutBounce (double time) {
    //     if(time < 0.5) 
    //     {
    //         return EaseOutBounce(time*2.0)/2.0;
    //     }
    //     return 1-EaseOutBounce((1-time)*2)/2;            
    // };

    // public static double PikeSine (double time) {
    //     if(time < 0.5) 
    //     {
    //         return EaseInSine(time*2.0);
    //     }
    //     return EaseInSine((1-time)*2);            
    // };

    // public static double PikeCosine (double time) {
    //     if(time < 0.5) 
    //     {
    //         return EaseOutSine(time*2.0);
    //     }
    //     return EaseOutSine((1-time)*2);            
    // };

            

    // // exponential easing in - accelerating from zero velocity
    // public static double EaseInExpo (double time, double min, double length, double duration) {
    //     return c * Math.pow( 2, 10 * (t/d - 1) ) + b;
    // };


    // // exponential easing out - decelerating to zero velocity
    // public static double EaseOutExpo (double time, double min, double length, double duration) {
    //     return c * ( -Math.pow( 2, -10 * t/d ) + 1 ) + b;
    // };
            

    // // exponential easing in/out - accelerating until halfway, then decelerating
    // public static double EaseInOutExpo (double time, double min, double length, double duration) {
    //     t /= d/2;
    //     if (t < 1) return c/2 * Math.pow( 2, 10 * (t - 1) ) + b;
    //     t--;
    //     return c/2 * ( -Math.pow( 2, -10 * t) + 2 ) + b;
    // };
            

    // // circular easing in - accelerating from zero velocity
    // public static double EaseInCirc (double time, double min, double length, double duration) {
    //     t /= d;
    //     return -c * (Math.sqrt(1 - t*t) - 1) + b;
    // };
            

    // // circular easing out - decelerating to zero velocity
    // public static double EaseOutCirc (double time, double min, double length, double duration) {
    //     t /= d;
    //     t--;
    //     return c * Math.sqrt(1 - t*t) + b;
    // };
            

    // // circular easing in/out - acceleration until halfway, then deceleration
    // public static double EaseInOutCirc (double time, double min, double length, double duration) {
    //     t /= d/2;
    //     if (t < 1) return -c/2 * (Math.sqrt(1 - t*t) - 1) + b;
    //     t -= 2;
    //     return c/2 * (Math.sqrt(1 - t*t) + 1) + b;
    // };

}