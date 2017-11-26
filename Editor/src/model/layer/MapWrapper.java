package model.layer;

import java.util.ArrayList;
import java.util.List;

import lib.misc.Vec2;
import lib.observe.BasicObservable;
import model.Tilemap;

public class MapWrapper extends BasicObservable implements ReadOnlyLayerManager
{
	private final List<ReadOnlyLayer> mLayers;
	private final Vec2 mDimension;
	
	public MapWrapper(Tilemap tm)
	{
		mLayers = new ArrayList<>();
		mDimension = new Vec2(tm.getWidth(), tm.getHeight());
		
		for(String id : Tilemap.TYPES)
		{
			mLayers.addAll(tm.getLayers(id));
		}
	}

	@Override
	public ReadOnlyLayer get(int i)
	{
		return mLayers.get(i);
	}

	@Override
	public int size()
	{
		return mLayers.size();
	}

	@Override
	public Vec2 dimension()
	{
		return mDimension;
	}
}
