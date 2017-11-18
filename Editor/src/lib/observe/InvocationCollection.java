package lib.observe;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class InvocationCollection implements IInvocationCollection
{
	private final Map<Filter, InvocationHandler> mInvocations = new HashMap<>();
	
	@Override
	public InvocationCollection addInvocation(Filter f, InvocationHandler h)
	{
		if(mInvocations.put(f, h) != null)
		{
			throw new IllegalArgumentException();
		}
		
		return this;
	}
	
	@Override
	public InvocationHandler getHandler(Object x, Method m, Object[] a)
	{
		for(final Entry<Filter, InvocationHandler> e : mInvocations.entrySet())
		{
			if(e.getKey().accept(x, m, a))
			{
				return e.getValue();
			}
		}
		
		return null;
	}
}
