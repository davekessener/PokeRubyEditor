package view;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lib.misc.Vec2;
import lib.observe.Observer;
import model.layer.ReadOnlyLayerManager;
import model.layer.ReadOnlyLayer;

public class TilesetCanvas extends TiledCanvas
{
	private final TileRenderer mTileRenderer;
	private final Observer mManagerObserver;
	private ReadOnlyLayerManager mMap;
	private Property<Boolean> mDrawOverlay, mDrawUpper;
	private int mCurrentLayer;
	
	public TilesetCanvas(ReadOnlyLayerManager mm, int ts, TileRenderer r)
	{
		super(mm.dimension().getX(), mm.dimension().getY(), ts);
		mTileRenderer = r;
		mMap = mm;
		mDrawOverlay = new SimpleBooleanProperty(false);
		mDrawUpper = new SimpleBooleanProperty(true);
		mCurrentLayer = -1;
		mManagerObserver = o -> draw();
		
		mDrawOverlay.addListener((ob, o, n) -> draw());
		mDrawUpper.addListener((ob, o, n) -> draw());
		mMap.addObserver(mManagerObserver);
		
		draw();
	}
	
	public Property<Boolean> drawOverlayProperty() { return mDrawOverlay; }
	public Property<Boolean> drawUpperProperty() { return mDrawUpper; }
	
	public void setActiveLayer(int i) { mCurrentLayer = i; draw(); }
	
	public void setManager(ReadOnlyLayerManager m)
	{
		mMap.deleteObserver(mManagerObserver);
		mMap = m;
		mMap.addObserver(mManagerObserver);
		
		draw();
	}
	
	@Override
	public void draw()
	{
		setWidth(mMap.dimension().getX());
		setHeight(mMap.dimension().getY());
		
		super.draw();
	}
	
	@Override
	protected void drawTile(GraphicsContext gc, int x, int y)
	{
		int s = getTileSize();
		
		for(int z = 0 ; z < mMap.size() ; ++z)
		{
			ReadOnlyLayer l = mMap.get(z);
			String t = l.get(new Vec2(x, y));
			
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

	public static interface TileRenderer { public abstract void drawTile(GraphicsContext gc, String id, int x, int y); }
}
