package view.map;

import lib.Utils;
import model.Map;
import model.Tilemap;
import model.Tileset;
import model.layer.MapWrapper;
import view.TilesetCanvas;
import view.tilemap.TilesetRenderer;

public class Preview extends TilesetCanvas
{
	private String mID;
	private Tilemap mTilemap;
	private Tileset mTileset;
	
	public Preview(String id, Tilemap map, Tileset ts)
	{
		super(new MapWrapper(map), ts.getSize(), new TilesetRenderer(ts));
		
		mID = id;
		mTilemap = map;
		mTileset = ts;
	}
	
	public String getID() { return mID; }
	public int getMapWidth() { return mTilemap.getWidth(); }
	public int getMapHeight() { return mTilemap.getHeight(); }
	public int getTileSize() { return mTileset.getSize(); }
	
	public static Preview Create(String id)
	{
		Map map = Utils.loadMap(id);
		Tilemap tilemap = Utils.loadTilemap(map.getTilemapID());
		Tileset tileset = Utils.loadTileset(tilemap.getTilesetID());
		
		return new Preview(id, tilemap, tileset);
	}
}
