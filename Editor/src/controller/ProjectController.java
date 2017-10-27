package controller;

import java.io.File;

import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Project;
import model.Tileset;
import view.MenuManager;
import view.ProjectUI;

public class ProjectController implements Controller
{
	private Project mProject;
	private ProjectUI mUI;
	private MenuManager mMenu;
	private ContentController mContent;
	
	public ProjectController(File dir, MenuManager menu)
	{
		mProject = new Project(dir.getAbsolutePath() + "/data");
		mUI = new ProjectUI((type, id) -> editItem(type, id));
		mMenu = menu;

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
		
		mMenu.setHandler("file:close", null);
		
		return true;
	}
	
	private boolean closeResource()
	{
		if(mContent != null && mContent.needsSave())
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
		
		if(type == "tileset")
		{
			Tileset ts = new Tileset();
			ts.load(EditorController.Instance.getLoader().loadData(type, id));
			mContent = new TilesetController(ts);
			mUI.setContent(mContent.getUI());
		}
	}
	
	private boolean confirmExiting()
	{
		Alert confirm = new Alert(AlertType.CONFIRMATION, "Do you want to save?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
		
		confirm.showAndWait();
		
		if(confirm.getResult() == ButtonType.YES)
		{
			mContent.save();
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
}
