package controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.eclipsesource.json.WriterConfig;

import controller.map.MapController;
import controller.tile.TilesetController;
import controller.tilemap.TilemapController;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Loader;
import model.Map;
import model.Project;
import model.Tilemap;
import model.Tileset;
import view.MenuManager;
import view.ProjectUI;

public class ProjectController implements Controller
{
	private Project mProject;
	private ProjectUI mUI;
	private MenuManager mMenu;
	private ContentController mContent;
	private File mOpenFile;
	
	public ProjectController(File dir, MenuManager menu)
	{
		mProject = new Project(dir.getAbsolutePath() + "/data");
		mUI = new ProjectUI((type, id) -> editItem(type, id));
		mMenu = menu;
		mOpenFile = null;

		mUI.updateTree(dir.getName(), mProject.content);
		
		mMenu.setHandler("file:close", () -> tryClose());
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
	
	private boolean closeResource()
	{
		if(mContent != null && mContent.needsSave().getValue())
		{
			if(!confirmExiting())
			{
				return false;
			}
			else
			{
				mContent = null;
				mUI.setContent(null);
			}
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
			mContent = new MapController(map);
		}

		mUI.setContent(mContent.getUI());
		mContent.needsSave().addListener(v -> changeSaveState());
		mOpenFile = l.getFile("data", type, id + ".json");
	}
	
	private void changeSaveState()
	{
		mMenu.setHandler("file:save", mContent.needsSave().getValue() ? () -> save() : null);
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
		try
		{
			FileWriter fw = new FileWriter(mOpenFile);
			mContent.save().writeTo(fw, WriterConfig.PRETTY_PRINT);
			fw.close();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}
