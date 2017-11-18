package lib.observe;

import java.util.List;

import javafx.beans.property.Property;

public interface IObservableList<T> extends IObservable, List<T>
{
	public abstract Property<Number> sizeProperty();
}
