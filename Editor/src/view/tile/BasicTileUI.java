package view.tile;

import model.Vec2;

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
		
		mTileset.setOnTileActivated((b, x, y) -> onSelect(new Vec2(x, y)));
		
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
