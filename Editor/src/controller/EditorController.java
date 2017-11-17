package controller;

import java.io.File;

import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lib.MenuManager;
import lib.Options;
import lib.action.ActionManager;
import lib.action.IActionManager;
import lib.observe.IObservable;
import model.Loader;
import view.EditorUI;

public class EditorController
{
	public static final EditorController Instance = new EditorController();
	
	private Stage mPrimary;
	private Scene mScene;
	private EditorUI mUI;
	private ProjectController mProject;
	private Loader mLoader;
	private Options mOptions;
	private IActionManager mActionManager;
	
	private EditorController()
	{
	}
	
	public Loader getLoader() { return mLoader; }
	public Options getOptions() { return mOptions; }
	public MenuManager getMenuManager() { return mUI; }
	public IActionManager getActionManager() { return mActionManager; }
	
	public void run(Stage primary)
	{
		mPrimary = primary;
		mUI = new EditorUI();
		mScene = new Scene(mUI.getNode(), 800, 600);
		mOptions = new Options();
		mActionManager = IObservable.MakeObservable(new ActionManager(), IActionManager.class);

		mOptions.register(mUI);
		IObservable.AddObserver(mActionManager, o -> checkUndoRedo());

		mPrimary.setTitle("PokeRuby Editor");
		mPrimary.setOnCloseRequest(e -> close(e));
		
		registerActionHandlers();
		
		mPrimary.setScene(mScene);
		mPrimary.show();
	}
	
	public void open(File dir)
	{
		tryCloseCurrentProject();
		mProject = new ProjectController(dir);
		mLoader = new Loader(dir);
		mUI.setContent(mProject.getUI());
	}
	
	public void close(WindowEvent e)
	{
		if(!tryCloseCurrentProject())
		{
			e.consume();
		}
		else
		{
			mUI.setContent(null);
		}
	}
	
	private boolean tryCloseCurrentProject()
	{
		return mProject != null ? mProject.tryClose() : true;
	}
	
	private void checkUndoRedo()
	{
		mUI.setHandler("edit:undo", mActionManager.canUndo()
				? () -> mActionManager.undo()
				: null);
		mUI.setHandler("edit:redo", mActionManager.canRedo()
				? () -> mActionManager.redo()
				: null);
	}
	
	private void registerActionHandlers()
	{
		mUI.setHandler("file:open", () -> {
			DirectoryChooser choose = new DirectoryChooser();
			choose.setTitle("Choose project root");
			File dir = choose.showDialog(mPrimary);
			if(dir != null) open(dir);
		});
		
		mUI.setHandler("file:exit", () -> 
			mPrimary.fireEvent(new WindowEvent(mPrimary, WindowEvent.WINDOW_CLOSE_REQUEST)));
	}
}
