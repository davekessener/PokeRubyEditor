package lib.tilemap;

import lib.Observable;
import model.ILayer;
import model.Layer;

public class ObservableLayer extends Observable implements ILayer
{
	private final Layer mLayer;
	
	public ObservableLayer(Layer l)
	{
		mLayer = l;
	}

	public void resize(int w, int h)
	{
		mLayer.resize(w, h);
		change();
	}
	
	public void set(int x, int y, String v)
	{
		mLayer.set(x, y, v);
		change();
	}
	
	@Override public int getWidth() { return mLayer.getWidth(); }
	@Override public int getHeight() { return mLayer.getHeight(); }
	@Override public String get(int x, int y) { return mLayer.get(x, y); }
}
