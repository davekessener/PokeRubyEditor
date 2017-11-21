package view.map;

import java.util.function.Consumer;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import lib.EnterableTextField;

public class ArgumentTextView extends ArgumentView
{
	private final GridPane mRoot;
	private Consumer<String> mOnChange;
	
	public ArgumentTextView(String v)
	{
		super("text");
		
		mRoot = new GridPane();
		
		EnterableTextField tf = new EnterableTextField(v);
		
		tf.addValidations(EnterableTextField.IS_NOT_EMPTY);
		
		tf.setCallback(s -> onChange(s));
		
		mRoot.addRow(0, new Label("Text ID"), tf);
		mRoot.setHgap(5D);
		GridPane.setHgrow(tf, Priority.ALWAYS);
	}
	
	public void setOnChange(Consumer<String> cb) { mOnChange = cb; }
	
	private void onChange(String s)
	{
		if(mOnChange != null)
		{
			mOnChange.accept(s);
		}
	}

	@Override
	public Parent getNode()
	{
		return mRoot;
	}
}
