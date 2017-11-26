package view;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;

import javafx.beans.property.Property;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import lib.MenuManager;

public class EditorUI implements UI, MenuManager
{
	private BorderPane root;
	private MenuBar menu;
	private Label status;
	
	private Map<String, MenuItem> menuItems;
	private Map<String, Runnable> handlers;
	private Map<String, Menu> menus;
	
	public EditorUI()
	{
		root = new BorderPane();
		menu = new MenuBar();
		menuItems = new HashMap<>();
		menus = new HashMap<>();
		
		Menu fileMenu = createFileMenu();
		Menu projectMenu = createProjectMenu();
		Menu optionsMenu = createOptionsMenu();
		
		HBox statusBar = createStatusBar();
		
		menu.getMenus().addAll(fileMenu, projectMenu, optionsMenu);
		
		menus.put("file", fileMenu);
		menus.put("edit", projectMenu);
		menus.put("options", optionsMenu);
		
		root.setTop(menu);
		root.setBottom(statusBar);

		initializeHandlers();
	}
	
	public void setContent(Node node)
	{
		root.setCenter(node);
	}
	
	@Override
	public void setStatus(String s)
	{
		status.setText(s);
	}
	
	@Override
	public void setStatus(Property<String> s)
	{
		status.textProperty().bind(s);
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
	
	private HBox createStatusBar()
	{
		HBox hb = new HBox();
		
		hb.setPadding(new Insets(0D, 5D, 0D, 0D));
		hb.getChildren().add(status = new Label(""));
		
		return hb;
	}
	
	private Menu createFileMenu()
	{
		Menu m = new Menu("File");
	
		m.getItems().addAll(
			createMenuItem("file:open", "Open ..."),
			createMenuItem("file:save", "Save"),
			createMenuItem("file:close", "Close"),
			new SeparatorMenuItem(),
			createMenuItem("file:exit", "Quit")
		);
		
		return m;
	}
	
	private Menu createProjectMenu()
	{
		Menu m = new Menu("Edit");
		
		m.getItems().addAll(
			createMenu("edit:new", "New"),
			createMenuItem("edit:undo", "Undo"),
			createMenuItem("edit:redo", "Redo")
		);
		
		return m;
	}
	
	private Menu createOptionsMenu()
	{
		Menu m = new Menu("Options");
		
		return m;
	}
	
	private Menu createMenu(String id, String content)
	{
		Menu m = new Menu(content);
		
		menuItems.put(id, m);
		
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

	@Override
	public void registerOption(String menuID, String optName, Property<Boolean> op)
	{
		CheckMenuItem m = new CheckMenuItem(optName);
		
		m.selectedProperty().setValue(op.getValue());
		op.bind(m.selectedProperty());

		menus.get(menuID).getItems().add(m);
	}

	@Override
	public void setRange(String id, Consumer<String> c, Set<String> opts)
	{
		Menu m = (Menu) menuItems.get(id);

		m.getItems().clear();
		
		if(c == null)
		{
			m.setDisable(true);
		}
		else
		{
			m.setDisable(false);
			
			for(String s : opts)
			{
				MenuItem i = new MenuItem(s);
				i.setOnAction(e -> c.accept(s));
				m.getItems().add(i);
			}
		}
	}
}
