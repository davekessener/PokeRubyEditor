package lib.tilemap;

import java.util.ArrayList;
import java.util.List;

import model.ILayer;

public class StaticLayerManager extends LayerManager
{
	private List<ILayer> mLayers;
	private int mWidth, mHeight;
	
	public StaticLayerManager(int w, int h)
	{
		mLayers = new ArrayList<>();
		mWidth = w;
		mHeight = h;
	}
	
	public void addLayer(ILayer l)
	{
		mLayers.add(l);
	}

	@Override
	public int getWidth()
	{
		return mWidth;
	}

	@Override
	public int getHeight()
	{
		return mHeight;
	}

	@Override
	public int size()
	{
		return mLayers.size();
	}

	@Override
	public ILayer getLayer(int i)
	{
		return mLayers.get(i);
	}
}
