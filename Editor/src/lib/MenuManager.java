package lib;

import java.util.Set;
import java.util.function.Consumer;

import javafx.beans.property.Property;

public interface MenuManager
{
	public abstract void setHandler(String id, Runnable callback);
	public abstract void registerOption(String menuID, String optName, Property<Boolean> op);
	public abstract void setRange(String id, Consumer<String> c, Set<String> opts);
	public abstract void setStatus(String v);
	public abstract void setStatus(Property<String> s);
}
