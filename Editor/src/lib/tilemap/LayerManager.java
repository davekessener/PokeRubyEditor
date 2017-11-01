package lib.tilemap;

import lib.Observable;
import model.ILayer;

public abstract class LayerManager extends Observable
{
	public abstract int getWidth();
	public abstract int getHeight();
	public abstract int size();
	public abstract ILayer getLayer(int i);
}
