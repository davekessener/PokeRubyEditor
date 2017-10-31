package view.tile;

import controller.tile.AnimatedTileEditor;
import controller.tile.StaticTileEditor;
import controller.tile.TileEditController;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import lib.Observable;
import model.Tileset.AnimatedTile;
import model.Tileset.StaticTile;
import model.Tileset.Tile;
import view.RenderUtils;
import model.Vec2;

public class ObservableTile extends Observable
{
	private Image mSource;
	private Tile mTile;
	private int mTileSize;
	
	public ObservableTile(Image src, int ts, Tile t)
	{
		mSource = src;
		mTile = t;
		mTileSize = ts;
	}
	
	protected Image getSource() { return mSource; }
	public int getTileSize() { return mTileSize; }
	
	public void render(GraphicsContext gc, int x, int y)
	{
		RenderUtils.RenderTile(gc, mSource, mTileSize, mTile.getPosition(), new Vec2(x, y));
	}
	
	public TileEditController createEditor()
	{
		if(mTile instanceof StaticTile)
		{
			return new StaticTileEditor(mSource, mTileSize, (StaticTile) mTile, () -> change());
		}
		else if(mTile instanceof AnimatedTile)
		{
			return new AnimatedTileEditor(mSource, mTileSize, (AnimatedTile) mTile, () -> change());
		}
		else
		{
			throw new RuntimeException("Invalid tile type: " + mTile.getClass().getSimpleName());
		}
	}
}
