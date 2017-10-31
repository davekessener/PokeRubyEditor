package view.tile;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import model.Vec2;
import view.RenderUtils;
import view.TiledCanvas;
import view.UI;

public class Tilemap extends TiledCanvas implements UI
{
	private Group mRoot;
	private Image mSource;
	private String[][] mTiles;
	private model.Tileset mTileset;
	
	public Tilemap(Image src, model.Tileset tileset, int w, int h)
	{
		super(w, h, tileset.getSize());
		mSource = src;
		mRoot = new Group();
		mTiles = new String[w][h];
		mTileset = tileset;
		
		mRoot.getChildren().add(this);
	}
	
	public void setTile(Vec2 p, String s) { mTiles[p.getX()][p.getY()] = s; }
	public String getTile(Vec2 p) { return mTiles[p.getX()][p.getY()]; }

	@Override
	protected void drawTile(GraphicsContext gc, int x, int y)
	{
		if(mTiles[x][y] != null)
		{
			RenderUtils.RenderTile(gc, mSource, getTileSize(), mTileset.getTile(mTiles[x][y]).getPosition(), new Vec2(x, y));
		}
	}
	
	@Override
	public Parent getNode()
	{
		return mRoot;
	}
}
