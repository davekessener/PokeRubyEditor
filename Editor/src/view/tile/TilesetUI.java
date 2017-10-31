package view.tile;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import view.TiledCanvas;
import view.UI;

public class TilesetUI extends TiledCanvas implements UI
{
	private Group mRoot;
	private Image mSource;
	
	public TilesetUI(Image source, int tilesize)
	{
		super((int)(source.getWidth() / tilesize), (int)(source.getHeight() / tilesize), tilesize);
		mRoot = new Group();
		mSource = source;
		
		mRoot.getChildren().add(this);
	}
	
	@Override
	protected void drawTiles(GraphicsContext gc)
	{
		gc.drawImage(mSource, 0, 0);
	}
	
	@Override
	public Parent getNode()
	{
		return mRoot;
	}
}
