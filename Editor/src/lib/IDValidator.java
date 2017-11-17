package lib;

import java.io.File;

import controller.EditorController;

public class IDValidator extends EnterableTextField.FileValidator
{
	private String mType;
	
	public IDValidator(String type)
	{
		super(s -> new File(EditorController.Instance.getLoader().generateFilename(type, s)));
		mType = type;
	}
	
	public String getType() { return mType; }
	
	@Override
	public String message()
	{
		return "Not an existing " + mType + "!";
	}
}
