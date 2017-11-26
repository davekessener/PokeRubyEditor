package model.layer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lib.misc.Vec2;
import lib.observe.BasicObservable;
import lib.observe.ObservableLayer;
import lib.observe.ObservableList;
import model.Tilemap;

public class MapLayerManager extends BasicObservable implements LayerManager
{
	private final Tilemap mTilemap;
	private final Map<String, List<Layer>> mCache;
	
	public MapLayerManager(Tilemap tm)
	{
		mTilemap = tm;
		mCache = new HashMap<>();
	}
	
	public int createLayer(String id, int i)
	{
		List<Layer> ll = getLayers(id);

		if(i < 0) i = 0;
		if(i > ll.size()) i = ll.size();
				
		ll.add(i, new BasicLayer(dimension()));
		
		return i;
	}
	
	public int deleteLayer(String id, int i)
	{
		if(i < 0) return 0;
		
		List<Layer> ll = getLayers(id);
		
		if(i >= ll.size()) return ll.size() - 1;
		
		ll.remove(i);
		
		return i >= ll.size() ? ll.size() - 1 : i;
	}
	
	public int getAbsoluteIndex(String id, int off)
	{
		for(String s : Tilemap.TYPES)
		{
			if(id.equals(s)) break;
			
			off += mTilemap.getLayers(s).size();
		}
		
		return off;
	}

	@Override
	public Layer get(int i)
	{
		List<Layer> ll = null;
		
		for(String id : Tilemap.TYPES)
		{
			ll = getLayers(id);
			
			if(i < ll.size()) break;
			
			i -= ll.size();
		}
		
		if(i >= ll.size())
		{
			throw new RuntimeException("Invalid layer index!");
		}
		
		return ll.get(i);
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
	
	public List<Layer> getLayers(String id)
	{
		List<Layer> ll = mCache.get(id);
		
		if(ll == null)
		{
			ObservableList<Layer> o = ObservableList.Instantiate(new ArrayList<Layer>());
			
			for(Layer l : mTilemap.getLayers(id))
			{
				o.add(new ObservableLayer(l));
			}
			
			o.addObserver(ob -> change());
			
			mCache.put(id, ll = o);
		}
		
		return ll;
	}
	
	public void resizeAll(Vec2 v)
	{
		for(int i1 = 0, i2 = size() ; i1 < i2 ; ++i1)
		{
			get(i1).resize(v);
		}
	}
}
