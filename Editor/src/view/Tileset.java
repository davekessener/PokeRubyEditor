package view;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import model.Vec2;

public class Tileset
{
	private Image mSource;
	private int mSize;
	private Map<String, Vec2> mTiles;
	
	public Tileset(Image source, int size)
	{
		mSource = source;
		mSize = size;
		mTiles = new HashMap<>();
	}
	
	public int getTileSize() { return mSize; }
	public int getSize() { return mTiles.size(); }
	
	public void addTile(String id, Vec2 position)
	{
		mTiles.put(id, position);
	}
	
	public void draw(GraphicsContext gc, String tile, Vec2 pos)
	{
		Vec2 src = mTiles.get(tile);
		gc.drawImage(mSource, src.getX() * mSize, src.getY() * mSize, mSize, mSize, pos.getX() * mSize, pos.getY() * mSize, mSize, mSize);
	}
}
