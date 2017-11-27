package model.layer;

import lib.misc.Vec2;

public interface MapManager extends ReadOnlyLayerManager
{
	public abstract int createLayer(String id, int i);
	public abstract int deleteLayer(String id, int i);
	public abstract int getAbsoluteIndex(String id, int off);
	public abstract LayerManager getLayers(String id);
	public abstract void resizeAll(Vec2 v);
}
