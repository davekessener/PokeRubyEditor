package lib;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;

public class EnterableTextField extends TextField
{
	private final List<Validator> mValidations;
	private Callback mCallback;
	private String mBackup;
	
	public EnterableTextField(String def)
	{
		super(def);
		
		mValidations = new ArrayList<>();
		
		this.setOnKeyPressed(e -> {
			if(e.getCode().equals(KeyCode.ENTER))
			{
				this.getParent().requestFocus();
			}
		});
		
		this.focusedProperty().addListener((ob, o, n) -> {
			final String s = this.getText();
			
			if(n)
			{
				mBackup = s;
			}
			else if(!s.equals(mBackup))
			{
				for(Validator v : mValidations)
				{
					if(!v.isValid(s))
					{
						String msg = v.message();
						
						if(msg == null) msg = "Invalid data!";
						
						(new Alert(AlertType.ERROR, msg)).showAndWait();
						
						this.setText(mBackup);
						
						return;
					}
				}
				
				if(mCallback != null)
				{
					mCallback.onChange(s);
				}
			}
		});
	}
	
	public void setCallback(Callback cb)
	{
		mCallback = cb;
	}
	
	public void addValidations(Validator ... vs)
	{
		for(Validator v : vs)
		{
			mValidations.add(v);
		}
	}
	
	public static interface Callback { void onChange(String s); }
	
	public static abstract class Validator
	{
		public abstract boolean isValid(String v);
		public String message() { return null; }
	}
	
	public static Validator IS_POSITIVE_INT = new Validator() {
		@Override
		public boolean isValid(String s)
		{
			int i;
			
			try
			{
				i = Integer.parseInt(s);
			}
			catch(NumberFormatException e)
			{
				return false;
			}
			
			return i > 0;
		}
		
		@Override
		public String message()
		{
			return "Must be a positive integer!";
		}
	};
	
	public static Validator IS_NOT_EMPTY = new Validator() {
		@Override public boolean isValid(String s) { return !s.isEmpty(); }
		@Override public String message() { return "Must not be empty!"; }
	};
}