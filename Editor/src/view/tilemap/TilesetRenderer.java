package view.tilemap;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import controller.EditorController;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import model.Loader;
import model.Tileset.Tile;
import model.Vec2;
import view.RenderUtils;
import view.TilesetCanvas.TileRenderer;

public class TilesetRenderer implements TileRenderer
{
	private final Image mSource;
	private final int mTileSize;
	private final Map<String, Vec2> mTiles;
	
	public TilesetRenderer(model.Tileset ts)
	{
		Loader l = EditorController.Instance.getLoader();
		
		mSource = l.loadMedia("tileset", ts.getSource());
		mTileSize = ts.getSize();
		mTiles = new LinkedHashMap<>();
		
		for(int i = 0 ; i < ts.getTileCount() ; ++i)
		{
			Tile t = ts.getTile(i);
			mTiles.put(t.getID(), t.getPosition());
		}
	}
	
	public int getTileSize() { return mTileSize; }
	public Image getSource() { return mSource; }
	public Map<String, Vec2> getTiles() { return Collections.unmodifiableMap(mTiles); }
	
	@Override
	public void drawTile(GraphicsContext gc, String id, int x, int y)
	{
		RenderUtils.RenderTile(gc, mSource, mTileSize, mTiles.get(id), new Vec2(x, y));
	}
}
