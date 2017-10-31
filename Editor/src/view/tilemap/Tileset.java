package view.tilemap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import model.Vec2;
import view.RenderUtils;
import view.TiledCanvas;

public class Tileset extends TiledCanvas
{
	private Image mSource;
	private List<String> mTileIDs;
	private List<Vec2> mTilePos;
	private int mWidth;
	private Callback mCallback;
	
	public Tileset(Image src, int ts, Map<String, Vec2> tiles, int w)
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
			int i = x + y * mWidth;
			if(mCallback != null && i < mTileIDs.size())
			{
				mCallback.onSelect(mTileIDs.get(i));
			}
		});
	}
	
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
