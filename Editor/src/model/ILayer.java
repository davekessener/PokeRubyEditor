package model;

public interface ILayer
{
	public abstract int getWidth();
	public abstract int getHeight();
	public abstract String get(int x, int y);
}
