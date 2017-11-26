package model.layer;

import lib.misc.Vec2;
import lib.observe.BasicObservable;

public class EmptyLayerManager extends BasicObservable implements ReadOnlyLayerManager
{
	@Override
	public ReadOnlyLayer get(int i)
	{
		throw new RuntimeException("No layer!");
	}

	@Override
	public int size()
	{
		return 0;
	}

	@Override
	public Vec2 dimension()
	{
		return Vec2.ORIGIN;
	}
}
