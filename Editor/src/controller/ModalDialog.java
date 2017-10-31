package controller;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
		
		Node ui = mUI.getNode();
		
		AnchorPane root = new AnchorPane();
		HBox hbox = new HBox();
		Button okBtn = new Button("OK");
		Button cancelBtn = new Button("Cancel");

		okBtn.setPrefWidth(80D);
		cancelBtn.setPrefWidth(80D);
		
		okBtn.setOnAction(e -> createAndClose());
		cancelBtn.setOnAction(e -> close());
		
		hbox.getChildren().addAll(okBtn, cancelBtn);
		hbox.setSpacing(5D);
		
		root.getChildren().addAll(hbox, ui);
		AnchorPane.setRightAnchor(hbox, 5D);
		AnchorPane.setBottomAnchor(hbox, 5D);
		AnchorPane.setTopAnchor(ui, 5D);
		AnchorPane.setLeftAnchor(ui, 5D);
		AnchorPane.setRightAnchor(ui, 5D);
		
		this.setScene(new Scene(root, Math.max(250, ui.prefWidth(100)), Math.max(100, ui.prefHeight(250) + hbox.getHeight() + 5)));
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
