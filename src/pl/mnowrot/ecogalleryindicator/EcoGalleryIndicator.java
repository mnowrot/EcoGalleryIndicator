/**
 * 
 */
package pl.mnowrot.ecogalleryindicator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * @author marcin
 * 
 */
public class EcoGalleryIndicator extends ViewGroup {

	/**
	 * @param context
	 */
	public EcoGalleryIndicator(Context context) {
		this(context, null);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public EcoGalleryIndicator(Context context, AttributeSet attrs) {
		this(context, attrs, R.style.AppTheme);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public EcoGalleryIndicator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.ViewGroup#onLayout(boolean, int, int, int, int)
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// This view must not contain child views, so this is a no-op
	}

}
