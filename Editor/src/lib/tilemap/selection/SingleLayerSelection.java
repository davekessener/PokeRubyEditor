package lib.tilemap.selection;

import lib.misc.Vec2;
import model.layer.LayerManager;
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
	public void apply(LayerManager m, int idx, Vec2 p)
	{
		SelectionUtils.applyOnLayer(mLayer, m.get(idx), p);
	}
}
