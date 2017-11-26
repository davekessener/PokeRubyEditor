package model.layer;

import lib.misc.Vec2;
import lib.observe.Observable;

public interface ReadOnlyLayerManager extends Observable
{
	public abstract ReadOnlyLayer get(int i);
	public abstract int size();
	public abstract Vec2 dimension();
}
