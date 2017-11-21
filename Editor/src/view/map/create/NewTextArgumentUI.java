package view.map.create;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import lib.EnterableTextField;
import lib.Utils;
import model.Event.Argument;
import model.Event.TextArgument;

public class NewTextArgumentUI extends NewArgumentUI
{
	private final GridPane mRoot;
	private final TextField mText;
	
	public NewTextArgumentUI()
	{
		mRoot = new GridPane();
		
		EnterableTextField tf = new EnterableTextField("");
		
		tf.addValidations(EnterableTextField.IS_NOT_EMPTY);
		
		mText = tf;
		
		mRoot.setHgap(5D);
		mRoot.addRow(0, new Label("Text ID"), mText);
		GridPane.setHgrow(mText, Priority.ALWAYS);
	}

	@Override
	public Parent getNode()
	{
		return mRoot;
	}

	@Override
	public boolean canCreate()
	{
		return !mText.getText().isEmpty();
	}

	@Override
	public Argument create()
	{
		return Utils.with(new TextArgument(), a -> a.setText(mText.getText()));
	}
}
