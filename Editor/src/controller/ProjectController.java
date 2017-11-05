package controller;

import java.io.File;
import java.util.HashMap;

import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import lib.MenuManager;
import model.Loader;
import model.Map;
import model.Tilemap;
import model.Tileset;
import view.ProjectUI;
import view.create.NewMapUI;
import view.create.NewTilemapUI;
import view.create.NewTilesetUI;
import view.create.NewUI;

public class ProjectController implements Controller
{
	private File mProject;
	private ProjectUI mUI;
	private MenuManager mMenu;
	private ContentController mContent;
	private File mOpenFile;
	
	public ProjectController(File dir, MenuManager menu)
	{
		mProject = dir;
		mUI = new ProjectUI(dir.getName(), (type, id) -> editItem(type, id));
		mMenu = menu;
		mOpenFile = null;
		
		updateProjectTree();

		mMenu.setHandler("file:close", () -> tryClose());
		mMenu.setRange("project:new", type -> createNew(type), CREATOR_UI.keySet());
	}
	
	private void updateProjectTree()
	{
		mUI.updateTree(mProject);
	}
	
	@Override
	public Node getUI()
	{
		return mUI.getNode();
	}
	
	public boolean tryClose()
	{
		if(!closeResource())
		{
			return false;
		}

		mMenu.setHandler("file:save", null);
		mMenu.setHandler("file:close", null);
		
		return true;
	}
	
	private void createNew(String type)
	{
		try
		{
			NewUI ui = CREATOR_UI.get(type).newInstance();
			ModalDialog dialog = new ModalDialog("Create new " + type, ui);
			
			dialog.showAndWait();
			
			if(dialog.isSuccessful())
			{
				Loader l = EditorController.Instance.getLoader();
				
				l.write(l.getFile("data", type, ui.getID() + ".json"), ui.getData());
				
				updateProjectTree();
			}
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	private boolean closeResource()
	{
		if(mContent != null)
		{
			if(mContent.needsSave().getValue() && !confirmExiting())
			{
				return false;
			}
			
			mContent = null;
			mUI.setContent(null);
		}
		
		return true;
	}
	
	private void editItem(String type, String id)
	{
		if(!closeResource()) return;

		Loader l = EditorController.Instance.getLoader();

		if(type == "tileset")
		{
			Tileset ts = new Tileset();	
			ts.load(l.loadData(type, id));
			mContent = new TilesetController(ts);
		}
		else if(type == "tilemap")
		{
			Tilemap tm = new Tilemap();
			tm.load(l.loadData(type, id));
			mContent = new TilemapController(tm);
		}
		else if(type == "map")
		{
			Map map = new Map();
			map.load(l.loadData(type, id));
			mContent = new MapController(id, map);
		}

		mUI.setContent(mContent.getUI());
		mContent.needsSave().addListener(v -> changeSaveState());
		mOpenFile = l.getFile("data", type, id + ".json");
	}
	
	private void changeSaveState()
	{
		mMenu.setHandler("file:save", (mContent != null && mContent.needsSave().getValue()) ? () -> save() : null);
	}
	
	private boolean confirmExiting()
	{
		Alert confirm = new Alert(AlertType.CONFIRMATION, "Do you want to save?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
		
		confirm.showAndWait();
		
		if(confirm.getResult() == ButtonType.YES)
		{
			save();
			return true;
		}
		else if(confirm.getResult() == ButtonType.NO)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private void save()
	{
		EditorController.Instance.getLoader().write(mOpenFile, mContent.save());
	}
	
	private static final java.util.Map<String, Class<? extends NewUI>> CREATOR_UI = new HashMap<>();
	
	static
	{
		CREATOR_UI.put("map", NewMapUI.class);
		CREATOR_UI.put("tilemap", NewTilemapUI.class);
		CREATOR_UI.put("tileset", NewTilesetUI.class);
	}
}
