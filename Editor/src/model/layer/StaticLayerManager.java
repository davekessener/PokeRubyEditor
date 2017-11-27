package model.layer;

import java.util.ArrayList;
import java.util.List;

import lib.misc.Vec2;
import lib.observe.BasicObservable;

public class StaticLayerManager extends BasicObservable implements ReadOnlyLayerManager
{
	private final int mWidth, mHeight;
	private final List<ReadOnlyLayer> mLayers;
	
	public StaticLayerManager(Vec2 v)
	{
		mWidth = v.getX();
		mHeight = v.getY();
		mLayers = new ArrayList<>();
	}
	
	public void add(ReadOnlyLayer l)
	{
		if(!l.dimension().equals(dimension()))
		{
			throw new RuntimeException("Mismatching dimensions!");
		}
		
		mLayers.add(l);
		
		change();
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
		return new Vec2(mWidth, mHeight);
	}
}
