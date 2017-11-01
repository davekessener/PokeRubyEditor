package controller.tilemap;

import controller.ContentController;
import controller.EditorController;
import javafx.scene.Node;
import lib.EnterableTextField;
import lib.tilemap.MapLayerManager;
import model.Loader;
import model.Tilemap;
import model.Tileset;
import view.TilesetCanvas;
import view.tilemap.TabData;
import view.tilemap.TabMap;
import view.tilemap.TabMeta;
import view.tilemap.TilemapUI;
import view.tilemap.TilesetRenderer;

public class TilemapController extends ContentController
{
	private TilemapUI mUI;
	private Tilemap mTilemap;
	private Tileset mTileset;
	private MapLayerManager mLayers;
	private TilesetRenderer mRenderTileset;
	private TabMap mTabMap;
	private TabMeta mTabMeta;
	private TabData mTabData;
	
	public TilemapController(Tilemap tm)
	{
		super(tm);
		mTilemap = tm;
		mTileset = loadTileset(mTilemap.getTilesetID());
		mLayers = new MapLayerManager(mTilemap);
		mRenderTileset = new TilesetRenderer(mTileset);
		
		mTabMap = new TabMap(mTilemap, mLayers, mTileset.getSize(), mRenderTileset);
		mTabMeta = new TabMeta(mTilemap.getMeta(), mTileset.getSize(), new TilesetCanvas(mLayers, mTileset.getSize(), mRenderTileset));
		mTabData = new TabData(mTilemap.getTilesetID(), mTilemap.getWidth(), mTilemap.getHeight());
		
		mLayers.addObserver(o -> change());
		mTabMap.setOnClick((x, y, lid, i, tile) -> setTile(lid, i, x, y, tile));
		mTabMeta.setOnChange((x, y, id) -> setMeta(x, y, id));
		mTabData.addTilesetValidation(new TilesetNameValidator());
		mTabData.setOnDimensionChange((w, h) -> resize(w, h));
		
		mUI = new TilemapUI(mTabMap.getNode(), mTabMeta.getNode(), mTabData.getNode());
	}
	
	private void setTile(String lid, int idx, int x, int y, String tile)
	{
		mLayers.setTile(lid, idx, x, y, tile);
		change();
	}
	
	private void setMeta(int x, int y, String id)
	{
		mTilemap.getMeta().set(x, y, id);
		change();
	}
	
	private void resize(int w, int h)
	{
		mLayers.resize(w, h);
		change();
	}
	
	private Tileset loadTileset(String id)
	{
		Loader l = EditorController.Instance.getLoader();
		Tileset ts = new Tileset();
		
		ts.load(l.loadData("tileset", id));
		
		return ts;
	}

	@Override
	public Node getUI()
	{
		return mUI.getNode();
	}
	
	@Override
	protected void change()
	{
		super.change();
		
		mTabMap.redraw();
		mTabMeta.redraw();
	}
	
	private class TilesetNameValidator extends EnterableTextField.Validator
	{
		@Override
		public String message()
		{
			return "Not an existing tileset!";
		}

		@Override
		public boolean isValid(String v)
		{
			return EditorController.Instance.getLoader().getFile("data", "tileset", v + ".json").exists();
		}
	}
}
