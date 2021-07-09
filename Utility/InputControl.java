package Utility;

public class InputControl<T extends Number>
{
  protected int X = 24;
  protected int Y = 24;
  protected int Width = 240;
  protected int Height = 24;
  
  private T _value;

  protected T Value(T v)
  {
    _value = v;
    return _value;
  }
}