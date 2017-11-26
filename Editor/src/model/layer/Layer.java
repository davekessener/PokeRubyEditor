package model.layer;

import lib.misc.Vec2;

public interface Layer extends ReadOnlyLayer
{
	public abstract void set(Vec2 p, String id);
	public abstract void resize(Vec2 v);
}
