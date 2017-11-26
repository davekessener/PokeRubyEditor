package lib.misc.pair;

public class StrictPair<T1, T2> extends BasicPair<T1, T2>
{
	public StrictPair() { super(); }
	public StrictPair(T1 t1, T2 t2)
	{
		super();
		
		setFirst(t1);
		setSecond(t2);
	}
	
	@Override
	public void setFirst(T1 v)
	{
		if(v == null)
		{
			throw new NullPointerException();
		}
		
		super.setFirst(v);
	}
	
	@Override
	public void setSecond(T2 v)
	{
		if(v == null)
		{
			throw new NullPointerException();
		}
		
		super.setSecond(v);
	}
}
