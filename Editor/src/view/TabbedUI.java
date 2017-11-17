package view;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class TabbedUI implements UI
{
	private TabPane mRoot;
	
	public TabbedUI()
	{
		mRoot = new TabPane();
	}
	
	public TabbedUI addTab(String title, UI ui)
	{
		Tab tab = new Tab(title);
		Node content = ui.getNode();
		tab.setContent(content);
		tab.setClosable(false);
		content.setStyle("-fx-padding: 10 10 0 10;");
		mRoot.getTabs().add(tab);
		return this;
	}

	@Override
	public Parent getNode()
	{
		return mRoot;
	}
}
