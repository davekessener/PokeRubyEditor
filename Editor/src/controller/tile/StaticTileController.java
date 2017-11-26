package controller.tile;

import controller.tile.TileController.TileWriter;
import lib.misc.Vec2;
import model.Tileset.StaticTile;
import view.tile.StaticTileUI;
import view.tile.TileUI;

public class StaticTileController implements ActualTileController
{
	private final StaticTile mTile;
	private final StaticTileUI mUI;
	private final TileWriter mWriter;
	
	public StaticTileController(StaticTile t, TileWriter w)
	{
		mTile = t;
		mWriter = w;
		mUI = new StaticTileUI(t.getPosition());
		
		mUI.setOnSelection(p -> setSelection(p));
	}
	
	private void setSelection(Vec2 p)
	{
		mTile.setPosition(p);
		
		mWriter.write(mTile);
	}

	@Override
	public TileUI getUI()
	{
		return mUI;
	}
}
