package lib.misc.pair;

public class BasicReadOnlyPair<T1, T2> implements ReadOnlyPair<T1, T2>
{
	private final T1 mFirst;
	private final T2 mSecond;
	
	public BasicReadOnlyPair(T1 t1, T2 t2)
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
}
