package controller.tile;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;
import model.Tileset.StaticTile;
import view.tile.StaticTileUI;
import model.Vec2;

public class StaticTileEditor implements TileEditController
{
	private StaticTileUI mTileUI;
	private Property<Boolean> mChanged;
	private StaticTile mTile;
	private Runnable mOnChange;
	
	public StaticTileEditor(Image src, int tilesize, StaticTile t, Runnable onChange)
	{
		mTileUI = new StaticTileUI(src, tilesize, t.getID(), t.getPosition());
		mChanged = new SimpleBooleanProperty(false);
		mTile = t;
		mOnChange = onChange;
		
		mTileUI.setOnSelect((b, x, y) -> select(x, y));
		mTileUI.setOnIDChange(id -> changeID(id));
	}
	
	private void changeID(String ID)
	{
		mTile.setID(ID);
		mChanged.setValue(true);
		mOnChange.run();
	}
	
	private void select(int x, int y)
	{
		Vec2 p = new Vec2(x, y);
		
		if(!p.equals(mTile.getPosition()))
		{
			mTile.setPosition(p);
			mTileUI.select(p);
			mChanged.setValue(true);
			mOnChange.run();
		}
	}

	@Override
	public Property<Boolean> changed()
	{
		return mChanged;
	}

	@Override
	public Node getUI()
	{
		return mTileUI.getNode();
	}
}
