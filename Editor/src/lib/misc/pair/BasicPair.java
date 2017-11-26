package lib.misc.pair;

public class BasicPair<T1, T2> implements Pair<T1, T2>
{
	private T1 mFirst;
	private T2 mSecond;
	
	public BasicPair() { }
	public BasicPair(T1 t1, T2 t2)
	{
		mFirst = t1;
		mSecond = t2;
	}

	@Override
	public T1 getFirst()
	{
		return mFirst;
	}

	@Override
	public T2 getSecond()
	{
		return mSecond;
	}

	@Override
	public void setFirst(T1 v)
	{
		mFirst = v;
	}

	@Override
	public void setSecond(T2 v)
	{
		mSecond = v;
	}
}
