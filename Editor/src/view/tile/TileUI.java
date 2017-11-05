package view.tile;

import model.Tileset.Type;
import view.UI;

public interface TileUI extends UI
{
	public abstract void bind(TiledImageView tileset);
	public abstract Type getType();
}
