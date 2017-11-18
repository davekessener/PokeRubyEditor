package view;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import model.Vec2;

public abstract class TiledCanvas extends Canvas
{
	private int mTileSize;
	private int mWidth, mHeight;
	private Vec2 mSelected;
	private TileActivatedHandler mCallback;
	private boolean mTrueClear;
	private Property<Boolean> mDrawGrid;
	private Property<String> mMouseOverTile; // TODO remove that shit!
	
	public TiledCanvas(int w, int h, int s)
	{
		super(w * s, h * s);
		mTileSize = s;
		mWidth = w;
		mHeight = h;
		mSelected = null;
		mCallback = null;
		mDrawGrid = new SimpleBooleanProperty(false);
		mMouseOverTile = new SimpleStringProperty("");
		mTrueClear = false;
		
		mDrawGrid.addListener((ob, o, n) -> draw());
		
		this.setOnMousePressed(e -> mouseClick(e));
		this.setOnMouseDragged(e -> mouseClick(e));
		this.setOnMouseMoved(e -> mouseOver(e));
	}
	
	protected int getTileSize() { return mTileSize; }
	public void setSelected(Vec2 s) { mSelected = s; draw(); }
	public Vec2 getSelected() { return mSelected; }
	public void setDrawGrid(boolean v) { mDrawGrid.setValue(v); }
	public void setTrueClear(boolean v) { mTrueClear = v; }
	
	public void setOnTileActivated(TileActivatedHandler h) { mCallback = h; }
	
	public Property<Boolean> drawGridProperty() { return mDrawGrid; }
	public Property<String> mousedOverTileProperty() { return mMouseOverTile; }
	
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
	
	public void draw()
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
	
	protected void mouseClick(MouseEvent e)
	{
		Vec2 p = translate(e);
		
		if(p != null)
		{
			onTileActivated(e.getButton(), p.getX(), p.getY());
		}
	}
	
	protected void mouseOver(MouseEvent e)
	{
		Vec2 p = translate(e);
		
		mMouseOverTile.setValue(p == null ? "" : ("X: " + p.getX() + ", Y: " + p.getY()));
	}
	
	protected Vec2 translate(MouseEvent e)
	{
		int x = (int) (e.getX() / mTileSize);
		int y = (int) (e.getY() / mTileSize);
		Vec2 r = null;

		if(x >= 0 && y >= 0 && x < mWidth && y < mHeight)
		{
			r = new Vec2(x, y);
		}
		
		return r;
	}
	
	protected void onTileActivated(MouseButton b, int x, int y)
	{
		if(mCallback != null)
		{
			mCallback.onAction(b, x, y);
		}
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
