package view.tile;

import java.util.function.Consumer;

import controller.EditorController;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import lib.observe.ObservableMap;
import model.Tileset.Tile;
import view.UI;
import view.tilemap.TilesetView;

public class TilesetUI implements UI
{
	private BorderPane mRoot;
	private TiledImageView mTilesetSource;
	private TilesetView mTilesetView;
	private Runnable mOnAddTiles;
	private Consumer<String> mOnSelectTile;
	
	public TilesetUI(Image src, int ts, ObservableMap<String, Tile> tiles)
	{
		mRoot = new BorderPane();
		mTilesetSource = new TiledImageView(src, ts);
		mTilesetView = new TilesetView(src, ts, new TilesetView.MapWrapper<>(tiles, o -> o.getPosition()), 8);
		
		Button addBtn = new Button("+");
		GridPane gp = new GridPane();
		ScrollPane sp = new ScrollPane();
		sp.setContent(mTilesetView);
		sp.prefWidthProperty().bind(mTilesetView.widthProperty().add(16));
		sp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		gp.addColumn(0, addBtn, sp);
		GridPane.setHgrow(addBtn, Priority.ALWAYS);
		GridPane.setVgrow(sp, Priority.ALWAYS);
		mRoot.setRight(gp);
		mTilesetSource.drawGridProperty().bind(EditorController.Instance.getOptions().drawGridProperty());
		
		mRoot.setOnKeyPressed(e -> {
			if(e.getCode().equals(KeyCode.TAB))
			{
				mTilesetView.setSelectedIndex(mTilesetView.getSelectedIndex() + 1);
				selectTile(mTilesetView.getSelectedID());
				e.consume();
			}
		});
		
		mTilesetView.setOnSelect(id -> selectTile(id));
		addBtn.setOnAction(e -> addTiles());
		tiles.addObserver(o -> redraw());
	}

	public void setOnSelect(Consumer<String> cb) { mOnSelectTile = cb; }
	public void setOnAddTiles(Runnable r) { mOnAddTiles = r; }
	
	public void redraw()
	{
		mTilesetSource.draw();
		mTilesetView.draw();
	}
	
	public void clearEditor()
	{
		mRoot.setCenter(null);
	}
	
	public void editTile(TileUI ui)
	{
		BorderPane bp = new BorderPane();
		ScrollPane sp = new ScrollPane();
		sp.setContent(ui.getNode());
		bp.setTop(sp);
		sp = new ScrollPane();
		sp.setContent(mTilesetSource);
		bp.setCenter(sp);
		mRoot.setCenter(bp);
		
		ui.bind(mTilesetSource);
	}
	
	private void selectTile(String id)
	{
		if(mOnSelectTile != null)
		{
			mOnSelectTile.accept(id);
		}
	}
	
	private void addTiles()
	{
		if(mOnAddTiles != null)
		{
			mOnAddTiles.run();
		}
	}

	@Override
	public Parent getNode()
	{
		return mRoot;
	}
}
