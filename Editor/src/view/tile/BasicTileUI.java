package view.tile;

import java.util.function.Consumer;

import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import lib.EnterableTextField;
import lib.misc.Vec2;
import lib.mouse.SimpleMouseHandler;

public abstract class BasicTileUI implements TileUI
{
	private final GridPane mRoot;
	private TiledImageView mTileset;
	private OnSelectionCallback mOnSelection;
	private Consumer<String> mOnAnimator;
	
	public BasicTileUI(String anims)
	{
		mRoot = new GridPane();
		
		EnterableTextField tfAnims = new EnterableTextField(anims);
		
		tfAnims.setCallback(s -> changeAnimator(s));
		
		mRoot.setHgap(5D);
		mRoot.addRow(0, new Label("Animator"), tfAnims);
		mRoot.setPadding(new Insets(0D, 5D, 0D, 5D));

		GridPane.setHgrow(tfAnims, Priority.ALWAYS);
		mRoot.getChildrenUnmodifiable().forEach(ch -> GridPane.setValignment(ch, VPos.CENTER)); // TODO !!!
	}
	
	public void setOnSelection(OnSelectionCallback cb) { mOnSelection = cb; }
	public void setOnAnimatorChange(Consumer<String> cb) { mOnAnimator = cb; }
	
	protected Node getRoot() { return mRoot; }

	private void changeAnimator(String a)
	{
		if(mOnAnimator != null)
		{
			mOnAnimator.accept(a);
		}
	}

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
