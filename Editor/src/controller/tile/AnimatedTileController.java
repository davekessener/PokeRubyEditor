package controller.tile;

import controller.tile.TileController.TileWriter;
import lib.misc.Vec2;
import lib.observe.ObservableList;
import model.Tileset.AnimatedTile;
import view.tile.AnimatedTileUI;
import view.tile.TileUI;

public class AnimatedTileController extends BasicTileController
{
	private final AnimatedTile mTile;
	private final AnimatedTileUI mUI;
	private final TileWriter mWriter;
	private final ObservableList<Vec2> mVertices;
	
	public AnimatedTileController(AnimatedTile t, TileWriter w)
	{
		super(t, w);
		
		mTile = t;
		mWriter = w;
		mVertices = ObservableList.Instantiate(t.getFrames());
		mUI = new AnimatedTileUI(mVertices, t.getPeriod(), t.getAnimators());
		
		mUI.setOnSelection(p -> setSelection(p));
		mUI.setOnAddFrame(idx -> addFrame(idx));
		mUI.setOnRemoveFrame(idx -> removeFrame(idx));
		mUI.setOnPeriodChange(p -> changePeriod(p));
		
		mVertices.addObserver(o -> write());
		
		super.registerHandler(mUI);
	}
	
	private void addFrame(int idx)
	{
		mVertices.add(idx, new Vec2(0, 0));
		mUI.setSelectedFrame(idx);
	}
	
	private void removeFrame(int idx)
	{
		if(mUI.getSelectedFrame() >= idx)
		{
			mUI.setSelectedFrame(mUI.getSelectedFrame() - 1);
		}
		
		mVertices.remove(idx);
	}
	
	private void changePeriod(int p)
	{
		mTile.setPeriod(p);
		
		write();
	}
	
	private void setSelection(Vec2 p)
	{
		mVertices.set(mUI.getSelectedFrame(), p);
		mUI.selectTile(p);
	}
	
	private void write()
	{
		mWriter.write(mTile);
	}

	@Override
	public TileUI getUI()
	{
		return mUI;
	}
}
