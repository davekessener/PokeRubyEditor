package lib.observe;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import lib.observe.IInvocationCollection.Filter;

public class ObservableInvocation extends BasicObservable implements InvocationHandler
{
	private final InvocationHandler mImplementation;
	private final Filter mChangeDetector;
	
	public ObservableInvocation(InvocationHandler s, Filter f)
	{
		mImplementation = s;
		mChangeDetector = f;
	}

	@Override
	public Object invoke(Object x, Method m, Object[] a) throws Throwable
	{
		Object r = null;
		
		if(m.getDeclaringClass().isAssignableFrom(Observable.class))
		{
			r = m.invoke(this, a);
		}
		else
		{
			r = mImplementation.invoke(x, m, a);
			
			if(mChangeDetector.accept(x, m, a))
			{
				change();
			}
		}
		
		return r;
	}
}
