package de.chaosdorf.dorfapp.colorpicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.view.MotionEvent;
import android.view.View;

import de.chaosdorf.dorfapp.R;

public class ColorPicker
{
	private final String key;

	public ColorPicker(final Context context, final OnColorChangedListener listener, final String key, final int initialColor, final int defaultColor)
	{
		this.key = key;

		OnColorChangedListener l = new OnColorChangedListener()
		{
			public void colorChanged(String key, int color)
			{
				listener.colorChanged(ColorPicker.this.key, color);
			}
		};
		new ColorPickerView(context, l, initialColor, defaultColor);
	}

	private static class ColorPickerView extends View
	{
		private final Context context;
		private final OnColorChangedListener listener;
		private final int defaultColor;
		private final int[] hueBarColors = new int[258];
		private final int[] mainColors = new int[65536];
		private final Shader[] shaders = new Shader[256];
		private final Paint paint;
		private float currentHue = 0;
		private int currentX = 0;
		private int currentY = 0;
		private int currentColor;

		ColorPickerView(Context context, OnColorChangedListener listener, int color, int defaultColor)
		{
			super(context);
			this.context = context;
			this.listener = listener;
			this.defaultColor = defaultColor;

			// Get the current hue from the current color and update the main color field
			float[] hsv = new float[3];
			Color.colorToHSV(color, hsv);
			currentHue = hsv[0];
			updateMainColors();

			currentColor = color;

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

		// Get the current selected color from the hue bar
		private int getCurrentMainColor()
		{
			int translatedHue = 255 - (int) (currentHue * 255 / 360);
			int index = 0;
			for (float i = 0; i < 256; i += 256 / 42)
			{
				if (index == translatedHue)
				{
					return Color.rgb(255, 0, (int) i);
				}
				index++;
			}
			for (float i = 0; i < 256; i += 256 / 42)
			{
				if (index == translatedHue)
				{
					return Color.rgb(255 - (int) i, 0, 255);
				}
				index++;
			}
			for (float i = 0; i < 256; i += 256 / 42)
			{
				if (index == translatedHue)
				{
					return Color.rgb(0, (int) i, 255);
				}
				index++;
			}
			for (float i = 0; i < 256; i += 256 / 42)
			{
				if (index == translatedHue)
				{
					return Color.rgb(0, 255, 255 - (int) i);
				}
				index++;
			}
			for (float i = 0; i < 256; i += 256 / 42)
			{
				if (index == translatedHue)
				{
					return Color.rgb((int) i, 255, 0);
				}
				index++;
			}
			for (float i = 0; i < 256; i += 256 / 42)
			{
				if (index == translatedHue)
				{
					return Color.rgb(255, 255 - (int) i, 0);
				}
				index++;
			}
			return Color.RED;
		}

		// Update the main field colors depending on the current selected hue
		private void updateMainColors()
		{
			int mainColor = getCurrentMainColor();
			int index = 0;
			int[] topColors = new int[256];
			int[] colors = new int[2];
			colors[0] = Color.BLACK;
			colors[1] = Color.BLACK;
			for (int y = 0; y < 256; y++)
			{
				for (int x = 0; x < 256; x++)
				{
					if (y == 0)
					{
						mainColors[index] = Color.rgb(
								255 - (255 - Color.red(mainColor)) * x / 255,
								255 - (255 - Color.green(mainColor)) * x / 255,
								255 - (255 - Color.blue(mainColor)) * x / 255
						);
						topColors[x] = mainColors[index];
					}
					else
					{
						mainColors[index] = Color.rgb(
								(255 - y) * Color.red(topColors[x]) / 255,
								(255 - y) * Color.green(topColors[x]) / 255,
								(255 - y) * Color.blue(topColors[x]) / 255
						);
					}
					index++;
				}
				colors[0] = mainColors[y];
				shaders[y] = new LinearGradient(0, 50, 0, 306, colors, null, Shader.TileMode.REPEAT);
			}
		}

		@Override
		protected void onDraw(Canvas canvas)
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
				canvas.drawLine(x + 10, 0, x + 10, 40, paint);
			}

			// Display the main field colors using LinearGradient
			for (int x = 0; x < 256; x++)
			{
				paint.setShader(shaders[x]);
				canvas.drawLine(x + 10, 50, x + 10, 306, paint);
			}
			paint.setShader(null);

			// Display the circle around the currently selected color in the main field
			if (currentX != 0 && currentY != 0)
			{
				paint.setStyle(Paint.Style.STROKE);
				paint.setColor(Color.BLACK);
				canvas.drawCircle(currentX, currentY, 10, paint);
			}

			// Draw a 'button' with the currently selected color
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(currentColor);
			canvas.drawRect(10, 316, 138, 356, paint);

			// Set the text color according to the brightness of the color
			if (Color.red(currentColor) + Color.green(currentColor) + Color.blue(currentColor) < 384)
			{
				paint.setColor(Color.WHITE);
			}
			else
			{
				paint.setColor(Color.BLACK);
			}
			canvas.drawText(context.getResources().getString(R.string.settings_bg_color_confirm), 74, 340, paint);

			// Draw a 'button' with the default color
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(defaultColor);
			canvas.drawRect(138, 316, 266, 356, paint);

			// Set the text color according to the brightness of the color
			if (Color.red(defaultColor) + Color.green(defaultColor) + Color.blue(defaultColor) < 384)
			{
				paint.setColor(Color.WHITE);
			}
			else
			{
				paint.setColor(Color.BLACK);
			}
			canvas.drawText(context.getResources().getString(R.string.settings_default_color_confirm), 202, 340, paint);
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
		{
			setMeasuredDimension(276, 366);
		}

		@Override
		public boolean onTouchEvent(MotionEvent event)
		{
			if (event.getAction() != MotionEvent.ACTION_DOWN)
			{
				return true;
			}
			float x = event.getX();
			float y = event.getY();

			// If the touch event is located in the hue bar
			if (x > 10 && x < 266 && y > 0 && y < 40)
			{
				// Update the main field colors
				currentHue = (255 - x) * 360 / 255;
				updateMainColors();

				// Update the current selected color
				int transX = currentX - 10;
				int transY = currentY - 60;
				int index = 256 * (transY - 1) + transX;
				if (index > 0 && index < mainColors.length)
				{
					currentColor = mainColors[256 * (transY - 1) + transX];
				}

				// Force the redraw of the dialog
				invalidate();
			}

			// If the touch event is located in the main field
			if (x > 10 && x < 266 && y > 50 && y < 306)
			{
				currentX = (int) x;
				currentY = (int) y;
				int transX = currentX - 10;
				int transY = currentY - 60;
				int index = 256 * (transY - 1) + transX;
				if (index > 0 && index < mainColors.length)
				{
					// Update the current color
					currentColor = mainColors[index];
					// Force the redraw of the dialog
					invalidate();
				}
			}

			// If the touch event is located in the left button, notify the listener with the current color
			if (x > 10 && x < 138 && y > 316 && y < 356)
			{
				listener.colorChanged("", currentColor);
			}

			// If the touch event is located in the right button, notify the listener with the default color
			if (x > 138 && x < 266 && y > 316 && y < 356)
			{
				listener.colorChanged("", defaultColor);
			}

			return true;
		}
	}
}
