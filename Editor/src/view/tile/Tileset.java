package view.tile;

import javafx.scene.canvas.GraphicsContext;
import lib.ObservableList;
import view.TiledCanvas;

public class Tileset extends TiledCanvas
{
	private ObservableList<ObservableTile> mTiles;
	
	public Tileset(ObservableList<ObservableTile> tiles, int ts)
	{
		super(1, tiles.size(), ts);
		
		mTiles = tiles;
		mTiles.addObserver(o -> redraw());
		
		for(ObservableTile t : tiles)
		{
			t.addObserver(o -> draw());
		}
	}
	
	private void redraw()
	{
		this.setHeight(mTiles.size());
		
		for(ObservableTile t : mTiles)
		{
			t.addObserver(o -> draw());
		}

		draw();
	}
	
	@Override
	protected void drawTiles(GraphicsContext gc)
	{
		for(int i = 0 ; i < mTiles.size() ; ++i)
		{
			mTiles.get(i).render(gc, 0, i);
		}
	}
}
