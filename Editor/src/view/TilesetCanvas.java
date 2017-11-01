package view;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import lib.tilemap.LayerManager;
import model.ILayer;

public class TilesetCanvas extends TiledCanvas
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
		mMap.addObserver(o -> redraw());

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
					String s = mMap.getLayer(mCurrentLayer).get(x, y);
					
					if(s != null)
					{
						mOnRightClick.onRightClick(s);
					}
				}
			}
		});
	}
	
	public Property<Boolean> drawOverlayProperty() { return mDrawOverlay; }
	public Property<Boolean> drawUpperProperty() { return mDrawUpper; }
	
	public void redraw() { draw(); }
	public void setActiveLayer(int i) { mCurrentLayer = i; redraw(); }
	
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
	
	public static interface LeftClickHandler { public abstract void onLeftClick(int x, int y); }
	public static interface RightClickHandler { public abstract void onRightClick(String s); }
	public static interface TileRenderer { public abstract void drawTile(GraphicsContext gc, String id, int x, int y); }
}
