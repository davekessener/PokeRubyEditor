package view.tile;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import view.TiledCanvas;

public class TiledImageView extends TiledCanvas
{
	private Image mSource;
	
	public TiledImageView(Image src, int ts)
	{
		super((int)(src.getWidth() / ts), (int)(src.getHeight() / ts), ts);
		
		mSource = src;
	}
	
	@Override
	protected void drawTiles(GraphicsContext gc)
	{
		gc.drawImage(mSource, 0.0, 0.0);
	}
}
