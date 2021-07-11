package Dailies.Daily202107;

public abstract interface ICreateExplodableListener<TExplodable extends IExplodable, TExplodableComponent extends IExplodableComponent>
{
  abstract TExplodable Create(TExplodableComponent component);
}
