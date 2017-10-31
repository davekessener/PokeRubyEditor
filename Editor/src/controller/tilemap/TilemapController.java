package controller.tilemap;

import com.eclipsesource.json.JsonValue;

import controller.ContentController;
import controller.EditorController;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import model.Loader;
import model.Tilemap;
import model.Tileset;
import view.tilemap.TabData;
import view.tilemap.TabMeta;
import view.tilemap.TilemapUI;
import view.TilemapCanvas.TileRenderer;;

public class TilemapController implements ContentController, TileRenderer
{
	private TilemapUI mUI;
	private Tilemap mTilemap;
	private Tileset mTileset;
	private Property<Boolean> mChanged;
	private TabMeta mTabMeta;
	private TabData mTabData;
	
	public TilemapController(Tilemap tm)
	{
		mTilemap = tm;
		mTileset = loadTileset(mTilemap.getTilesetID());
		mChanged = new SimpleBooleanProperty(false);
		mTabMeta = new TabMeta(mTilemap.getMeta(), mTileset.getSize(), this);
		mTabData = new TabData(mTilemap.getTilesetID(), mTilemap.getWidth(), mTilemap.getHeight());
		
		mUI = new TilemapUI(new Group(), mTabMeta.getNode(), mTabData.getNode());
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
	public Property<Boolean> needsSave()
	{
		return mChanged;
	}

	@Override
	public JsonValue save()
	{
		mChanged.setValue(false);
		
		return mTilemap.save();
	}

	@Override
	public void drawTile(GraphicsContext gc, String id, int x, int y)
	{
		int s = mTileset.getSize();
		gc.setFill(mTabMeta.getColorOfMeta(id));
		gc.fillRect(x * s + 0.5, y * s + 0.5, s + 0.5, s + 0.5);
	}
}
