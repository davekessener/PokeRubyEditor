package controller;

import com.eclipsesource.json.JsonValue;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import model.Vec2;
import view.Tilemap;
import view.Tileset;

public class TilesetController implements ContentController
{
	private BorderPane mRoot;
	private model.Tileset mTilesetModel;
	private Image mSource;
	private Tileset mTileset;
	private Tilemap mTilemap;
	private int mSelected;
	
	public TilesetController(model.Tileset ts)
	{
		mRoot = new BorderPane();
		mTilesetModel = ts;
		mSource = EditorController.Instance.getLoader().loadMedia("tileset", ts.getSource());
		mTileset = new Tileset(mSource, ts.getSize());
		mTilemap = new Tilemap(mTileset, 1, ts.getTileCount(), e -> handleSelect(e));
		mSelected = 0;
		
		for(int i = 0 ; i < mTilesetModel.getTileCount() ; ++i)
		{
			model.Tileset.Tile tile = mTilesetModel.getTile(i);
			mTileset.addTile(tile.getID(), tile.getPosition());
			mTilemap.setTile(new Vec2(0, i), tile.getID());
		}
		
		draw();
		
		ScrollPane pane = new ScrollPane();
		
		pane.setContent(mTilemap.getNode());
		
		mRoot.setRight(pane);
	}

	@Override
	public Node getUI()
	{
		return mRoot;
	}

	@Override
	public boolean needsSave()
	{
		return false;
	}

	@Override
	public JsonValue save()
	{
		return mTilesetModel.save();
	}
	
	private void draw()
	{
		mTilemap.draw(new Vec2(0, mSelected));
	}
	
	private void handleSelect(MouseEvent e)
	{
		if(e.isPrimaryButtonDown())
		{
			System.out.println("x: " + e.getSceneX() + ", y: " + e.getSceneY());
		}
	}
}
