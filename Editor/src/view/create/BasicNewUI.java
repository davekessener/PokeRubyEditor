package view.create;

import java.util.HashMap;
import java.util.Map;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import lib.EnterableTextField;
import lib.EnterableTextField.Validator;
import lib.EnterableTextField.EmptyAcceptor;

public abstract class BasicNewUI implements NewUI
{
	private final GridPane mRoot;
	private final Map<String, EnterableTextField> mTextFields;
	private final Map<String, String> mDefaults;
	private int mIdx;
	
	public BasicNewUI()
	{
		mRoot = new GridPane();
		mTextFields = new HashMap<>();
		mDefaults = new HashMap<>();
		mIdx = 0;
		
		mRoot.setHgap(5D);
		mRoot.setVgap(3D);
		
		addField("id", "ID", new NewIDValidator());
	}
	
	protected void addField(String id, String name) { addField(id, name, null, null); }
	protected void addField(String id, String name, Validator v) { addField(id, name, v, null); }
	protected void addField(String id, String name, String def) { addField(id, name, null, def); }
	protected void addField(String id, String name, Validator v, String def)
	{
		Label lbl = new Label(name);
		EnterableTextField tf = new EnterableTextField("");
		
		tf.addValidations(new EmptyAcceptor(v));
		
		mRoot.addRow(mIdx++, lbl, tf);
		GridPane.setHalignment(lbl, HPos.RIGHT);
		GridPane.setValignment(lbl, VPos.CENTER);
		GridPane.setHgrow(tf, Priority.ALWAYS);
		
		mTextFields.put(id, tf);
		
		if(def != null)
		{
			mDefaults.put(id, def);
		}
	}
	
	protected String getField(String id)
	{
		String s = mTextFields.get(id).getText();
		String d = null;
		
		if(s.equals(""))
		{
			d = mDefaults.get(id);
		}

		return d == null ? s : d;
	}

	@Override
	public boolean canCreate()
	{
		for(String id : mTextFields.keySet())
		{
			if(!mDefaults.containsKey(id) && mTextFields.get(id).getText().isEmpty())
			{
				return false;
			}
		}
		
		return validate();
	}
	
	protected boolean validate()
	{
		return true;
	}

	@Override
	public Parent getNode()
	{
		return mRoot;
	}

	@Override
	public String getID()
	{
		return getField("id");
	}
	
	private static class NewIDValidator extends EnterableTextField.Validator
	{
		@Override
		public boolean isValid(String v)
		{
			return v.matches("[a-z][a-z0-9:_]*");
		}
		
		@Override
		public String message()
		{
			return "Must be an alphanumerical ID!";
		}
	}
}
