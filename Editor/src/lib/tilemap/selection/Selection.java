package lib.tilemap.selection;

import lib.misc.Vec2;
import model.layer.LayerManager;

public interface Selection
{
	public abstract void apply(LayerManager m, int l, Vec2 p);
	public abstract Vec2 dimension();
}
