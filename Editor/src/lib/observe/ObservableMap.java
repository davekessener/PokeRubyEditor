package lib.observe;

import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.Map;

import static lib.observe.IInvocationCollection.ClassCastFilter;
import static lib.observe.IInvocationCollection.OnModifyFilter;
import static lib.observe.IInvocationCollection.DeferredInvocation;
import static lib.observe.IInvocationCollection.Chain;

public abstract class ObservableMap<K, V> implements IObservableMap<K, V>
{
	@SuppressWarnings("unchecked")
	public static <K, V> IObservableMap<K, V> Instantiate(Map<K, V> target)
	{
		return (IObservableMap<K, V>) Proxy.newProxyInstance(
				IObservableMap.class.getClassLoader(),
				new Class<?>[] { IObservableMap.class },
				new ObservableInvocation(new BindingInvocation(
						(new InvocationCollection())
							.addInvocation(ClassCastFilter(Map.class), DeferredInvocation(target))),
					Chain(ClassCastFilter(Map.class), OnModifyFilter(Collections.unmodifiableMap(target)))));
	}
}
