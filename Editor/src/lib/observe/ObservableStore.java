package lib.observe;

import java.lang.reflect.Proxy;
import java.util.function.Function;

import lib.store.BasicStore;
import lib.store.Store;

import static lib.observe.IInvocationCollection.ClassCastFilter;
import static lib.observe.IInvocationCollection.DeferredInvocation;

public interface ObservableStore<T> extends Observable, Store<T>
{
	public static <T, TT> ObservableStore<T> Instantiate(ObservableList<TT> l, Function<TT, T> f)
	{
		return Instantiate(new BasicStore<T>(idx -> f.apply(l.get(idx)), () -> l.size()), l);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> ObservableStore<T> Instantiate(Store<T> s, Observable t)
	{
		return (ObservableStore<T>) Proxy.newProxyInstance(
				ObservableStore.class.getClassLoader(),
				new Class<?>[] { ObservableStore.class },
				new BindingInvocation(
					(new InvocationCollection())
						.addInvocation(ClassCastFilter(Store.class), DeferredInvocation(s))
						.addInvocation(ClassCastFilter(Observable.class), DeferredInvocation(t))));
	}
}
