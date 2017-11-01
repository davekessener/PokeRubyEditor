package view.tilemap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import controller.EditorController;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import model.Vec2;
import view.RenderUtils;
import view.TiledCanvas;

public class TilesetUI extends TiledCanvas
{
	private Image mSource;
	private List<String> mTileIDs;
	private List<Vec2> mTilePos;
	private int mWidth;
	private Callback mCallback;
	
	public TilesetUI(TilesetRenderer r, int w)
	{
		this(r.getSource(), r.getTileSize(), r.getTiles(), w);
		

		drawGridProperty().bind(EditorController.Instance.getOptions().drawGridProperty());
	}
	
	public TilesetUI(Image src, int ts, Map<String, Vec2> tiles, int w)
	{
		super(w, (tiles.size() + w - 1) / w, ts);
		mSource = src;
		mWidth = w;
		
		mTileIDs = new ArrayList<>();
		mTilePos = new ArrayList<>();
		
		for(Entry<String, Vec2> e : tiles.entrySet())
		{
			mTileIDs.add(e.getKey());
			mTilePos.add(e.getValue());
		}
		
		this.setOnTileActivated((b, x, y) -> {
			String s = getID(x + y * mWidth);
			
			setSelected(new Vec2(x, y));

			if(mCallback != null && s != null)
			{
				mCallback.onSelect(s);
			}
		});
	}
	
	public void setSelected(String id)
	{
		int i = mTileIDs.indexOf(id);
		super.setSelected(new Vec2(i % mWidth, i / mWidth));
	}
	
	public String getID(int i) { return i < mTileIDs.size() ? mTileIDs.get(i) : null; }
	public void setOnSelect(Callback cb) { mCallback = cb; }
	
	@Override
	protected void drawTile(GraphicsContext gc, int x, int y)
	{
		int i = x + y * mWidth;
		
		if(i < mTilePos.size())
		{
			RenderUtils.RenderTile(gc, mSource, getTileSize(), mTilePos.get(i), new Vec2(x, y));
		}
	}
	
	public static interface Callback { public abstract void onSelect(String id); }
}
