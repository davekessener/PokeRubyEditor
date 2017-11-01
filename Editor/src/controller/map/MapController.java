package controller.map;

import controller.ContentController;
import javafx.scene.Node;
import javafx.scene.control.TabPane;
import model.Map;

public class MapController extends ContentController
{
	private TabPane mRoot;
	
	private Map mMap;
	
	public MapController(Map map)
	{
		super(map);
		mMap = map;
	}

	@Override
	public Node getUI()
	{
		return mRoot;
	}
}
