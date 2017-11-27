package model.layer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import lib.misc.Vec2;
import lib.misc.pair.ReadOnlyPair;
import lib.misc.pair.StrictReadOnlyPair;
import lib.observe.BasicObservable;
import model.Tilemap;

public class BasicMapLayerManager extends BasicObservable implements MapManager
{
	private final Tilemap mTilemap;
	private final Map<String, LayerManager> mCache;
	
	public BasicMapLayerManager(Tilemap tm)
	{
		mTilemap = tm;
		mCache = new HashMap<>();
	}


	@Override
	public Layer get(int i)
	{
		ReadOnlyPair<String, Integer> idx = getRelativeIndex(i);
		
		if(idx == null)
		{
			throw new RuntimeException("Invalid layer index!");
		}
		
		return getLayers(idx.getFirst()).get(idx.getSecond());
	}

	@Override
	public int size()
	{
		return Arrays.stream(Tilemap.TYPES).map(id -> getLayers(id).size()).reduce(0, (i1, i2) -> i1 + i2);
	}
	
	@Override
	public Vec2 dimension()
	{
		return new Vec2(mTilemap.getWidth(), mTilemap.getHeight());
	}
	
	// # ----------------------------------------------------------------------

	@Override
	public int createLayer(String id, int i)
	{
		LayerManager m = getLayers(id);

		if(i < 0) i = 0;
		if(i > m.size()) i = m.size();
		
		m.add(i, new BasicLayer(dimension()));
		
		return i;
	}

	@Override
	public int deleteLayer(String id, int i)
	{
		if(i < 0) return 0;
		
		LayerManager ll = getLayers(id);
		
		if(i >= ll.size()) return ll.size() - 1;
		
		ll.remove(i);
		
		return i >= ll.size() ? ll.size() - 1 : i;
	}
	
	@Override
	public LayerManager getLayers(String id)
	{
		LayerManager m = mCache.get(id);
		
		if(m == null)
		{
			mCache.put(id, m = new BasicLayerManager(mTilemap, id));
			
			m.addObserver(o -> change());
		}
		
		return m;
	}

	@Override
	public void resizeAll(Vec2 v)
	{
		mTilemap.resize(v);
	}
	
	@Override
	public int getAbsoluteIndex(String id, int off)
	{
		for(String s : Tilemap.TYPES)
		{
			if(id.equals(s)) break;
			
			off += mTilemap.getLayers(s).size();
		}
		
		return off;
	}
	
	private ReadOnlyPair<String, Integer> getRelativeIndex(int i)
	{
		for(String id : Tilemap.TYPES)
		{
			LayerManager ll = getLayers(id);
			
			if(i < ll.size())
			{
				return new StrictReadOnlyPair<>(id, i);
			}
			
			i -= ll.size();
		}
		
		return null;
	}
}
