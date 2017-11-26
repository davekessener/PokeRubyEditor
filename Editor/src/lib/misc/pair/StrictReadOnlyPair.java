package lib.misc.pair;

public class StrictReadOnlyPair<T1, T2> extends BasicReadOnlyPair<T1, T2>
{
	public StrictReadOnlyPair(T1 t1, T2 t2)
	{
		super(t1, t2);
		
		if(t1 == null || t2 == null)
		{
			throw new NullPointerException();
		}
	}
}
