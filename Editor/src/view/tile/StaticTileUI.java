package view.tile;

import java.util.function.Consumer;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import lib.EnterableTextField;
import lib.misc.Vec2;
import model.Tileset.Type;

public class StaticTileUI extends BasicTileUI
{
	private final GridPane mRoot;
	private Vec2 mPos;
	private Consumer<String> mOnAnimatorChange;
	
	public StaticTileUI(Vec2 p, String anims)
	{
		mRoot = new GridPane();
		mPos = p;
		
		EnterableTextField tfAnims = new EnterableTextField(anims);
		
		tfAnims.setCallback(s -> changeAnimator(s));
		
		mRoot.addRow(0, new Label("Animator"), tfAnims);
		
		mRoot.setHgap(5D);
		mRoot.setPadding(new Insets(0D, 0D, 0D, 5D));
		GridPane.setHgrow(tfAnims, Priority.ALWAYS);
	}
	
	public void setOnAnimatorChange(Consumer<String> cb) { mOnAnimatorChange = cb; }
	
	private void changeAnimator(String s)
	{
		if(mOnAnimatorChange != null)
		{
			mOnAnimatorChange.accept(s);
		}
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
