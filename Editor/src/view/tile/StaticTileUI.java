package view.tile;

import javafx.scene.Group;
import javafx.scene.Parent;
import lib.misc.Vec2;
import model.Tileset.Type;

public class StaticTileUI extends BasicTileUI
{
	private final Group mRoot;
	private Vec2 mPos;
	
	public StaticTileUI(Vec2 p)
	{
		mRoot = new Group();
		mPos = p;
	}

	@Override
	protected void onSelect(Vec2 p)
	{
		this.selectTile(p);
		super.onSelect(p);
	}
	
	@Override
	public void onBind()
	{
		this.selectTile(mPos);
	}

	@Override
	public Parent getNode()
	{
		return mRoot;
	}
	
	@Override
	public Type getType()
	{
		return Type.STATIC;
	}
}
