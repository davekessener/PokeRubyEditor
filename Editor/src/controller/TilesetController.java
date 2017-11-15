package controller;

import controller.tile.TileController;
import javafx.scene.Node;
import javafx.scene.image.Image;
import model.Tileset;
import model.Tileset.Tile;
import model.Vec2;
import model.Tileset.StaticTile;
import view.tile.TilesetUI;

public class TilesetController extends ContentController
{
	private TilesetUI mUI;
	private Tileset mTileset;
	private Image mSource;
	
	public TilesetController(String tid, Tileset ts)
	{
		super(ts);
		
		mTileset = ts;
		mSource = LoadTilesetSource(ts.getSource());
		mUI = new TilesetUI(mSource, ts.getSize(), mTileset.getTiles());
		
		mUI.setOnAddTiles(() -> addTiles());
		mUI.setOnSelect(id -> editTile(id));
		
		mTileset.getTiles().addObserver(o -> change());
	}
	
	private void addTiles()
	{
		for(int i = 0, l = mTileset.getTiles().size(), k = 8 - (l + i + 1) % 8 ; i < k ; ++i)
		{
			mTileset.addTile(new StaticTile(Format(l + i), new Vec2(0, 0)));
		}
	}
	
	private void editTile(String id)
	{
		if(id == null)
		{
			mUI.clearEditor();
		}
		else
		{
			mUI.editTile(new TileController(mTileset.getTiles().get(id), t -> writeTile(t)).getUI());
		}
	}
	
	private void writeTile(Tile t)
	{
		if(!mTileset.getTiles().containsKey(t.getID()))
		{
			throw new IllegalArgumentException("ERR: Unknown tile " + t.getID());
		}
		
		mTileset.getTiles().put(t.getID(), t);
	}

	@Override
	public Node getUI()
	{
		return mUI.getNode();
	}
	
	private static String Format(int idx) { return String.format("%04X", idx); }
	
	private static Image LoadTilesetSource(String src)
	{
		return EditorController.Instance.getLoader().loadMedia("tileset", src);
	}
}
