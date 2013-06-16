/**
 * 
 */
package pl.mnowrot.ecogalleryindicator;

import us.feras.ecogallery.EcoGallery;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author marcin
 * 
 */
public class EcoGalleryIndicator extends View {

	private EcoGallery mEcoGallery;
	private int mColor;
	private int mSize;

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
		this(context, attrs, 0);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public EcoGalleryIndicator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		initialize(context, attrs, defStyle);

	}

	private void initialize(Context context, AttributeSet attrs, int defStyle) {
		TypedArray a = null;
		try {
			a = context.obtainStyledAttributes(attrs, R.styleable.EcoGalleryIndicator, defStyle, 0);
			mColor = a.getColor(R.styleable.EcoGalleryIndicator_color, android.R.color.background_light);
			mSize = a.getDimensionPixelSize(R.styleable.EcoGalleryIndicator_size, context.getResources()
					.getDimensionPixelSize(R.dimen.ecoGallery_indicator_size_default));
		} finally {
			a.recycle();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {

	}

	public EcoGallery getEcoGallery() {
		return mEcoGallery;
	}

	public void setEcoGallery(EcoGallery mEcoGallery) {
		this.mEcoGallery = mEcoGallery;
	}

}
