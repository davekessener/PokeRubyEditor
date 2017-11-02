package view.map;

import controller.EditorController;
import lib.tilemap.MapLayerManager;
import model.Loader;
import model.Tilemap;
import model.Tileset;
import view.TilesetCanvas;
import view.tilemap.TilesetRenderer;

public class Preview extends TilesetCanvas
{
	private String mID;
	private Tilemap mTilemap;
	private Tileset mTileset;
	
	public Preview(String id, Tilemap map, Tileset ts)
	{
		super(new MapLayerManager(map), ts.getSize(), new TilesetRenderer(ts));
		
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
		Loader l = EditorController.Instance.getLoader();
		Tilemap map = new Tilemap();
		Tileset ts = new Tileset();
		
		map.load(l.loadData("tilemap", id));
		ts.load(l.loadData("tileset", map.getTilesetID()));
		
		return new Preview(id, map, ts);
	}
}
