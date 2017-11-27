package model.layer;

import lib.misc.Vec2;
import lib.observe.Observable;

public interface ReadOnlyLayer extends Observable
{
	public abstract Vec2 dimension();
	public abstract String get(Vec2 p);
}
