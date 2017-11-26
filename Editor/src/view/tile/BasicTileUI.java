package view.tile;

import lib.misc.Vec2;
import lib.mouse.SimpleMouseHandler;

public abstract class BasicTileUI implements TileUI
{
	private TiledImageView mTileset;
	private OnSelectionCallback mOnSelection;
	
	public BasicTileUI()
	{
	}
	
	public void setOnSelection(OnSelectionCallback cb) { mOnSelection = cb; }

	@Override
	public void bind(TiledImageView tileset)
	{
		mTileset = tileset;
		
		mTileset.setMouseHandler(new SimpleMouseHandler((e, p) -> onSelect(p)));
		
		onBind();
	}
	
	public void selectTile(Vec2 p)
	{
		mTileset.setSelected(p);
	}
	
	protected void onBind()
	{
	}
	
	protected void onSelect(Vec2 p)
	{
		if(mOnSelection != null)
		{
			mOnSelection.onSelect(p);
		}
	}
	
	public static interface OnSelectionCallback { public abstract void onSelect(Vec2 p); }
}
