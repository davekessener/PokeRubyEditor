package view;

import java.util.HashSet;
import java.util.Set;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import lib.IObservable;
import lib.Observer;
import lib.tilemap.LayerManager;
import model.Direction;
import model.ILayer;
import model.Vec2;

public class TilesetCanvas extends TiledCanvas implements Observer
{
	private final TileRenderer mTileRenderer;
	private LayerManager mMap;
	private Property<Boolean> mDrawOverlay, mDrawUpper;
	private int mCurrentLayer;
	private LeftClickHandler mOnLeftClick;
	private RightClickHandler mOnRightClick;
	
	public TilesetCanvas(LayerManager mm, int ts, TileRenderer r)
	{
		super(mm.getWidth(), mm.getHeight(), ts);
		mTileRenderer = r;
		mMap = mm;
		mDrawOverlay = new SimpleBooleanProperty(false);
		mDrawUpper = new SimpleBooleanProperty(true);
		mCurrentLayer = -1;
		
		mDrawOverlay.addListener((ob, o, n) -> redraw());
		mDrawUpper.addListener((ob, o, n) -> redraw());
		mMap.addObserver(this);

		this.setOnTileActivated((b, x, y) -> {
			if(b.equals(MouseButton.PRIMARY))
			{
				if(mOnLeftClick != null)
				{
					mOnLeftClick.onLeftClick(x, y);
				}
			}
			else if(b.equals(MouseButton.SECONDARY))
			{
				if(mOnRightClick != null && mCurrentLayer >= 0)
				{
					mOnRightClick.onRightClick(mMap.getLayer(mCurrentLayer).get(x, y));
				}
			}
			else if(b.equals(MouseButton.MIDDLE))
			{
				if(mOnLeftClick != null)
				{
					Vec2 o = new Vec2(x, y);
					
					for(Vec2 p : findAll(o, new HashSet<>(), getTileID(o)))
					{
						mOnLeftClick.onLeftClick(p.getX(), p.getY());
					}
				}
			}
		});
	}
	
	public Property<Boolean> drawOverlayProperty() { return mDrawOverlay; }
	public Property<Boolean> drawUpperProperty() { return mDrawUpper; }
	
	public void setActiveLayer(int i) { mCurrentLayer = i; redraw(); }
	public void setManager(LayerManager m) { mMap.deleteObserver(this); mMap = m; m.addObserver(this); }
	
	public void redraw()
	{
		setWidth(mMap.getWidth());
		setHeight(mMap.getHeight());
		draw();
	}
	
	public void setOnLeftClick(LeftClickHandler lcb) { mOnLeftClick = lcb; }
	public void setOnRightClick(RightClickHandler rcb) { mOnRightClick = rcb; }
	
	@Override
	protected void drawTile(GraphicsContext gc, int x, int y)
	{
		int s = getTileSize();
		
		for(int z = 0 ; z < mMap.size() ; ++z)
		{
			ILayer l = mMap.getLayer(z);
			String t = l.get(x, y);
			
			if(mDrawOverlay.getValue() && z == mCurrentLayer)
			{
				gc.setFill(Color.rgb(255, 255, 255, 0.75));
				gc.fillRect(x * s, y * s, s, s);
			}
			
			if(t != null)
			{
				mTileRenderer.drawTile(gc, t, x, y);
			}
			
			if(!mDrawUpper.getValue() && z == mCurrentLayer)
			{
				break;
			}
		}
	}
	
	private String getTileID(Vec2 p)
	{
		return mMap.getLayer(mCurrentLayer).get(p.getX(), p.getY());
	}
	
	private static boolean Equals(String s1, String s2)
	{
		return s1 == null ? s2 == null : s1.equals(s2);
	}
	
	private boolean isContained(Vec2 p)
	{
		return p.getX() >= 0 && p.getY() >= 0 && p.getX() < mMap.getWidth() && p.getY() < mMap.getHeight();
	}
	
	private Set<Vec2> findAll(Vec2 p, Set<Vec2> s, String t)
	{
		if(!s.contains(p) && Equals(getTileID(p), t))
		{
			s.add(p);
			
			for(Direction d : Direction.values())
			{
				Vec2 q = p.add(d.distance);
				
				if(isContained(q))
				{
					findAll(q, s, t);
				}
			}
		}
		
		return s;
	}
	
	@Override
	public void onChange(IObservable o)
	{
		redraw();
	}
	
	public static interface LeftClickHandler { public abstract void onLeftClick(int x, int y); }
	public static interface RightClickHandler { public abstract void onRightClick(String s); }
	public static interface TileRenderer { public abstract void drawTile(GraphicsContext gc, String id, int x, int y); }
}
