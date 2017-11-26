package lib.misc;

import java.util.Iterator;

public class Rect implements Iterable<Vec2>
{
	private Vec2 mStart, mStop;
	
	public Rect(int w, int h) { this(Vec2.ORIGIN, w, h); }
	public Rect(Vec2 v, int w, int h)
	{
		mStart = new Vec2(v.getX(), v.getY());
		mStop = v.add(new Vec2(w, h));
	}
	
	public Rect(Vec2 v, Vec2 u)
	{
		mStart = new Vec2(Math.min(v.getX(), u.getX()), Math.min(v.getY(), u.getY()));
		mStop = new Vec2(Math.max(v.getX(), u.getX()), Math.max(v.getY(), u.getY()));
	}
	
	public Vec2 getStart() { return mStart.clone(); }
	public Vec2 dimension() { return new Vec2(getWidth(), getHeight()); }
	public int getX() { return mStart.getX(); }
	public int getY() { return mStart.getY(); }
	public int getWidth() { return mStop.getX() - mStart.getX(); }
	public int getHeight() { return mStop.getY() - mStart.getY(); }
	
	public Vec2 getOffset() { return new Vec2(-getX(), -getY()); }

	public Range X() { return Range.of(mStart.getX(), mStop.getX()); }
	public Range Y() { return Range.of(mStart.getY(), mStop.getY()); }
	
	public boolean contains(Vec2 p)
	{
		return p.getX() >= mStart.getX() && p.getX() < mStop.getX() && p.getY() >= mStart.getY() && p.getY() < mStop.getY();
	}

	@Override
	public Iterator<Vec2> iterator()
	{
		return new Iterator2D(this);
	}
	
	public static class Iterator2D implements Iterator<Vec2>
	{
		private final int x, y;
		private final int l, w;
		private int i;
		
		public Iterator2D(Rect r)
		{
			x = r.getX();
			y = r.getY();
			w = r.getWidth();
			l = w * r.getHeight();
			i = 0;
		}

		@Override
		public boolean hasNext()
		{
			return i < l;
		}

		@Override
		public Vec2 next()
		{
			int t = i++;
			
			return new Vec2(x + t % w, y + t / w);
		}
	}
}
