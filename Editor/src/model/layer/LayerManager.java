package model.layer;

public interface LayerManager extends ReadOnlyLayerManager
{
	@Override public abstract Layer get(int i);
	
	public abstract void add(int i, Layer l);
	public abstract void remove(int i);
	
	public default void add(Layer l) { add(size(), l); }
}
