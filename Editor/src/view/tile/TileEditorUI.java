package view.tile;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import model.Tileset.Type;

public class TileEditorUI implements TileUI
{
	private final BorderPane mRoot;
	private OnSelectCallback mOnSelect;
	private TiledImageView mTilesetView;
	private TileUI mUI;
	
	public TileEditorUI(TileUI ui)
	{
		mRoot = new BorderPane();
		
		mRoot.setPadding(new Insets(3D, 3D, 3D, 3D));
		
		ComboBox<Type> types = new ComboBox<>();
		types.getItems().addAll(Type.values());
		types.getSelectionModel().select(ui.getType());
		types.setPadding(new Insets(0D, 5D, 0D, 0D));
		
		BorderPane.setAlignment(types, Pos.CENTER_LEFT);
		
		mRoot.setLeft(types);
		
		types.valueProperty().addListener((ob, o, n) -> { if(!n.equals(o)) select(n); });
		
		setUI(ui);
	}
	
	public void setOnSelect(OnSelectCallback cb) { mOnSelect = cb; }
	
	public void setUI(TileUI ui)
	{
		mUI = ui;
		mRoot.setCenter(mUI.getNode());
		
		if(mTilesetView != null)
		{
			mUI.bind(mTilesetView);
		}
	}
	
	private void select(Type t)
	{
		if(mOnSelect != null)
		{
			mOnSelect.onSelect(t);
		}
	}
	
	@Override
	public Parent getNode()
	{
		return mRoot;
	}
	
	public static interface OnSelectCallback { public abstract void onSelect(Type t); }

	@Override
	public void bind(TiledImageView tileset)
	{
		mUI.bind(mTilesetView = tileset);
	}

	@Override
	public Type getType()
	{
		return mUI.getType();
	}
}
