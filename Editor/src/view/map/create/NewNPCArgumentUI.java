package view.map.create;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import lib.EnterableTextField;
import lib.Utils;
import model.Event.Argument;
import model.Event.NPCArgument;

public class NewNPCArgumentUI extends NewArgumentUI
{
	private final GridPane mRoot;
	private final TextField mSprite, mText, mAI;
	
	public NewNPCArgumentUI()
	{
		mRoot = new GridPane();
		
		EnterableTextField tfSprite = new EnterableTextField("");
		EnterableTextField tfText = new EnterableTextField("");
		EnterableTextField tfAI = new EnterableTextField("");
		
		mRoot.addRow(0, new Label("Sprite ID"), tfSprite);
		mRoot.addRow(1, new Label("Text ID"), tfText);
		mRoot.addRow(2, new Label("AI"), tfAI);

		mRoot.setHgap(5D);
		mRoot.setVgap(3D);
		GridPane.setHgrow(tfSprite, Priority.ALWAYS);
		GridPane.setHgrow(tfText, Priority.ALWAYS);
		GridPane.setHgrow(tfAI, Priority.ALWAYS);
		
		tfSprite.addValidations(EnterableTextField.IS_NOT_EMPTY);
		tfText.addValidations(EnterableTextField.IS_NOT_EMPTY);
		tfAI.addValidations(EnterableTextField.IS_NOT_EMPTY);
		
		mSprite = tfSprite;
		mText = tfText;
		mAI = tfAI;
	}
	
	@Override
	public boolean canCreate()
	{
		return !mSprite.getText().isEmpty() && !mText.getText().isEmpty() && !mAI.getText().isEmpty();
	}

	@Override
	public Argument create()
	{
		return Utils.with(new NPCArgument(), a -> {
			a.setSpriteID(mSprite.getText());
			a.setTextID(mSprite.getText());
			a.setAI(mAI.getText());
		});
	}

	@Override
	public Parent getNode()
	{
		return mRoot;
	}
}
