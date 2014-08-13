package ryan.pope.convocloud.presentation;

import ryan.pope.convocloud.business.ColorPicker;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class ColorPickerDialog extends AlertDialog
{

	public interface OnColorSelectedListener
	{
		public void onColorSelected(int color);
	}

	private ColorPicker colorPickerView;

	private final OnColorSelectedListener onColorSelectedListener;

	private OnClickListener onClickListener = new DialogInterface.OnClickListener()
	{
		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			switch (which)
			{
				case BUTTON_POSITIVE:
					int selectedColor = colorPickerView.getColor();
					onColorSelectedListener.onColorSelected(selectedColor);
					break;
				case BUTTON_NEGATIVE:
					dialog.dismiss();
					break;
			}
		}
	};

	public ColorPickerDialog(Context context, int initialColor,
			OnColorSelectedListener onColorSelectedListener)
	{
		super(context);

		this.onColorSelectedListener = onColorSelectedListener;

		RelativeLayout relativeLayout = new RelativeLayout(context);
		LayoutParams layoutParams = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

		colorPickerView = new ColorPicker(context);
		colorPickerView.setColor(initialColor);

		relativeLayout.addView(colorPickerView, layoutParams);

		setButton(BUTTON_POSITIVE, context.getString(android.R.string.ok),
				onClickListener);
		setButton(BUTTON_NEGATIVE, context.getString(android.R.string.cancel),
				onClickListener);

		setView(relativeLayout);

	}

}
