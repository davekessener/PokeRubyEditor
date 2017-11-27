package lib.tilemap.selection;

import lib.misc.Vec2;
import model.layer.MapManager;
import model.layer.ReadOnlyLayer;

public class SingleLayerSelection implements Selection
{
	private final ReadOnlyLayer mLayer;
	
	public SingleLayerSelection(ReadOnlyLayer l)
	{
		mLayer = l;
	}

	@Override
	public Vec2 dimension()
	{
		return mLayer.dimension().clone();
	}

	@Override
	public void apply(MapManager m, String id, int l, Vec2 p)
	{
		SelectionUtils.applyOnLayer(mLayer, m.getLayers(id).get(l), p);
	}
}
