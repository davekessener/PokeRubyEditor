package view;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;

public class ProjectUI implements UI
{
	private BorderPane mRoot;
	private TreeView<String> mTree;
	private Callback mCallback;
	
	public ProjectUI(Callback cb)
	{
		mRoot = new BorderPane();
		mCallback = cb;

		ScrollPane treeContainer = new ScrollPane();
		
		mTree = new TreeView<>();
		
		mTree.setOnMouseClicked(e -> {
			if(mCallback != null && e.getClickCount() == 2)
			{
				TreeItem<String> selected = mTree.getSelectionModel().getSelectedItem();
				mCallback.handle(selected.getParent().getValue(), selected.getValue());
			}
		});
		
		mTree.prefHeightProperty().bind(mRoot.heightProperty());
		
		treeContainer.setContent(mTree);
		
		mRoot.setLeft(treeContainer);
	}
	
	public void setContent(Node content)
	{
		mRoot.setCenter(content);
	}
	
	public void updateTree(String name, Map<String, List<String>> t)
	{
		TreeItem<String> root = new TreeItem<>(name);
		
		for(Entry<String, List<String>> e : t.entrySet())
		{
			TreeItem<String> branch = new TreeItem<>(e.getKey());
			
			for(String s : e.getValue())
			{
				branch.getChildren().add(new TreeItem<>(s));
			}
			
			root.getChildren().add(branch);
		}
		
		mTree.setRoot(root);
	}
	
	@Override
	public Parent getNode()
	{
		return mRoot;
	}
	
	public static interface Callback { public abstract void handle(String type, String id); }
}
