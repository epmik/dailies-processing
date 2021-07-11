package Dailies.Daily202107;

import processing.core.PGraphics;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

public interface IExplodableComponent
{
    public void Update(double elapsedTime);
    public void Draw(PGraphics canvas, double elapsedTime);
    public void Reset();
    public void HandleKeyPress(KeyEvent event);
    public void HandleKeyRelease(KeyEvent event);
    public void HandleMouseRelease(MouseEvent event);
    public boolean IsTransparent();
}
