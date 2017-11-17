package lib.observe;

import java.lang.reflect.Proxy;
import java.util.Map;

public interface ObservableMap<K, V> extends Map<K, V>, IObservable
{
	@SuppressWarnings("unchecked")
	public static <K, V> ObservableMap<K, V> Instantiate(Map<K, V> target)
	{
		return (ObservableMap<K, V>) Proxy.newProxyInstance(
				ObservableMap.class.getClassLoader(),
				new Class<?>[] { ObservableMap.class },
				new BindingInvocation(target, Map.class));
	}
}
