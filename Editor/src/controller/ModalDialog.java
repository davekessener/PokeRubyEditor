package controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.BasicDialogUI;
import view.DialogUI;

public class ModalDialog extends Stage
{
	private DialogUI mUI;
	private boolean mCreated;
	
	public ModalDialog(String title, DialogUI dui)
	{
		mUI = dui;
		mCreated = false;
		
		this.setTitle(title);
		this.initModality(Modality.APPLICATION_MODAL);
		
		if(mUI instanceof BasicDialogUI)
		{
			((BasicDialogUI) mUI).setStage(this);
		}
		
		Node ui = mUI.getNode();
		
		GridPane root = new GridPane();
		HBox hbox = new HBox();
		Button okBtn = new Button("OK");
		Button cancelBtn = new Button("Cancel");

		okBtn.setPrefWidth(100D);
		cancelBtn.setPrefWidth(100D);
		
		okBtn.setOnAction(e -> createAndClose());
		cancelBtn.setOnAction(e -> close());
		
		hbox.getChildren().addAll(okBtn, cancelBtn);
		hbox.setAlignment(Pos.BASELINE_RIGHT);
		hbox.setSpacing(5D);
		
		root.addColumn(0, ui, hbox);
		root.setPadding(new Insets(5D, 5D, 5D, 5D));
		root.setVgap(7D);
		GridPane.setHgrow(ui, Priority.ALWAYS);
		GridPane.setVgrow(ui, Priority.ALWAYS);
		
		this.setScene(new Scene(root));
	}
	
	public boolean isSuccessful() { return mCreated; }
	
	private void createAndClose()
	{
		if(!mUI.canCreate())
		{
			return;
		}
		
		mCreated = true;
		close();
	}
}
