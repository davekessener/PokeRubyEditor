package lib;

import java.util.Set;

import javafx.beans.property.Property;

public interface MenuManager
{
	public abstract void setHandler(String id, Runnable callback);
	public abstract void registerOption(String menuID, String optName, Property<Boolean> op);
	public abstract void setRange(String id, Consumer<String> c, Set<String> opts);
	
	public static interface Consumer<T> { public abstract void consume(T e); }
}
