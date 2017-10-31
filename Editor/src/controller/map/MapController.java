package controller.map;

import com.eclipsesource.json.JsonValue;

import controller.ContentController;
import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.TabPane;
import model.Map;

public class MapController implements ContentController
{
	private TabPane mRoot;
	private Property<Boolean> mChanged;
	
	private Map mMap;
	
	public MapController(Map map)
	{
		mMap = map;
	}

	@Override
	public Node getUI()
	{
		return mRoot;
	}

	@Override
	public Property<Boolean> needsSave()
	{
		return mChanged;
	}

	@Override
	public JsonValue save()
	{
		mChanged.setValue(false);
		
		return mMap.save();
	}
}
