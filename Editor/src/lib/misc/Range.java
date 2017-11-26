package lib.misc;

import java.util.Iterator;

public class Range implements Iterable<Integer>
{
	private int mBegin, mEnd;
	
	private Range(int i1, int i2)
	{
		mBegin = i1;
		mEnd = i2;
	}
	
	public int getBegin() { return mBegin; }
	public int getEnd() { return mEnd; }
	public int getLength() { return mEnd - mBegin; }
	
	public void setBegin(int i)
	{
		mBegin = Math.min(mEnd, i);
		mEnd   = Math.max(mEnd, i);
	}
	
	public void setEnd(int i)
	{
		mEnd   = Math.max(mBegin, i);
		mBegin = Math.min(mBegin, i);
	}
	
	public void setLength(int l)
	{
		mEnd = mBegin + l;
	}
	
	public static Range ofLength(int start, int length)
	{
		return new Range(start, start + length);
	}
	
	public static Range of(int start, int stop)
	{
		return new Range(start, stop);
	}

	@Override
	public Iterator<Integer> iterator()
	{
		return new BasicIterator(mBegin, mEnd);
	}
	
	public static class BasicIterator implements Iterator<Integer>
	{
		private int i1, i2;
		
		public BasicIterator(int i1, int i2)
		{
			this.i1 = i1;
			this.i2 = i2;
		}

		@Override
		public boolean hasNext()
		{
			return i1 < i2;
		}

		@Override
		public Integer next()
		{
			return i1++;
		}
	}
}
