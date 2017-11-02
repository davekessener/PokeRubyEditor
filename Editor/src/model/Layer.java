package model;

import com.eclipsesource.json.JsonValue;

public class Layer implements JsonModel, ILayer
{
	private int mWidth, mHeight;
	private String[][] mData;
	
	public Layer(int w, int h)
	{
		mData = null;
		resize(w, h);
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
	
	public void resize(int w, int h)
	{
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
	
	@Override public int getWidth() { return mWidth; }
	@Override public int getHeight() { return mHeight; }
	@Override public String get(int x, int y) { return mData[x][y]; }
	public void set(int x, int y, String v) { mData[x][y] = v; }
	
	@Override
	public void load(JsonValue value)
	{
		mData = Utils.LoadStringMatrix(mWidth, mHeight, value);
	}

	@Override
	public JsonValue save()
	{
		return Utils.SaveStringMatrix(mWidth, mHeight, mData);
	}
}
