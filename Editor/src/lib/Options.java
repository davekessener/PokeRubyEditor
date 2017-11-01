package lib;

import com.eclipsesource.json.JsonValue;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import model.JsonModel;

public class Options implements JsonModel
{
	private Property<Boolean> mDrawGrid;
	
	public Options()
	{
		mDrawGrid = new SimpleBooleanProperty(false);
	}
	
	public boolean getDrawGrid() { return mDrawGrid.getValue(); }
	public void setDrawGrid(boolean v) { mDrawGrid.setValue(v); }
	public Property<Boolean> drawGridProperty() { return mDrawGrid; }
	
	public void register(MenuManager mm)
	{
		mm.registerOption("options", "Draw Grid", mDrawGrid);
	}

	@Override
	public void load(JsonValue value)
	{
	}

	@Override
	public JsonValue save()
	{
		return null;
	}
}
