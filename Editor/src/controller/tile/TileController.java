package controller.tile;

import model.Tileset.AnimatedTile;
import model.Tileset.StaticTile;
import model.Tileset.Tile;
import model.Tileset.Type;
import view.tile.TileEditorUI;
import view.tile.TileUI;

public class TileController
{
	private TileWriter mWriter;
	private ActualTileController mController;
	private TileEditorUI mUI;
	private Tile mTile;
	
	public TileController(Tile t, TileWriter w)
	{
		mWriter = w;
		mTile = t;
		mController = instantiateController();
		mUI = new TileEditorUI(mController.getUI());
		
		mUI.setOnSelect(type -> changeType(type));
	}
	
	private void changeType(Type t)
	{
		if(t.equals(Type.STATIC))
		{
			updateController(new StaticTile(mTile.getID(), mTile.getPosition()));
		}
		else if(t.equals(Type.ANIMATED))
		{
			updateController(new AnimatedTile(mTile.getID()));
		}
		else
		{
			throw new RuntimeException();
		}
	}
	
	public TileUI getUI()
	{
		return mUI;
	}
	
	private void updateController(Tile t)
	{
		mTile = t;
		mController = instantiateController();
		mUI.setUI(mController.getUI());
		mWriter.write(mTile);
	}
	
	private ActualTileController instantiateController()
	{
		if(mTile instanceof StaticTile)
		{
			return new StaticTileController((StaticTile) mTile, mWriter);
		}
		else if(mTile instanceof AnimatedTile)
		{
			return new AnimatedTileController((AnimatedTile) mTile, mWriter);
		}
		else
		{
			throw new RuntimeException();
		}
	}
	
	public static interface TileWriter { public abstract void write(Tile t); }
}
