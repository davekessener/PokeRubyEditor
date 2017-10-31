package controller.tile;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;
import model.Tileset.AnimatedTile;
import view.tile.AnimatedTileUI;
import model.Vec2;

public class AnimatedTileEditor implements TileEditController
{
	private Property<Boolean> mChanged;
	private AnimatedTileUI mTileUI;
	private AnimatedTile mTile;
	private Runnable mOnChange;
	
	public AnimatedTileEditor(Image src, int ts, AnimatedTile t, Runnable onChange)
	{
		mChanged = new SimpleBooleanProperty(false);
		mTileUI = new AnimatedTileUI(src, ts);
		mTile = t;
		mOnChange = onChange;
		
		mTileUI.setFrameCount(t.getFrameCount());
		mTileUI.setPeriod(mTile.getPeriod());
		selectFrame(0);
		
		mTileUI.setOnActionHandler((b, x, y) -> selectTile(x, y));
		mTileUI.setOnAddFrameNext(() -> addFrame(mTileUI.getSelectedFrame() + 1));
		mTileUI.setOnAddFramePrev(() -> addFrame(mTileUI.getSelectedFrame()));
		mTileUI.setOnDeleteFrame(() -> deleteFrame(mTileUI.getSelectedFrame()));
		mTileUI.setOnPeriodChange(p -> changePeriod(p));
		mTileUI.setOnSelectFrame(f -> selectFrame(f));
	}
	
	private void change()
	{
		mChanged.setValue(true);
		mOnChange.run();
		mTileUI.setDeleteEnable(mTile.getFrameCount() > 1);
	}
	
	private void selectTile(int x, int y)
	{
		Vec2 p = new Vec2(x, y);
		int i = mTileUI.getSelectedFrame();
		
		if(!mTile.getFrame(i).equals(p))
		{
			mTile.setFrame(i, p);
			change();
		}
	}
	
	private void addFrame(int idx)
	{
		Vec2 p = new Vec2(0, 0);
		mTile.addFrame(idx, p);
		mTileUI.setFrameCount(mTile.getFrameCount());
		mTileUI.selectFrame(idx, p);
		change();
	}
	
	private void deleteFrame(int idx)
	{
		mTile.removeFrame(idx);
		if(idx > mTile.getFrameCount()) --idx;
		mTileUI.setFrameCount(mTile.getFrameCount());
		mTileUI.selectFrame(idx, mTile.getFrame(idx));
		change();
	}
	
	private void changePeriod(int p)
	{
		if(p > 0)
		{
			mTile.setPeriod(p);
			change();
		}
		else
		{
			mTile.setPeriod(mTile.getPeriod());
		}
	}
	
	private void selectFrame(int idx)
	{
		mTileUI.selectFrame(idx, mTile.getFrame(idx));
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
