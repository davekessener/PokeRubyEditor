package view.tilemap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import controller.EditorController;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import lib.misc.Vec2;
import lib.mouse.SimpleMouseHandler;
import view.RenderUtils;
import view.TiledCanvas;

public class TilesetView extends TiledCanvas
{
	private Image mSource;
	private List<String> mTileIDs;
	private List<Vec2> mTilePos;
	private int mWidth;
	private TileView mTiles;
	private Consumer<String> mOnSelect;
	
	public TilesetView(TilesetRenderer r, int w)
	{
		this(r.getSource(), r.getTileSize(), new BasicMapWrapper(r.getTiles()), w);
	}
	
	public TilesetView(Image src, int ts, TileView tiles, int w)
	{
		super(w, (tiles.keySet().size() + w) / w, ts);
		mTiles = tiles;
		mSource = src;
		mWidth = w;
		
		mTileIDs = new ArrayList<>();
		mTilePos = new ArrayList<>();
		
		draw();
		
		super.setSelected(new Vec2(0, 0));
		super.setMouseHandler(new SimpleMouseHandler((e, p) -> onSelect(p)));
		
		drawGridProperty().bind(EditorController.Instance.getOptions().drawGridProperty());
	}
	
	public void setOnSelect(Consumer<String> cb) { mOnSelect = cb; }
	
	private void onSelect(Vec2 p)
	{
		setSelected(p);
		
		if(mOnSelect != null)
		{
			mOnSelect.accept(getSelectedID());
		}
	}

	public int getSelectedIndex()
	{
		return getIndexOf(getSelected());
	}
	
	public void setSelectedIndex(int idx)
	{
		if(idx < 0 || idx >= mTileIDs.size()) idx = 0;
		
		setSelected(new Vec2(idx % mWidth, idx / mWidth));
	}
	
	@Override
	public void draw()
	{
		mTileIDs.clear();
		mTilePos.clear();

		mTileIDs.add(null);
		mTilePos.add(null);
		
		for(String id : mTiles.keySet())
		{
			mTileIDs.add(id);
			mTilePos.add(mTiles.get(id));
		}
		
		super.setHeight((mTiles.keySet().size() + mWidth) / mWidth);
		
		super.draw();
	}
	
	public void selectID(String id)
	{
		int i = mTileIDs.indexOf(id);
		super.setSelected(new Vec2(i % mWidth, i / mWidth));
	}
	
	public String getSelectedID()
	{
		return getID(getSelectedIndex());
	}
	
	public int getIndexOf(Vec2 p) { return p == null ? -1 : p.getX() + p.getY() * mWidth; }
	public String getID(int i) { return i >= 0 && i < mTileIDs.size() ? mTileIDs.get(i) : null; }
	public String getID(Vec2 p) { return getID(getIndexOf(p)); }
	
	@Override
	protected void drawTile(GraphicsContext gc, int x, int y)
	{
		int i = x + y * mWidth;
		
		if(i > 0 && i < mTilePos.size())
		{
			RenderUtils.RenderTile(gc, mSource, getTileSize(), mTilePos.get(i), new Vec2(x, y));
		}
	}
	
	public static interface TileView
	{
		public abstract Vec2 get(String id);
		public abstract Set<String> keySet();
	}
	
	public static class MapWrapper<T> implements TileView
	{
		private final Map<String, T> mMap;
		private final Function<T, Vec2> mConverter;

		public MapWrapper(Map<String, T> map, Function<T, Vec2> c)
		{
			mMap = map;
			mConverter = c;
		}

		@Override
		public Vec2 get(String id)
		{
			return mConverter.apply(mMap.get(id));
		}

		@Override
		public Set<String> keySet()
		{
			return mMap.keySet();
		}
	}
	
	public static class BasicMapWrapper extends MapWrapper<Vec2>
	{
		public BasicMapWrapper(Map<String, Vec2> m)
		{
			super(m, o -> o);
		}
	}
}
