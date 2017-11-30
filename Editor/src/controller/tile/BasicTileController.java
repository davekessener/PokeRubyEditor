package controller.tile;

import controller.tile.TileController.TileWriter;
import model.Tileset.Tile;
import view.tile.BasicTileUI;

public abstract class BasicTileController implements ActualTileController
{
	private final Tile mTile;
	private final TileWriter mWriter;
	
	public BasicTileController(Tile t, TileWriter w)
	{
		mTile = t;
		mWriter = w;
	}
	
	private void onAnimatorChange(String s)
	{
		mTile.setAnimators(s);
		
		mWriter.write(mTile);
	}
	
	protected void registerHandler(BasicTileUI ui)
	{
		ui.setOnAnimatorChange(s -> onAnimatorChange(s));
	}
}
