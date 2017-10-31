package lib;

import java.util.HashSet;
import java.util.Set;

public abstract class Observable implements IObservable
{
	private Set<Observer> mObservers = new HashSet<>();
	
	@Override
	public void addObserver(Observer o)
	{
		mObservers.add(o);
	}

	@Override
	public void deleteObserver(Observer o)
	{
		mObservers.remove(o);
	}
	
	protected void change()
	{
		for(Observer o : mObservers)
		{
			o.onChange(this);
		}
	}
}
