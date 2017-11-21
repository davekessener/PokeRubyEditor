package lib.observe;

import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.Map;

import static lib.observe.IInvocationCollection.ClassCastFilter;
import static lib.observe.IInvocationCollection.OnModifyFilter;
import static lib.observe.IInvocationCollection.DeferredInvocation;
import static lib.observe.IInvocationCollection.Chain;

public interface ObservableMap<K, V> extends Map<K, V>, IObservable
{
	@SuppressWarnings("unchecked")
	public static <K, V> ObservableMap<K, V> Instantiate(Map<K, V> target)
	{
		return (ObservableMap<K, V>) Proxy.newProxyInstance(
				ObservableMap.class.getClassLoader(),
				new Class<?>[] { ObservableMap.class },
				new ObservableInvocation(new BindingInvocation(
						(new InvocationCollection())
							.addInvocation(ClassCastFilter(Map.class), DeferredInvocation(target))),
					Chain(ClassCastFilter(Map.class), OnModifyFilter(Collections.unmodifiableMap(target)))));
	}
}
