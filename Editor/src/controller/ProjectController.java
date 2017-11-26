package controller;

import java.io.File;
import java.util.HashMap;

import controller.map.MapController;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import lib.MenuManager;
import lib.Utils;
import model.JsonModel;
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
	private final File mProject;
	private final ProjectUI mUI;
	private final MenuManager mMenu;
	private ContentController mContent;
	private File mOpenFile;
	
	public ProjectController(File dir)
	{
		mProject = dir;
		mUI = new ProjectUI(dir.getName(), (type, id) -> editItem(type, id));
		mMenu = EditorController.Instance.getMenuManager();
		
		updateProjectTree();

		mMenu.setRange("edit:new", type -> createNew(type), CREATORS.keySet());
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
		NewUI ui = CREATORS.get(type).instantiateUI();
		ModalDialog dialog = new ModalDialog("Create new " + type, ui);
			
		dialog.showAndWait();
		
		if(dialog.isSuccessful())
		{
			Loader l = EditorController.Instance.getLoader();
			
			l.writeData(type, ui.getID(), ui.getData());
			
			updateProjectTree();
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
		
		EditorController e = EditorController.Instance;
		
		e.getActionManager().clear();
		mContent = CREATORS.get(type).instantiateController(id);
		mUI.setContent(mContent.getUI());
		mContent.needsSave().addListener(v -> changeSaveState());
		mOpenFile = new File(e.getLoader().generateFilename(type, id));
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
		EditorController.Instance.getLoader().writeFile(mOpenFile, mContent.save());
	}
	
	private static final java.util.Map<String, Creator> CREATORS = new HashMap<>();
	
	static
	{
		new Creator("map", MapController.class, Map.class, NewMapUI.class);
		new Creator("tilemap", TilemapController.class, Tilemap.class, NewTilemapUI.class);
		new Creator("tileset", TilesetController.class, Tileset.class, NewTilesetUI.class);
	}
	
	private static class Creator
	{
		private final String mType;
		private final Class<? extends ContentController> mControllerClass;
		private final Class<? extends JsonModel> mModelClass;
		private final Class<? extends NewUI> mCreatorClass;
		
		public Creator(String type, Class<? extends ContentController> ccc, Class<? extends JsonModel> cjm, Class<? extends NewUI> cnui)
		{
			mType = type;
			mControllerClass = ccc;
			mModelClass = cjm;
			mCreatorClass = cnui;
			
			CREATORS.put(mType, this);
		}

		public NewUI instantiateUI()
		{
			return Instantiate(mCreatorClass);
		}
		
		public JsonModel instantiateModel()
		{
			return Instantiate(mModelClass);
		}
		
		public ContentController instantiateController(String id)
		{
			try
			{
				return mControllerClass.getConstructor(String.class, mModelClass).newInstance(id, Utils.loadJSON(instantiateModel(), mType, id));
			}
			catch(Exception e)
			{
				throw new RuntimeException(e);
			}
		}
		
		private static <T> T Instantiate(Class<? extends T> c)
		{
			try
			{
				return c.newInstance();
			}
			catch(InstantiationException | IllegalAccessException e)
			{
				throw new RuntimeException(e);
			}
		}
	}
}
