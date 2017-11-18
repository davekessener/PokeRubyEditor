package lib.observe;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Predicate;

public interface IInvocationCollection
{
	public abstract InvocationCollection addInvocation(Filter f, InvocationHandler h);
	public abstract InvocationHandler getHandler(Object x, Method m, Object[] a);
	
	public static final Filter ACCEPT_ALL = (x, m, a) -> true;
	public static Filter ClassCastFilter(Class<?> c) { return (x, m, a) -> m.getDeclaringClass().isAssignableFrom(c); }
	public static Filter MethodNameFilter(String name) { return (x, m, a) -> name.equals(m.getName()); }
	public static Filter AnyMethodNameFilter(String[] names) { return (x, m, a) -> Arrays.stream(names).anyMatch(s -> s.equalsIgnoreCase(m.getName())); }
	public static Filter GenericMethodNameFilter(Predicate<String> f) { return (x, m, a) -> f.test(m.getName()); }
	
	public static <T> Filter OnModifyFilter(T t)
	{
		return (x, m, a) -> {
			try
			{
				try
				{
					m.invoke(t, a);
				}
				catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
				{
					if(e.getCause() instanceof UnsupportedOperationException)
					{
						throw (UnsupportedOperationException) e.getCause();
					}
					else
					{
						throw new RuntimeException(e);
					}
				}
			}
			catch(UnsupportedOperationException e)
			{
				return true;
			}
			
			return false;
		};
	}
	
	public static Filter Chain(Filter ... fs)
	{
		return (x, m, a) -> {
			for(final Filter f : fs)
			{
				if(!f.accept(x, m, a))
				{
					return false;
				}
			}
			
			return true;
		};
	}
	
	public static InvocationHandler DeferredInvocation(Object o) { return (x, m, a) -> m.invoke(o, a); }
	public static <T> InvocationHandler ObjectHolder(T value) { return new Holder<T>(value); }

	public static interface Filter { public abstract boolean accept(Object x, Method m, Object[] a); }
	
	public static class Holder<T> implements InvocationHandler
	{
		private final T mValue;
		
		public Holder(T v)
		{
			mValue = v;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
		{
			return mValue;
		}
	}
}
