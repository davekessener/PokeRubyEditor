package view;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;

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
				Pair<String, String> selected = processSelected(tree.getSelectionModel().getSelectedItem());
				
				if(selected != null)
				{
					cb.handle(selected.getKey(), selected.getValue());
				}
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

	@Override
	public Parent getNode()
	{
		return mRoot;
	}
	
	public void updateTree(File root)
	{
		for(TreeItem<String> type : mTreeRoot.getChildren())
		{
			populateBranch(new File(root.getAbsolutePath() + "/data/" + type.getValue()), type);
		}
	}
	
	private void populateBranch(File path, TreeItem<String> branch)
	{
		if(path == null || !path.exists() || !path.isDirectory() || !path.canWrite())
			return;
		
		ObservableList<TreeItem<String>> children = branch.getChildren();
		
		Map<String, File> dirs = Arrays.stream(path.listFiles()).filter(f -> f.isDirectory()).collect(Collectors.toMap(e -> e.getName(), e -> e));
		
		children.removeIf(e -> !e.getChildren().isEmpty() && !dirs.values().stream().anyMatch(f -> f.getName().equals(e.getValue())));
		
		for(TreeItem<String> b : children)
		{
			populateBranch(dirs.get(b.getValue()), b);
			dirs.remove(b.getValue());
		}
		
		for(File f : dirs.values())
		{
			TreeItem<String> b = new TreeItem<>(f.getName());
			
			populateBranch(f, b);

			if(!b.getChildren().isEmpty())
			{
				children.add(b);
			}
		}
		
		for(File f : path.listFiles())
		{
			if(f.isFile() && f.canWrite() && f.getAbsolutePath().endsWith(".json"))
			{
				String id = f.getName().replaceAll("\\.json$", "");
				
				if(!children.stream().anyMatch(c -> id.equals(c.getValue())))
				{
					branch.getChildren().add(new TreeItem<>(id));
				}
			}
		}
	}
	
	private Pair<String, String> processSelected(TreeItem<String> selected)
	{
		if(selected == null) return null;
		if(selected.getParent() == null) return null;
		if(!selected.getChildren().isEmpty()) return null;
		
		Deque<String> path = new ArrayDeque<>();
		
		TreeItem<String> t = selected;
		while(t.getParent().getParent() != null)
		{
			path.addFirst(t.getValue());
			t = t.getParent();
		}
		
		if(t == selected) return null;
		
		final String type = t.getValue();
		final String id = String.join("/", path);
		
		if(!Arrays.stream(VALID_TYPES).anyMatch(s -> s.equals(type))) return null;
		if(id == null || id.isEmpty()) return null;
		
		return new Pair<String, String>(type, id);
	}
	
	private static final String[] VALID_TYPES = new String[] {"map", "tilemap", "tileset"};
	
	public static interface Callback { public abstract void handle(String type, String id); }
}
