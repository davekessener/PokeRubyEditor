package view.map.create;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import lib.EnterableTextField;
import lib.IDValidator;
import lib.Utils;
import model.Event.Argument;
import model.Event.WarpArgument;

public class NewWarpArgumentUI extends NewArgumentUI
{
	private final GridPane mRoot;
	private final TextField mMap, mTarget;
	
	public NewWarpArgumentUI()
	{
		mRoot = new GridPane();
		
		EnterableTextField tfMap = new EnterableTextField("");
		EnterableTextField tfTarget = new EnterableTextField("");
		
		mRoot.addRow(0, new Label("Map ID"), tfMap);
		mRoot.addRow(1, new Label("Target ID"), tfTarget);
		
		mRoot.setHgap(5D);
		mRoot.setVgap(3D);
		GridPane.setHgrow(tfMap, Priority.ALWAYS);
		GridPane.setHgrow(tfTarget, Priority.ALWAYS);
		
		tfMap.addValidations(new IDValidator("map"));
		tfTarget.addValidations(EnterableTextField.IS_NOT_EMPTY);
		
		mMap = tfMap;
		mTarget = tfTarget;
	}

	@Override
	public boolean canCreate()
	{
		return !mMap.getText().isEmpty() && !mTarget.getText().isEmpty();
	}

	@Override
	public Parent getNode()
	{
		return mRoot;
	}
	
	@Override
	public Argument create()
	{
		return Utils.with(new WarpArgument(), a -> {
			a.setMapID(mMap.getText());
			a.setTargetID(mTarget.getText());
		});
	}
}
