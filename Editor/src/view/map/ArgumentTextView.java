package view.map;

import java.util.function.Consumer;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import lib.EnterableTextField;
import lib.Utils;
import lib.action.Action;
import model.Event.Argument;
import model.Event.TextArgument;

public class ArgumentTextView extends ArgumentView
{
	private final GridPane mRoot;
	private final TextArgument mArgument;
	
	public ArgumentTextView(Consumer<Action> onchange, Argument a)
	{
		super(a.getType().toString(), onchange);
		
		mRoot = new GridPane();
		mArgument = (TextArgument) a;
		
		EnterableTextField tf = new EnterableTextField(mArgument.getText());
		
		tf.addValidations(EnterableTextField.IS_NOT_EMPTY);
		
		tf.setCallback(s -> act(Utils.ReversableAction(mArgument, "Text", s)));
		
		mRoot.addRow(0, new Label("Text ID"), tf);
		mRoot.setHgap(5D);
		GridPane.setHgrow(tf, Priority.ALWAYS);
	}
	
	@Override
	public Parent getNode()
	{
		return mRoot;
	}
}
