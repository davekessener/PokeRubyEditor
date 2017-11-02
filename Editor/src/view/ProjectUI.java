package view;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;

public class ProjectUI implements UI
{
	private BorderPane mRoot;
	private TreeItem<String> mTreeRoot;
	
	public ProjectUI(String name, Callback cb)
	{
		mRoot = new BorderPane();

		ScrollPane treeContainer = new ScrollPane();
		
		TreeView<String> tree = new TreeView<>();
		
		tree.setOnMouseClicked(e -> {
			if(cb != null && e.getClickCount() == 2)
			{
				TreeItem<String> selected = tree.getSelectionModel().getSelectedItem();
				cb.handle(selected.getParent().getValue(), selected.getValue());
			}
		});

		mTreeRoot = new TreeItem<>(name);
		
		for(String s : VALID_TYPES)
		{
			mTreeRoot.getChildren().add(new TreeItem<>(s));
		}
		
		tree.prefHeightProperty().bind(mRoot.heightProperty());
		tree.setRoot(mTreeRoot);
		
		treeContainer.setContent(tree);
		
		mRoot.setLeft(treeContainer);
	}
	
	public void setContent(Node content)
	{
		mRoot.setCenter(content);
	}
	
	public void updateTree(File dir)
	{
		Map<String, List<String>> t = generateTree(dir.getAbsolutePath() + "/data");
		
		for(TreeItem<String> branch : mTreeRoot.getChildren())
		{
			List<String> content = t.get(branch.getValue());
			
			if(content != null) for(String s : content)
			{
				if(!branch.getChildren().stream().anyMatch(i -> i.getValue().equals(s)))
				{
					branch.getChildren().add(new TreeItem<>(s));
				}
			}
		}
	}
	
	@Override
	public Parent getNode()
	{
		return mRoot;
	}

	private static Map<String, List<String>> generateTree(String dir)
	{
		Map<String, List<String>> content = new HashMap<>();
		
		for(String type : VALID_TYPES)
		{
			content.put(type, readIDs(dir + File.separator + type));
		}
		
		return content;
	}
	
	private static List<String> readIDs(String path)
	{
		List<String> r = new ArrayList<>();
		
		for(File f : (new File(path)).listFiles((dir, fn) -> fn.toLowerCase().endsWith(".json")))
		{
			r.add(f.getName().replaceAll("\\.json$", ""));
		}
		
		return r;
	}
	
	private static final String[] VALID_TYPES = new String[] {"map", "tilemap", "tileset"};
	
	public static interface Callback { public abstract void handle(String type, String id); }
}
