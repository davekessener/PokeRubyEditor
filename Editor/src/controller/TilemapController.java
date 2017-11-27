package controller;

import javafx.scene.Node;
import lib.Utils;
import lib.misc.Vec2;
import lib.misc.record.BackupRecorder;
import lib.misc.record.JsonBackup;
import model.Tilemap;
import model.Tileset;
import model.layer.BasicMapLayerManager;
import model.layer.MapManager;
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
	private final MapManager mLayers;
	private final TilesetRenderer mRenderTileset;
	private final TabMap mTabMap;
	private final TabMeta mTabMeta;
	private final TabData mTabData;
	private final BackupRecorder mActionStack;
	
	public TilemapController(String tid, Tilemap tm)
	{
		super(tm);
		mUI = new TabbedUI();
		mTilemap = tm;
		mTileset = Utils.loadTileset(mTilemap.getTilesetID());
		mLayers = new BasicMapLayerManager(mTilemap);
		mRenderTileset = new TilesetRenderer(mTileset);
		mActionStack = new BackupRecorder(new JsonBackup(mTilemap));
		
		mActionStack.start();
		
		Runnable save = () -> actionPerformed();
		
		mTabMap = new TabMap(mTilemap, mLayers, mTileset.getSize(), mRenderTileset, save);
		mTabMeta = new TabMeta(mTilemap.getMeta(), mTileset.getSize(), new TilesetCanvas(mLayers, mTileset.getSize(), mRenderTileset), save);
		mTabData = new TabData(mTilemap.getTilesetID(), mTilemap.getWidth(), mTilemap.getHeight());
		
		mLayers.addObserver(o -> change());
		
		mTabData.setOnDimensionChange((w, h) -> resize(w, h));
		
		mUI.addTab("Tilemap", mTabMap).addTab("Meta", mTabMeta).addTab("Data", mTabData);
	}
	
	private void actionPerformed()
	{
		act(mActionStack.stop());
		mActionStack.start();
	}
	
	private void resize(int w, int h)
	{
		mLayers.resizeAll(new Vec2(w, h));
		
		actionPerformed();
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
