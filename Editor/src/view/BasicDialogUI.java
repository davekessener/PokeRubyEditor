package view;

import javafx.stage.Stage;

public abstract class BasicDialogUI implements DialogUI
{
	private Stage mStage;
	
	public Stage getStage() { return mStage; }
	public void setStage(Stage s) { mStage = s; }
	public boolean hasStage() { return mStage != null; }
}
