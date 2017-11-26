package view.map;

import java.util.function.Consumer;

import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import lib.EnterableTextField;
import lib.IDValidator;
import lib.Utils;
import lib.action.Action;
import model.Event.Argument;
import model.Event.WarpArgument;

public class ArgumentWarpView extends ArgumentView
{
	private final GridPane mRoot;
	private final WarpArgument mArgument;
	
	public ArgumentWarpView(Consumer<Action> onchange, Argument a)
	{
		super(a.getType().toString(), onchange);
		
		mRoot = new GridPane();
		mArgument = (WarpArgument) a;
		
		EnterableTextField tfMap = new EnterableTextField(mArgument.getMapID());
		EnterableTextField tfTarget = new EnterableTextField(mArgument.getTargetID());
		
		tfMap.addValidations(new IDValidator("map"));
		tfTarget.addValidations(EnterableTextField.IS_NOT_EMPTY);
		
		tfMap.setCallback(s -> act(Utils.ReversableAction(mArgument, "MapID", s)));
		tfTarget.setCallback(s -> act(Utils.ReversableAction(mArgument, "TargetID", s)));
	}
	
	@Override
	public Parent getNode()
	{
		return mRoot;
	}
}
