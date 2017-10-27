package view;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;

public class EditorUI implements UI, MenuManager
{
	private BorderPane root;
	private MenuBar menu;
	
	private Map<String, MenuItem> menuItems;
	private Map<String, Runnable> handlers;
	
	public EditorUI()
	{
		root = new BorderPane();
		menu = new MenuBar();
		menuItems = new HashMap<>();
		
		Menu fileMenu = createFileMenu();
		
		menu.getMenus().addAll(fileMenu);
		
		root.setTop(menu);

		initializeHandlers();
	}
	
	public void setContent(Node node)
	{
		root.setCenter(node);
	}
	
	@Override
	public void setHandler(String id, Runnable handler)
	{
		handlers.put(id, handler);
		menuItems.get(id).setDisable(handler == null);
	}
	
	@Override
	public Parent getNode()
	{
		return root;
	}
	
	private Menu createFileMenu()
	{
		Menu m = new Menu("File");
	
		m.getItems().addAll(
			createMenuItem("file:open", "Open ..."),
			createMenuItem("file:close", "Close"),
			new SeparatorMenuItem(),
			createMenuItem("file:exit", "Quit")
		);
		
		return m;
	}
	
	private MenuItem createMenuItem(String id, String content)
	{
		MenuItem item = new MenuItem(content);
		
		menuItems.put(id, item);
		
		return item;
	}
	
	private void initializeHandlers()
	{
		handlers = new HashMap<>();
		for(Entry<String, MenuItem> e : menuItems.entrySet())
		{
			MenuItem m = e.getValue();
			
			m.setOnAction(a -> {
				Runnable r = handlers.get(e.getKey());
				if(r != null) r.run();
			});
			m.setDisable(true);
		}
	}
}
