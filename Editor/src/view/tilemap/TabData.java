package view.tilemap;

import java.util.function.Consumer;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import lib.EnterableTextField;
import view.UI;

public class TabData implements UI
{
	private GridPane mRoot;
	private EnterableTextField mWidth, mHeight;
	private EnterableTextField mTileset;
	
	public TabData(String ts, int w, int h)
	{
		mRoot = new GridPane();

		Label lWidth = new Label("Width");
		Label lHeight = new Label("Height");
		Label lTileset = new Label("Tileset");
		
		mWidth = new EnterableTextField("" + w);
		mHeight = new EnterableTextField("" + h);
		mTileset = new EnterableTextField(ts);
		
		mWidth.addValidations(EnterableTextField.IS_POSITIVE_INT);
		mHeight.addValidations(EnterableTextField.IS_POSITIVE_INT);
		mTileset.addValidations(EnterableTextField.IS_NOT_EMPTY);

		lWidth.setAlignment(Pos.CENTER_RIGHT);
		lHeight.setAlignment(Pos.CENTER_RIGHT);
		lTileset.setAlignment(Pos.CENTER_RIGHT);

		lWidth.prefHeightProperty().bind(mWidth.heightProperty());
		lHeight.prefHeightProperty().bind(mHeight.heightProperty());
		lTileset.prefHeightProperty().bind(mTileset.heightProperty());
		
		mRoot.add(lTileset, 0, 0);
		mRoot.add(mTileset, 1, 0);
		mRoot.add(lWidth, 0, 1);
		mRoot.add(mWidth, 1, 1);
		mRoot.add(lHeight, 0, 2);
		mRoot.add(mHeight, 1, 2);

		mRoot.setHgap(5D);
		mRoot.setVgap(5D);
	}
	
	public void addTilesetValidation(EnterableTextField.Validator v) { mTileset.addValidations(v); }
	public void setOnTilesetChange(Consumer<String> cb) { mTileset.setCallback(cb); }
	
	public void setOnDimensionChange(DimCallback cb)
	{
		mWidth.setCallback(s  -> cb.onChange(Integer.parseInt(s), Integer.parseInt(mHeight.getText())));
		mHeight.setCallback(s -> cb.onChange(Integer.parseInt(mWidth.getText()), Integer.parseInt(s)));
	}

	@Override
	public Parent getNode()
	{
		return mRoot;
	}
	
	public static interface DimCallback { public abstract void onChange(int w, int h); }
}
