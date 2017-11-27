package lib.tilemap.selection;

import lib.misc.Vec2;
import model.layer.MapManager;

public class SingleTileSelection implements Selection
{
	private final String mTile;
	
	public SingleTileSelection(String t)
	{
		mTile  = t;
	}

	@Override
	public Vec2 dimension()
	{
		return new Vec2(1, 1);
	}

	@Override
	public void apply(MapManager m, String id, int l, Vec2 p)
	{
		m.getLayers(id).get(l).set(p, mTile);
	}
}
