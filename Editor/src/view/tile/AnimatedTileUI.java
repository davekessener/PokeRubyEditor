package view.tile;

import java.util.List;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lib.EnterableTextField;
import lib.observe.IObservableList;
import model.Tileset.Type;
import model.Vec2;

import static javafx.beans.property.ReadOnlyIntegerProperty.readOnlyIntegerProperty;

public class AnimatedTileUI extends BasicTileUI
{
	private final HBox mRoot;
	private List<Vec2> mFrames;
	private TextField mCurrentFrame;
	private Callback<Integer> mOnAddFrame, mOnRemoveFrame;
	private Callback<Integer> mOnPeriodChange;
	private final Property<Number> mSelectedFrame;

	public AnimatedTileUI(IObservableList<Vec2> frames, int period)
	{
		mRoot = new HBox();
		mFrames = frames;
		mSelectedFrame = new SimpleIntegerProperty();
		
		mRoot.setSpacing(3D);
		
		EnterableTextField tfFrame, tfPeriod;
		tfFrame = new EnterableTextField("1");
		tfPeriod = new EnterableTextField("" + period);
		
		tfFrame.addValidations(EnterableTextField.IS_POSITIVE_INT.lessThan(() -> mFrames.size() + 1));
		tfPeriod.addValidations(EnterableTextField.IS_POSITIVE_INT);
		
		Label lblMax = new Label("/ " + mFrames.size());
		Label lblMs = new Label("ms");
		VBox vb = new VBox();
		Button addPrev = new Button("V");
		Button addNext = new Button("^");
		Button deleteBtn = new Button("-");
		Button btnPrev = new Button("<");
		Button btnNext = new Button(">");
		vb.getChildren().addAll(addNext, addPrev);
		lblMax.setAlignment(Pos.CENTER_LEFT);
		lblMs.setAlignment(Pos.CENTER_LEFT);
		mRoot.setAlignment(Pos.CENTER);
		mRoot.getChildren().addAll(vb, btnPrev, tfFrame, lblMax, btnNext, deleteBtn, tfPeriod, lblMs);

		tfFrame.setCallback(s -> mSelectedFrame.setValue(Integer.parseInt(s) - 1));
		
		mCurrentFrame = tfFrame;
		
		deleteBtn.disableProperty().bind(readOnlyIntegerProperty(frames.sizeProperty()).lessThan(2));
		btnPrev.disableProperty().bind(readOnlyIntegerProperty(mSelectedFrame).isEqualTo(0));
		btnNext.disableProperty().bind(readOnlyIntegerProperty(mSelectedFrame).isEqualTo(readOnlyIntegerProperty(frames.sizeProperty()).subtract(1)));
		frames.addObserver(o -> lblMax.setText("/ " + frames.size()));

		btnPrev.setOnAction(e -> setSelectedFrame(getSelectedFrame() - 1));
		btnNext.setOnAction(e -> setSelectedFrame(getSelectedFrame() + 1));
		
		addPrev.setOnAction(e -> addFrame(getSelectedFrame()));
		addNext.setOnAction(e -> addFrame(getSelectedFrame() + 1));
		deleteBtn.setOnAction(e -> removeFrame(getSelectedFrame()));
		tfPeriod.setCallback(s -> changePeriod(Integer.parseInt(s)));
		
		mSelectedFrame.addListener(o -> this.selectTile(mFrames.get(getSelectedFrame())));
		mSelectedFrame.addListener(o -> mCurrentFrame.setText("" + (mSelectedFrame.getValue().intValue() + 1)));
	}
	
	public int getSelectedFrame() { return (int) mSelectedFrame.getValue(); }
	public void setSelectedFrame(int idx) { mSelectedFrame.setValue(idx); }
	
	public void setOnAddFrame(Callback<Integer> cb) { mOnAddFrame = cb; }
	public void setOnRemoveFrame(Callback<Integer> cb) { mOnRemoveFrame = cb; }
	public void setOnPeriodChange(Callback<Integer> cb) { mOnPeriodChange = cb; }
	
	private void addFrame(int idx)
	{
		if(mOnAddFrame != null)
		{
			mOnAddFrame.call(idx);
		}
	}
	
	private void removeFrame(int idx)
	{
		if(mOnRemoveFrame != null)
		{
			mOnRemoveFrame.call(idx);
		}
	}
	
	private void changePeriod(int p)
	{
		if(mOnPeriodChange != null)
		{
			mOnPeriodChange.call(p);
		}
	}
	
	@Override
	protected void onBind()
	{
		this.selectTile(mFrames.get(getSelectedFrame()));
	}

	@Override
	public Type getType()
	{
		return Type.ANIMATED;
	}

	@Override
	public Parent getNode()
	{
		return mRoot;
	}
	
	public static interface Callback<T> { public abstract void call(T v); }
}
