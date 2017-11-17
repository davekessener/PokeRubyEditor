package lib.observe;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;

public interface ObservableList<T> extends IObservable, List<T>
{
	public Property<Number> sizeProperty();
	
	@SuppressWarnings("unchecked")
	public static <T> ObservableList<T> Instantiate(List<T> target)
	{
		return (ObservableList<T>) Proxy.newProxyInstance(
				ObservableList.class.getClassLoader(),
				new Class<?>[] { ObservableList.class },
				new ListInvocation<>(target));
	}
	
	static class ListInvocation<T> extends BindingInvocation
	{
		private final Property<Number> mSizeProperty;

		public ListInvocation(final List<T> t)
		{
			super(t, List.class);

			mSizeProperty = new SimpleIntegerProperty();
			
			this.addObserver(x -> mSizeProperty.setValue(t.size()));
		}

		@Override
		protected Object onUnknownTarget(Object x, Method m, Object[] a) throws Throwable
		{
			if(m.getDeclaringClass().isAssignableFrom(ObservableList.class))
			{
				return mSizeProperty;
			}
			else
			{
				return super.onUnknownTarget(x, m, a);
			}
		}
	}
}
