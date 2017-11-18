package lib.observe;

import java.lang.reflect.Proxy;

public interface IObservable
{
	public abstract void addObserver(Observer o);
	public abstract void deleteObserver(Observer o);

	
	@SuppressWarnings("unchecked")
	public static <T> T MakeObservable(Object o, Class<T> c)
	{
		return (T) Proxy.newProxyInstance(
				c.getClassLoader(),
				new Class<?>[] { c },
				new ObservableInvocation(new BindingInvocation(
						(new InvocationCollection()).addInvocation(
								IInvocationCollection.ClassCastFilter(c),
								IInvocationCollection.DeferredInvocation(o))),
						IInvocationCollection.ACCEPT_ALL));
	}
	
	public static void AddObserver(Object p, Observer o)
	{
		((Observable) Proxy.getInvocationHandler(p)).addObserver(o);
	}
}
