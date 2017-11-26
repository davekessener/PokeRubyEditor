package model.layer;

import java.util.function.Function;

import lib.misc.Vec2;

public class LayerWrapper implements ReadOnlyLayer
{
	private final int mWidth, mHeight;
	private final Function<Vec2, String> mLookup;
	
	public LayerWrapper(int w, int h, Function<Vec2, String> l)
	{
		mWidth = w;
		mHeight = h;
		mLookup = l;
	}

	@Override
	public Vec2 dimension()
	{
		return new Vec2(mWidth, mHeight);
	}

	@Override
	public String get(Vec2 p)
	{
		return mLookup.apply(p);
	}
}
