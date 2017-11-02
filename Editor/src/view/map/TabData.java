package view.map;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import lib.EnterableTextField;
import lib.IDValidator;
import view.UI;

public class TabData implements UI
{
	private GridPane mRoot;
	private EnterableTextField mNameField;
	private EnterableTextField mTilemapField;
	private EnterableTextField mBorderField;

	public TabData(String name, String tilemap, String border)
	{
		mRoot = new GridPane();
		
		Label lblName = new Label("Name");
		Label lblTilemap = new Label("Tilemap ID");
		Label lblBorder = new Label("Border ID");
		
		mNameField = new EnterableTextField(name);
		mTilemapField = new EnterableTextField(tilemap);
		mBorderField = new EnterableTextField(border);

		mNameField.addValidations(EnterableTextField.IS_NOT_EMPTY);
		mTilemapField.addValidations(new IDValidator("tilemap"));
		mBorderField.addValidations(new IDValidator("tilemap"));
		
		mRoot.setHgap(5D);
		mRoot.setVgap(5D);
		mRoot.addColumn(0, lblName, lblTilemap, lblBorder);
		mRoot.addColumn(1, mNameField, mTilemapField, mBorderField);

		for(EnterableTextField tf : new EnterableTextField[] {mNameField, mTilemapField, mBorderField})
		{
			tf.addValidations(EnterableTextField.IS_NOT_EMPTY);
			GridPane.setHgrow(tf, Priority.ALWAYS);
		}

		for(Label l : new Label[] {lblName, lblTilemap, lblBorder})
		{
			GridPane.setHalignment(l, HPos.RIGHT);
			GridPane.setValignment(l, VPos.CENTER);
		}
	}
	
	public void setOnNameChange(EnterableTextField.Callback cb) { mNameField.setCallback(cb); }
	public void setOnTilemapIDChange(EnterableTextField.Callback cb) { mTilemapField.setCallback(cb); }
	public void setOnBorderIDChange(EnterableTextField.Callback cb) { mBorderField.setCallback(cb); }
	
	@Override
	public Parent getNode()
	{
		return mRoot;
	}
}
