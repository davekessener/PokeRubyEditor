package lib.tilemap.selection;

import lib.misc.Vec2;
import model.layer.LayerManager;
import model.layer.ReadOnlyLayerManager;

public class MapSelection implements Selection
{
	private final ReadOnlyLayerManager mManager;
	
	public MapSelection(ReadOnlyLayerManager m)
	{
		mManager = m;
	}

	@Override
	public Vec2 dimension()
	{
		return mManager.dimension().clone();
	}

	@Override
	public void apply(LayerManager m, int l, Vec2 p)
	{
		if(m.size() != mManager.size())
		{
			throw new RuntimeException("Invalid layer count!");
		}
		
		for(int i = 0 ; i < mManager.size() ; ++i)
		{
			SelectionUtils.applyOnLayer(mManager.get(i), m.get(i), p);
		}
	}
}
