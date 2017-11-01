package lib.tilemap;

import java.util.List;

import model.ILayer;
import model.Layer;
import model.Tilemap;

public class MapLayerManager extends LayerManager
{
	private Tilemap mMap;
	private int nSize;
	
	public MapLayerManager(Tilemap map)
	{
		mMap = map;
		nSize = 0;
		
		for(String id : Tilemap.LAYERS)
		{
			nSize += mMap.getLayers(id).size();
		}
	}
	
	public void resize(int w, int h)
	{
		mMap.resize(w, h);
		change();
	}
	
	@Override public int getWidth() { return mMap.getWidth(); }
	@Override public int getHeight() { return mMap.getHeight(); }
	
	public int getLayerCount(String lid) { return mMap.getLayers(lid).size(); }
	public ILayer getLayer(String lid, int i) { return mMap.getLayers(lid).get(i); }
	
	public int getAbsoluteIndex(String id, int i)
	{
		int off = 0;
		
		for(String s : Tilemap.LAYERS)
		{
			if(s.equals(id)) break;
			off += mMap.getLayers(s).size();
		}
		
		return i < mMap.getLayers(id).size() ? i + off : -1;
	}
	
	public void setTile(String lid, int idx, int x, int y, String t)
	{
		mMap.getLayers(lid).get(idx).set(x, y, t);
		change();
	}
	
	public void addLayer(String lid, int i)
	{
		List<Layer> ls = mMap.getLayers(lid);
		
		if(i >= ls.size()) i = ls.size() - 1;
		
		ls.add(i, new Layer(mMap.getWidth(), mMap.getHeight()));
		++nSize;
		
		change();
	}
	
	public void deleteLayer(String lid, int i)
	{
		List<Layer> ls = mMap.getLayers(lid);
		
		if(i >= ls.size()) i = ls.size() - 1;
		
		ls.remove(i);
		--nSize;
		
		change();
	}

	@Override
	public int size()
	{
		return nSize;
	}

	@Override
	public ILayer getLayer(int i)
	{
		int k = 0;
		
		while(i > mMap.getLayers(Tilemap.LAYERS[k]).size() || mMap.getLayers(Tilemap.LAYERS[k]).size() == 0)
		{
			i -= mMap.getLayers(Tilemap.LAYERS[k]).size();
			++k;
		}
		
		return mMap.getLayers(Tilemap.LAYERS[k]).get(i);
	}
}
