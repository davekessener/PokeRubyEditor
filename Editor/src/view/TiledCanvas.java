package view;

import java.util.Optional;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import lib.misc.Vec2;
import lib.mouse.MouseHandler;

public abstract class TiledCanvas extends Canvas implements CachedUI
{
	private int mTileSize;
	private int mWidth, mHeight;
	private Vec2 mSelected;
	private boolean mTrueClear;
	private Property<Boolean> mDrawGrid;
	private Property<Vec2> mMouseOverTile;
	private MouseHandler mMouseCallback;
	private boolean mDirty;
	
	public TiledCanvas(int w, int h, int ts)
	{
		super(w * ts, h * ts);
		mTileSize = ts;
		mWidth = w;
		mHeight = h;
		mDrawGrid = new SimpleBooleanProperty(false);
		mMouseOverTile = new SimpleObjectProperty<Vec2>();
		mTrueClear = false;
		mDirty = false;
		
		mDrawGrid.addListener((ob, o, n) -> draw());
		
		this.setOnMousePressed(e -> mousePressed(e));
		this.setOnMouseDragged(e -> mouseDrag(e));
		this.setOnMouseReleased(e -> mouseReleased(e));
		this.setOnMouseMoved(e -> mouseOver(e));
		
		draw();
	}
	
	protected int getTileSize() { return mTileSize; }
	public void setSelected(Vec2 s) { mSelected = s; draw(); }
	public Vec2 getSelected() { return mSelected; }
	public void setDrawGrid(boolean v) { mDrawGrid.setValue(v); }
	public void setTrueClear(boolean v) { mTrueClear = v; }
	
	public void setMouseHandler(MouseHandler h) { mMouseCallback = h; }
	
	public Property<Boolean> drawGridProperty() { return mDrawGrid; }
	public Property<Vec2> mousedOverTileProperty() { return mMouseOverTile; }
	
	public void setWidth(int w)
	{
		mWidth = w;
		super.setWidth(mWidth * mTileSize);
	}
	
	public void setHeight(int h)
	{
		mHeight = h;
		super.setHeight(mHeight * mTileSize);
	}
	
	public final void draw()
	{
		if(!mDirty)
		{
			mDirty = true;
			
			CacheUtils.ScheduleRenderIn(Duration.millis(5), this);
		}
	}
	
	@Override
	public boolean isDirty()
	{
		return mDirty;
	}
	
	@Override
	public void render()
	{
		GraphicsContext gc = this.getGraphicsContext2D();
		
		if(mTrueClear)
		{
			gc.clearRect(0, 0, this.getWidth(), this.getHeight());
		}
		else
		{
			gc.setFill(Color.BLACK);
			gc.fillRect(0, 0, this.getWidth(), this.getHeight());
		}

		drawTiles(gc);
		
		if(mDrawGrid.getValue())
		{
			drawGrid(gc);
		}
		
		if(mSelected != null)
		{
			RenderUtils.RenderBox(gc, mSelected, mTileSize, mDrawGrid.getValue());
		}
		
		mDirty = false;
	}
	
	protected void drawGrid(GraphicsContext gc)
	{
		gc.setStroke(Color.BLACK);
		
		for(int i = 0 ; i < mWidth ; ++i)
		{
			gc.strokeLine(i * mTileSize + 0.5, 0.5, i * mTileSize + 0.5, mHeight * mTileSize - 0.5);
		}
		
		for(int i = 0 ; i < mHeight ; ++i)
		{
			gc.strokeLine(0.5, i * mTileSize + 0.5, mWidth * mTileSize - 0.5, i * mTileSize + 0.5);
		}

		gc.strokeLine(mWidth * mTileSize - 0.5, 0.5, mWidth * mTileSize - 0.5, mHeight * mTileSize - 0.5);
		gc.strokeLine(0.5, mHeight * mTileSize - 0.5, mWidth * mTileSize - 0.5, mHeight * mTileSize - 0.5);
	}
	
	protected void mousePressed(MouseEvent e)
	{
		if(mMouseCallback != null)
		{
			translate(e).ifPresent(p -> mMouseCallback.onPressed(e, p));
		}
	}
	
	protected void mouseDrag(MouseEvent e)
	{
		if(mMouseCallback != null)
		{
			translate(e).ifPresent(p -> mMouseCallback.onDragged(e, p));
		}
	}
	
	protected void mouseReleased(MouseEvent e)
	{
		if(mMouseCallback != null)
		{
			mMouseCallback.onReleased(e);
		}
	}
	
	protected void mouseOver(MouseEvent e)
	{
		mMouseOverTile.setValue(translate(e).orElse(null));
	}
	
	protected Optional<Vec2> translate(MouseEvent e)
	{
		int x = (int) (e.getX() / mTileSize);
		int y = (int) (e.getY() / mTileSize);
		Vec2 r = null;

		if(x >= 0 && y >= 0 && x < mWidth && y < mHeight)
		{
			r = new Vec2(x, y);
		}
		
		return Optional.ofNullable(r);
	}
	
	protected void drawTiles(GraphicsContext gc)
	{
		for(int x = 0 ; x < mWidth ; ++x)
		{
			for(int y = 0 ; y < mHeight ; ++y)
			{
				drawTile(gc, x, y);
			}
		}
	}
	
	protected void drawTile(GraphicsContext gc, int x, int y) { }
	
	public static interface TileActivatedHandler { public abstract void onAction(MouseButton button, int x, int y); }
}
