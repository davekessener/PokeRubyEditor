package controller.tile;

import java.util.ArrayList;

import com.eclipsesource.json.JsonValue;

import controller.ContentController;
import controller.EditorController;
import controller.ModalDialog;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import lib.ObservableList;
import model.Vec2;
import model.Tileset.AnimatedTile;
import model.Tileset.StaticTile;
import view.tile.CreateTileUI;
import view.tile.ObservableTile;
import view.tile.Tileset;

public class TilesetController implements ContentController
{
	private BorderPane mRoot;
	private model.Tileset mTilesetModel;
	private Image mSource;
	private Tileset mTileset;
	private int mSelected;
	private ObservableList<ObservableTile> mTiles;
	private TileEditController mTileEditor;
	private Property<Boolean> mCanRemoveTile, mChanged;
	
	public TilesetController(model.Tileset ts)
	{
		mRoot = new BorderPane();
		mTilesetModel = ts;
		mSource = EditorController.Instance.getLoader().loadMedia("tileset", ts.getSource());
		mCanRemoveTile = new SimpleBooleanProperty();
		mChanged = new SimpleBooleanProperty(false);

		mTiles = new ObservableList<>(new ArrayList<>());
		
		for(int i = 0 ; i < mTilesetModel.getTileCount() ; ++i)
		{
			mTiles.add(new ObservableTile(mSource, mTilesetModel.getSize(), mTilesetModel.getTile(i)));
		}
		
		mTileset = new Tileset(mTiles, mTilesetModel.getSize());
		
		mTileset.setOnTileActivated((b, x, y) -> select(y));
		
		setupUI();
		
		select(0);
	}
	
	@Override
	public Node getUI()
	{
		return mRoot;
	}

	@Override
	public Property<Boolean> needsSave()
	{
		return mChanged;
	}

	@Override
	public JsonValue save()
	{
		mChanged.setValue(false);
		
		return mTilesetModel.save();
	}

	private void select(int s)
	{
		mSelected = s;
		mTileset.setSelected(new Vec2(0, mSelected));
		mCanRemoveTile.setValue(mTilesetModel.getTileCount() <= 1);
		mTileEditor = mTiles.get(s).createEditor();
		mTileEditor.changed().addListener((ob, o, n) -> { if(n) mChanged.setValue(n); });
		mRoot.setCenter(mTileEditor.getUI());
	}
	
	private void addNewTile()
	{
		CreateTileUI ui = new CreateTileUI();
		ModalDialog dialog = new ModalDialog("Create new tile", ui);
		
		dialog.showAndWait();
		
		if(dialog.isSuccessful())
		{
			mTilesetModel.addTile(ui.getID(), ui.getType().equals("animated") ? new AnimatedTile() : new StaticTile());
			mTiles.add(new ObservableTile(mSource, mTilesetModel.getSize(), mTilesetModel.getTile(mTilesetModel.getTileCount() - 1)));
			select(mTilesetModel.getTileCount() - 1);
			mChanged.setValue(true);
		}
	}
	
	private void deleteTile()
	{
		if(mTilesetModel.getTileCount() > 1)
		{
			mTilesetModel.deleteTile(mSelected);
			mTiles.remove(mSelected);
			select(0);
			mChanged.setValue(true);
		}
	}
	
	private void setupUI()
	{
		ScrollPane pane = new ScrollPane();
		VBox hbox = new VBox();
		
		Button add = new Button("+");
		Button del = new Button("-");
		
		add.setOnAction(e -> addNewTile());
		del.setOnAction(e -> deleteTile());
		
		pane.setContent(mTileset);
		hbox.getChildren().addAll(add, del, pane);
		
		del.disableProperty().bind(mCanRemoveTile);
		del.prefWidthProperty().bind(pane.widthProperty());
		add.prefWidthProperty().bind(pane.widthProperty());
		
		mRoot.setRight(hbox);
	}
}
