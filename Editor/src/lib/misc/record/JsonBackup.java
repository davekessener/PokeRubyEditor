package lib.misc.record;

import com.eclipsesource.json.JsonValue;

import model.JsonModel;

public class JsonBackup implements Backupable
{
	private final JsonModel mModel;
	
	public JsonBackup(JsonModel m)
	{
		mModel = m;
	}

	@Override
	public JsonValue backup()
	{
		return mModel.save();
	}

	@Override
	public void restore(Object o)
	{
		if(!(o instanceof JsonValue))
		{
			throw new IllegalArgumentException("Can only restore from JSON tag!");
		}
		
		mModel.load((JsonValue) o);
	}
}
