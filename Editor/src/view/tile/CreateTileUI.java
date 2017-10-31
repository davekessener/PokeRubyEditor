package view.tile;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import view.DialogUI;

public class CreateTileUI implements DialogUI
{
	private GridPane mRoot;
	private TextField mID;
	private ComboBox<String> mType;
	
	public CreateTileUI()
	{
		mRoot = new GridPane();
		mID = new TextField();
		mType = new ComboBox<>();
		
		mType.getItems().addAll("Static", "Animated");
		mType.getSelectionModel().select(0);
		
		Label lID = new Label("ID");
		Label lType = new Label("Type");
		
		lID.prefHeightProperty().bind(mID.heightProperty());
		lType.prefHeightProperty().bind(mType.heightProperty());
		mType.prefWidthProperty().bind(mID.widthProperty());
		
		mRoot.add(lID, 0, 0);
		mRoot.add(lType, 0, 1);
		mRoot.add(mID, 1, 0);
		mRoot.add(mType, 1, 1);
		
		mRoot.setAlignment(Pos.CENTER);
		mRoot.setHgap(5D);
		mRoot.setVgap(5D);

		GridPane.setHgrow(mID, Priority.ALWAYS);
		GridPane.setHgrow(mType, Priority.ALWAYS);
		
		GridPane.setHalignment(lID, HPos.RIGHT);
		GridPane.setHalignment(lType, HPos.RIGHT);
	}
	
	public String getID() { return mID.getText(); }
	public String getType() { return mType.getSelectionModel().getSelectedItem().toLowerCase(); }

	@Override
	public Parent getNode()
	{
		return mRoot;
	}

	@Override
	public boolean canCreate()
	{
		if(getID().isEmpty())
		{
			(new Alert(AlertType.ERROR, "ID field cannot be empty!")).showAndWait();
			
			return false;
		}
		
		return true;
	}
}
