package de.chaosdorf.dorfapp.colorpicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ColorPickerView extends View
{
	private final int[] hueBarColors = new int[258];
	private Paint paint;
	private float currentHue = 0;

	public ColorPickerView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init(attrs);
	}

	public ColorPickerView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(attrs);
	}

	public ColorPickerView(Context context)
	{
		super(context);

		throw new RuntimeException("Valid colors (e.g. #ffffff) must be passed to this class via the XML parameters: pj:textColorNotFocused & pj:textColorFocused.");
	}

	private void init(AttributeSet attrs)
	{
		{
			// Get the current hue from the current color and update the main color field
			float[] hsv = new float[3];
			Color.colorToHSV(color, hsv);
			currentHue = hsv[0];

			// Initialize the colors of the hue slider bar
			int index = 0;
			for (float i = 0; i < 256; i += 256 / 42) // Red (#f00) to pink
			// (#f0f)
			{
				hueBarColors[index] = Color.rgb(255, 0, (int) i);
				index++;
			}
			for (float i = 0; i < 256; i += 256 / 42) // Pink (#f0f) to blue
			// (#00f)
			{
				hueBarColors[index] = Color.rgb(255 - (int) i, 0, 255);
				index++;
			}
			for (float i = 0; i < 256; i += 256 / 42) // Blue (#00f) to light
			// blue (#0ff)
			{
				hueBarColors[index] = Color.rgb(0, (int) i, 255);
				index++;
			}
			for (float i = 0; i < 256; i += 256 / 42) // Light blue (#0ff) to
			// green (#0f0)
			{
				hueBarColors[index] = Color.rgb(0, 255, 255 - (int) i);
				index++;
			}
			for (float i = 0; i < 256; i += 256 / 42) // Green (#0f0) to yellow
			// (#ff0)
			{
				hueBarColors[index] = Color.rgb((int) i, 255, 0);
				index++;
			}
			for (float i = 0; i < 256; i += 256 / 42) // Yellow (#ff0) to red
			// (#f00)
			{
				hueBarColors[index] = Color.rgb(255, 255 - (int) i, 0);
				index++;
			}

			// Initializes the Paint that will draw the View
			paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setTextAlign(Paint.Align.CENTER);
			paint.setTextSize(12);
		}

		@Override
		protected void onDraw (Canvas canvas)
		{
			int translatedHue = 255 - (int) (currentHue * 255 / 360);

			// Display all the colors of the hue bar with lines
			for (int x = 0; x < 256; x++)
			{
				// If this is not the current selected hue, display the actual color
				if (translatedHue != x)
				{
					paint.setColor(hueBarColors[x]);
					paint.setStrokeWidth(1);
				}
				else // else display a slightly larger black line
				{
					paint.setColor(Color.BLACK);
					paint.setStrokeWidth(3);
				}
				canvas.drawLine(x, 0, x, 40, paint);
			}
		}

		@Override
		protected void onMeasure ( int widthMeasureSpec, int heightMeasureSpec)
		{
			setMeasuredDimension(40, 366);
		}

		@Override
		public boolean onTouchEvent (MotionEvent event)
		{
			if (event.getAction() != MotionEvent.ACTION_DOWN)
			{
				return true;
			}

			float x = event.getX();

			// Update the main field colors
			currentHue = (255 - x) * 360 / 255;

			// Force the redraw of the dialog
			invalidate();

			return true;
		}
	}
