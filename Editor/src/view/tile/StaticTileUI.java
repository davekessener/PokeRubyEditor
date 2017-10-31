package view.tile;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import model.Vec2;
import view.UI;
import view.TiledCanvas.TileActivatedHandler;

public class StaticTileUI implements UI
{
	private BorderPane mRoot;
	private TilesetUI mTileset;
	private TextField mID;
	
	public StaticTileUI(Image src, int ts, String id, Vec2 p)
	{
		mRoot = new BorderPane();
		mTileset = new TilesetUI(src, ts);
		mID = new TextField(id);
		
		ScrollPane pane = new ScrollPane();
		HBox hbox = new HBox();
		
		Label lbl = new Label(" ID: ");
		hbox.getChildren().addAll(lbl, mID);
		pane.setContent(mTileset);
		mRoot.setTop(hbox);
		mRoot.setCenter(pane);
		lbl.prefHeightProperty().bind(mID.heightProperty());

		mID.setOnKeyPressed(e -> { if(e.getCode().equals(KeyCode.ENTER)) mTileset.requestFocus(); });
		
		select(p);
	}
	
	public void select(Vec2 p)
	{
		mTileset.setSelected(p);
	}
	
	public void setOnSelect(TileActivatedHandler h)
	{
		mTileset.setOnTileActivated(h);
	}
	
	public void setOnIDChange(StringChangeListener cb)
	{
		mID.focusedProperty().addListener((ob, o, n) -> { if(!n) cb.onChange(mID.getText()); });
	}
	
	@Override
	public Parent getNode()
	{
		return mRoot;
	}
	
	public static interface StringChangeListener { public abstract void onChange(String id); }
}
