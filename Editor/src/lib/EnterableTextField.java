package lib;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

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
	
	public static class EmptyAcceptor extends Validator
	{
		private Validator mChild;
		
		public EmptyAcceptor(Validator child)
		{
			mChild = child;
		}
		
		@Override
		public boolean isValid(String v)
		{
			return v.equals("") || (mChild == null || mChild.isValid(v));
		}
		
		@Override
		public String message()
		{
			return mChild.message();
		}
	}
	
	public static class FileValidator extends Validator
	{
		private FileGenerator mGenerator;
		
		public FileValidator(FileGenerator gen)
		{
			mGenerator = gen;
		}
		
		@Override
		public boolean isValid(String s)
		{
			File f = mGenerator.generate(s);
			
			return f != null && f.exists();
		}
		
		@Override
		public String message()
		{
			return "Must be a valid file!";
		}
		
		public static interface FileGenerator { public abstract File generate(String content); }
	}
	
	public static class IntValidator extends Validator
	{
		private Predicate<Integer> mCheck;
		private String mDesc;
		
		public IntValidator(String desc, Predicate<Integer> check)
		{
			mDesc = desc;
			mCheck = check;
		}
		
		public IntValidator lessThan(int v) { return new IntValidator(mDesc + " and less than " + v, i -> mCheck.test(i) && i < v); }
		public IntValidator greaterThan(int v) { return new IntValidator(mDesc + " and greater than " + v, i -> mCheck.test(i) && i > v); }
		
		public IntValidator lessThan(Producer<Integer> p) { return new IntValidator(mDesc + " and less than", i -> mCheck.test(i) && i < p.produce()); }
		public IntValidator greaterThan(Producer<Integer> p) { return new IntValidator(mDesc + " and greater than", i -> mCheck.test(i) && i > p.produce()); }
		
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
			
			return mCheck == null ? true : mCheck.test(i);
		}

		public String message()
		{
			return "Must be " + mDesc + " [Integer]!";
		}
		
		public static interface Producer<T> { public abstract T produce(); }
	}
	
	public static IntValidator IS_INT = new IntValidator("", i -> true);
	public static IntValidator IS_POSITIVE_INT = new IntValidator("positive", i -> i > 0);
	
	public static Validator IS_NOT_EMPTY = new Validator() {
		@Override public boolean isValid(String s) { return !s.isEmpty(); }
		@Override public String message() { return "Must not be empty!"; }
	};
}
