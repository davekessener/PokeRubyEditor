package lib.observe;

import java.lang.reflect.Proxy;

public interface Observable
{
	public abstract void addObserver(Observer o);
	public abstract void deleteObserver(Observer o);
	public abstract void change();

	
	@SuppressWarnings("unchecked")
	public static <T> T MakeObservable(Object o, Class<T> c)
	{
		if(o instanceof Observable)
		{
			return (T) o;
		}
		else
		{
			return (T) Proxy.newProxyInstance(
				c.getClassLoader(),
				new Class<?>[] { c, Observable.class },
				new ObservableInvocation(new BindingInvocation(
						(new InvocationCollection()).addInvocation(
								IInvocationCollection.ClassCastFilter(c),
								IInvocationCollection.DeferredInvocation(o))),
						IInvocationCollection.ACCEPT_ALL));
		}
	}
	
	public static void AddObserver(Object p, Observer o)
	{
		if(p instanceof Observable)
		{
			((Observable) p).addObserver(o);
		}
		else
		{
			((BasicObservable) Proxy.getInvocationHandler(p)).addObserver(o);
		}
	}
}
