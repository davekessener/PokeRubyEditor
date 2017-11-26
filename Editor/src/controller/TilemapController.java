package controller;

import javafx.scene.Node;
import lib.IDValidator;
import lib.Utils;
import lib.misc.Vec2;
import model.Tilemap;
import model.Tileset;
import model.layer.MapLayerManager;
import view.TabbedUI;
import view.TilesetCanvas;
import view.tilemap.TabData;
import view.tilemap.TabMap;
import view.tilemap.TabMeta;
import view.tilemap.TilesetRenderer;

public class TilemapController extends ContentController
{
	private final TabbedUI mUI;
	private final Tilemap mTilemap;
	private final Tileset mTileset;
	private final MapLayerManager mLayers;
	private final TilesetRenderer mRenderTileset;
	private final TabMap mTabMap;
	private final TabMeta mTabMeta;
	private final TabData mTabData;
	
	public TilemapController(String tid, Tilemap tm)
	{
		super(tm);
		mUI = new TabbedUI();
		mTilemap = tm;
		mTileset = Utils.loadTileset(mTilemap.getTilesetID());
		mLayers = new MapLayerManager(mTilemap);
		mRenderTileset = new TilesetRenderer(mTileset);
		
		mTabMap = new TabMap(mTilemap, mLayers, mTileset.getSize(), mRenderTileset);
		mTabMeta = new TabMeta(mTilemap.getMeta(), mTileset.getSize(), new TilesetCanvas(mLayers, mTileset.getSize(), mRenderTileset));
		mTabData = new TabData(mTilemap.getTilesetID(), mTilemap.getWidth(), mTilemap.getHeight());
		
		mLayers.addObserver(o -> change());
		mTabMap.setOnAction(a -> act(a));
		mTabMeta.setOnChange((x, y, id) -> setMeta(new Vec2(x, y), id));
		mTabData.addTilesetValidation(new IDValidator("tileset"));
		mTabData.setOnDimensionChange((w, h) -> resize(w, h));
		
		mUI.addTab("Tilemap", mTabMap).addTab("Meta", mTabMeta).addTab("Data", mTabData);
	}
	
	private void setMeta(Vec2 p, String id)
	{
		String m = mTilemap.getMeta().get(p);
		
		act(
			() -> mTilemap.getMeta().set(p, id),
			() -> mTilemap.getMeta().set(p, m));
	}
	
	private void resize(int w, int h)
	{
		// TODO proper undo support
		mLayers.resizeAll(new Vec2(w, h));
		mTabMeta.refreshMap(mTilemap.getMeta());
		change();
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
		
		mTabMap.draw();
		mTabMeta.draw();
	}
}
