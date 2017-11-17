package lib.observe;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import javafx.util.Pair;

public class BindingInvocation extends Observable implements InvocationHandler
{
	private final List<Pair<Object, Class<?>>> mTargets;
	
	@SuppressWarnings("unchecked")
	public BindingInvocation(Object o, Class<?> c) { this(new Pair[] { new Pair<Object, Class<?>>(o, c) }); }
	public BindingInvocation(Pair<Object, Class<?>>[] os)
	{
		mTargets = Arrays.asList(os);
	}

	@Override
	public Object invoke(Object x, Method m, Object[] a) throws Throwable
	{
		Class<?> c = m.getDeclaringClass();
		
		if(c.isAssignableFrom(IObservable.class))
		{
			return m.invoke(this, a);
		}
		
		for(final Pair<Object, Class<?>> p : mTargets)
		{
			if(c.isAssignableFrom(p.getValue()))
			{
				return doInvoke(m, p.getKey(), a);
			}
		}
		
		return onUnknownTarget(x, m, a);
	}
	
	protected Object onUnknownTarget(Object x, Method m, Object[] a) throws Throwable
	{
		throw new RuntimeException("Called method " + m.getName() + " on object of type " + x.getClass().getName());
	}
	
	private Object doInvoke(Method m, Object o, Object[] a) throws Throwable
	{
		Object r = m.invoke(o, a);
		
		change();
		
		return r;
	}
}
