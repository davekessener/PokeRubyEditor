package model.layer;

import lib.misc.Vec2;

public class BasicLayer implements Layer
{
	private int mWidth, mHeight;
	private String[][] mData;
	
	public BasicLayer(Vec2 v)
	{
		mData = null;
		resize(v);
	}
	
	public BasicLayer(Vec2 v, String[][] d)
	{
		mData = d;
		mWidth = v.getX();
		mHeight = v.getY();
	}
	
	public void fill(String v)
	{
		for(int y = 0 ; y < mHeight ; ++y)
		{
			for(int x = 0 ; x < mWidth ; ++x)
			{
				mData[x][y] = v;
			}
		}
	}
	
	@Override
	public void resize(Vec2 v)
	{
		int w = v.getX(), h = v.getY();
		String[][] d = new String[w][h];
		
		if(mData != null)
		{
			for(int y = 0 ; y < Math.min(mHeight, h) ; ++y)
			{
				for(int x = 0 ; x < Math.min(mWidth, w) ; ++x)
				{
					d[x][y] = mData[x][y];
				}
			}
		}
		
		mWidth = w;
		mHeight = h;
		mData = d;
	}
	
	@Override
	public Vec2 dimension()
	{
		return new Vec2(mWidth, mHeight);
	}
	
	@Override public String get(Vec2 p)
	{
		return mData[p.getX()][p.getY()];
	}
	
	@Override
	public void set(Vec2 p, String v)
	{
		mData[p.getX()][p.getY()] = v;
	}
}
