package controller;

import java.io.File;

import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lib.MenuManager;
import lib.Options;
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
	
	private EditorController()
	{
	}
	
	public Loader getLoader() { return mLoader; }
	public Options getOptions() { return mOptions; }
	public MenuManager getMenuManager() { return mUI; }
	
	public void run(Stage primary)
	{
		mPrimary = primary;
		mUI = new EditorUI();
		mScene = new Scene(mUI.getNode(), 800, 600);
		mOptions = new Options();

		mOptions.register(mUI);

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
	
	private void registerActionHandlers()
	{
		mUI.setHandler("file:open", () -> {
			DirectoryChooser choose = new DirectoryChooser();
			choose.setTitle("Choose project root");
			File dir = choose.showDialog(mPrimary);
			if(dir != null) open(dir);
		});
		
		mUI.setHandler("file:exit", () -> mPrimary.close());
	}
}
