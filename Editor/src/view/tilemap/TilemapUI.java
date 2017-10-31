package view.tilemap;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import view.UI;

public class TilemapUI implements UI
{
	private TabPane mRoot;
	
	public TilemapUI(Node map, Node meta, Node data)
	{
		mRoot = new TabPane();
		
		mRoot.getTabs().addAll(createTab("Tilemap", map), createTab("Meta", meta), createTab("Data", data));
	}
	
	private Tab createTab(String title, Node content)
	{
		Tab tab = new Tab(title);
		tab.setContent(content);
		tab.setClosable(false);
		content.setStyle("-fx-padding: 10 10 10 10;");
		return tab;
	}
	
	@Override
	public Parent getNode()
	{
		return mRoot;
	}
}
