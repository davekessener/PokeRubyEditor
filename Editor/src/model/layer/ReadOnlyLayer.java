package model.layer;

import lib.misc.Vec2;

public interface ReadOnlyLayer
{
	public abstract Vec2 dimension();
	public abstract String get(Vec2 p);
}
