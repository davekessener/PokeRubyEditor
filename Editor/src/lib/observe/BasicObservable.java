package lib.observe;

import java.util.HashSet;
import java.util.Set;

public abstract class BasicObservable implements Observable
{
	private Set<Observer> mObservers = new HashSet<>();
	private boolean mSuspended = false;
	
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
	
	@Override
	public void change()
	{
		if(!mSuspended)
		{
			mSuspended = true;
			
			for(Observer o : mObservers)
			{
				o.onChange(this);
			}
			
			mSuspended = false;
		}
	}
}
