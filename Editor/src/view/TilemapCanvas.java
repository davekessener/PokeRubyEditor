package view;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

public class TilemapCanvas extends TiledCanvas
{
	private final TileRenderer mTileRenderer;
	private List<String[][]> mMap;
	private boolean mDrawOverlay, mDrawUpper;
	private int mCurrentLayer;
	private LeftClickHandler mOnLeftClick;
	private RightClickHandler mOnRightClick;
	
	public TilemapCanvas(int w, int h, int ts, TileRenderer r)
	{
		super(w, h, ts);
		mTileRenderer = r;
		mMap = new ArrayList<>();
		mDrawOverlay = false;
		mDrawUpper = true;
		mCurrentLayer = 0;
		
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
				if(mOnRightClick != null)
				{
					String s = mMap.get(mCurrentLayer)[x][y];
					
					if(s != null)
					{
						mOnRightClick.onRightClick(s);
					}
				}
			}
		});
	}
	
	public void addLayer(String[][] l)
	{
		mMap.add(l);
		mCurrentLayer = mMap.size() - 1;
	}
	
	public void setOnLeftClick(LeftClickHandler lcb) { mOnLeftClick = lcb; }
	public void setOnRightClick(RightClickHandler rcb) { mOnRightClick = rcb; }
	
	@Override
	protected void drawTile(GraphicsContext gc, int x, int y)
	{
		int s = getTileSize();
		
		for(int z = 0 ; z < mMap.size() ; ++z)
		{
			String[][] l = mMap.get(z);
			
			if(mDrawOverlay && z == mCurrentLayer)
			{
				gc.setFill(Color.rgb(255, 255, 255, 0.5));
				gc.fillRect(x * s + 0.5, y * s + 0.5, s + 0.5, s + 0.5);
			}
			
			if(l[x][y] != null)
			{
				mTileRenderer.drawTile(gc, l[x][y], x, y);
			}
			
			if(!mDrawUpper && z == mCurrentLayer)
			{
				break;
			}
		}
	}
	
	public static interface LeftClickHandler { public abstract void onLeftClick(int x, int y); }
	public static interface RightClickHandler { public abstract void onRightClick(String s); }
	public static interface TileRenderer { public abstract void drawTile(GraphicsContext gc, String id, int x, int y); }
}
