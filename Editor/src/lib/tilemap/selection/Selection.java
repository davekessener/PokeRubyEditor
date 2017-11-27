package lib.tilemap.selection;

import lib.misc.Vec2;
import model.layer.MapManager;

public interface Selection
{
	public abstract void apply(MapManager m, String id, int l, Vec2 p);
	public abstract Vec2 dimension();
}
