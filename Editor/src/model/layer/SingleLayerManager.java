package model.layer;

import lib.misc.Vec2;
import lib.observe.BasicObservable;

public class SingleLayerManager extends BasicObservable implements ReadOnlyLayerManager
{
	private ReadOnlyLayer mLayer;
	
	public SingleLayerManager(ReadOnlyLayer l)
	{
		setLayer(l);
	}
	
	public void setLayer(ReadOnlyLayer l)
	{
		if(l == null)
		{
			throw new NullPointerException();
		}
		
		mLayer = l;
		
		change();
	}

	@Override
	public ReadOnlyLayer get(int i)
	{
		return mLayer;
	}

	@Override
	public int size()
	{
		return 1;
	}

	@Override
	public Vec2 dimension()
	{
		return mLayer.dimension().clone();
	}
}
