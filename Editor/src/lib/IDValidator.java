package lib;

import controller.EditorController;

public class IDValidator extends EnterableTextField.FileValidator
{
	private String mType;
	
	public IDValidator(String type)
	{
		super(s -> EditorController.Instance.getLoader().getFile("data", type, s + ".json"));
		mType = type;
	}
	
	public String getType() { return mType; }
	
	@Override
	public String message()
	{
		return "Not an existing " + mType + "!";
	}
}
