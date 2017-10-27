package view;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import model.Vec2;

public class Tilemap implements UI
{
	private Group mRoot;
	private Canvas mCanvas;
	private String[][] mTiles;
	private Tileset mTileset;
	private int mWidth, mHeight;
	
	public Tilemap(Tileset ts, int w, int h) { this(ts, w, h, null); }
	public Tilemap(Tileset tileset, int w, int h, EventHandler<? super MouseEvent> cb)
	{
		mRoot = new Group();
		mCanvas = new Canvas(w * tileset.getTileSize(), h * tileset.getTileSize());
		mTiles = new String[w][h];
		mTileset = tileset;
		mWidth = w;
		mHeight = h;
		
		if(cb != null) mCanvas.setOnMouseEntered(cb);
		
		mRoot.getChildren().add(mCanvas);
	}
	
	public void setTile(Vec2 p, String s) { mTiles[p.getX()][p.getY()] = s; }
	public String getTile(Vec2 p) { return mTiles[p.getX()][p.getY()]; }
	
	public void draw(Vec2 select)
	{
		GraphicsContext gc = mCanvas.getGraphicsContext2D();
		
		gc.clearRect(0, 0, mCanvas.getWidth(), mCanvas.getHeight());
		
		for(int x = 0 ; x < mWidth ; ++x)
		{
			for(int y = 0 ; y < mHeight ; ++y)
			{
				if(mTiles[x][y] != null)
				{
					mTileset.draw(gc, mTiles[x][y], new Vec2(x, y));
				}
			}
		}
		
		int s = mTileset.getTileSize();
		gc.setStroke(Color.RED);
		gc.strokeRect(select.getX() * s + 0.5, select.getY() * s + 0.5, s, s);
	}
	
	@Override
	public Parent getNode()
	{
		return mRoot;
	}
}
