package lib.observe;

import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.List;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;

import static lib.observe.IInvocationCollection.ClassCastFilter;
import static lib.observe.IInvocationCollection.MethodNameFilter;
import static lib.observe.IInvocationCollection.OnModifyFilter;
import static lib.observe.IInvocationCollection.ObjectHolder;
import static lib.observe.IInvocationCollection.Chain;

public interface ObservableList<T> extends Observable, List<T>
{
	public abstract Property<Number> sizeProperty();
	
	@SuppressWarnings("unchecked")
	public static <T> ObservableList<T> Instantiate(List<T> target)
	{
		Property<Number> sizeProperty = new SimpleIntegerProperty();
		InvocationCollection collection = new InvocationCollection();
		ObservableInvocation invocationHandler = new ObservableInvocation(
			new BindingInvocation(collection),
			Chain(ClassCastFilter(List.class), OnModifyFilter(Collections.unmodifiableList(target))));
		
		collection.addInvocation(ClassCastFilter(List.class), (x, m, a) -> {
			Object r = m.invoke(target, a);
			
			target.forEach(e -> {
				if(e instanceof Observable)
				{
					((Observable) e).addObserver((Observer) x);
				}
			});
			
			return r;
		});
		collection.addInvocation(ClassCastFilter(Observer.class), (x, m, a) -> { invocationHandler.change(); return null; });
		collection.addInvocation(MethodNameFilter("sizeProperty"), ObjectHolder(sizeProperty));

		invocationHandler.addObserver(o -> sizeProperty.setValue(target.size()));
		
		return (ObservableList<T>) Proxy.newProxyInstance(
				ObservableList.class.getClassLoader(),
				new Class<?>[] { ObservableList.class, Observer.class },
				invocationHandler);
	}
}
