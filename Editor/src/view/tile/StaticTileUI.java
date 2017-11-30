package view.tile;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import lib.misc.Vec2;
import model.Tileset.Type;

public class StaticTileUI extends BasicTileUI
{
	private final Pane mRoot;
	private Vec2 mPos;
	
	public StaticTileUI(Vec2 p, String anims)
	{
		super(anims);
		
		mRoot = new Pane();
		mPos = p;
		
		mRoot.setPadding(new Insets(0D, 0D, 0D, 5D));
		
		mRoot.getChildren().add(super.getRoot());
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
