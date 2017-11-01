package lib;

import javafx.beans.property.Property;

public interface MenuManager
{
	public abstract void setHandler(String id, Runnable callback);
	public abstract void registerOption(String menuID, String optName, Property<Boolean> op);
}
