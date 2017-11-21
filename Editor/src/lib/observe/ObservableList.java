package lib.observe;

import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.List;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;

import static lib.observe.IInvocationCollection.ClassCastFilter;
import static lib.observe.IInvocationCollection.MethodNameFilter;
import static lib.observe.IInvocationCollection.OnModifyFilter;
import static lib.observe.IInvocationCollection.DeferredInvocation;
import static lib.observe.IInvocationCollection.ObjectHolder;
import static lib.observe.IInvocationCollection.Chain;

public interface ObservableList<T> extends IObservable, List<T>
{
	public abstract Property<Number> sizeProperty();
	
	@SuppressWarnings("unchecked")
	public static <T> ObservableList<T> Instantiate(List<T> target)
	{
		Property<Number> sizeProperty = new SimpleIntegerProperty();
		ObservableInvocation invocationHandler = new ObservableInvocation(new BindingInvocation(
			(new InvocationCollection())
				.addInvocation(ClassCastFilter(List.class), DeferredInvocation(target))
				.addInvocation(MethodNameFilter("sizeProperty"), ObjectHolder(sizeProperty))),
			Chain(ClassCastFilter(List.class), OnModifyFilter(Collections.unmodifiableList(target))));
		
		invocationHandler.addObserver(o -> sizeProperty.setValue(target.size()));
		
		return (ObservableList<T>) Proxy.newProxyInstance(
				ObservableList.class.getClassLoader(),
				new Class<?>[] { ObservableList.class },
				invocationHandler);
	}
}
