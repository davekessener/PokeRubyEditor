package lib.tilemap.selection;

import lib.misc.Vec2;
import model.Tilemap;
import model.layer.LayerManager;
import model.layer.MapManager;
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
	public void apply(MapManager m, String id, int l, Vec2 p)
	{
		if(m.size() != mManager.size())
		{
			throw new RuntimeException("Invalid layer count!");
		}
		
		int idx = 0;
		
		for(String lid : Tilemap.TYPES)
		{
			LayerManager ll = m.getLayers(lid);
			
			for(int i = 0 ; i < ll.size() ; ++i, ++idx)
			{
				SelectionUtils.applyOnLayer(mManager.get(idx), ll.get(i), p);
			}
		}
	}
}
