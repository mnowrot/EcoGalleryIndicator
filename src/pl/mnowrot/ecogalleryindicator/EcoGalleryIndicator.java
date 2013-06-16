/**
 * 
 */
package pl.mnowrot.ecogalleryindicator;

import us.feras.ecogallery.EcoGallery;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint.Style;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author mnowrot
 * 
 */
public class EcoGalleryIndicator extends View {

	private EcoGallery mEcoGallery;
	private int mColor;
	private int mSize;
	private int mItemsCount;
	private ShapeDrawable mShape;
	private int mMaxItemsInLine;
	private int mLinesCount;

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

		initializeAttrs(context, attrs, defStyle);
		initializeShapes();
	}

	private void initializeShapes() {
		mShape = new ShapeDrawable(new OvalShape());
		mShape.getPaint().setColor(mColor);
	}

	private void initializeAttrs(Context context, AttributeSet attrs, int defStyle) {
		TypedArray a = null;
		try {
			a = context.obtainStyledAttributes(attrs, R.styleable.EcoGalleryIndicator, defStyle, 0);
			Resources res = context.getResources();
			mColor = a.getColor(R.styleable.EcoGalleryIndicator_color, res.getColor(android.R.color.background_light));
			mSize = a.getDimensionPixelSize(R.styleable.EcoGalleryIndicator_size,
					res.getDimensionPixelSize(R.dimen.ecoGalleryIndicator_item_size_default));
		} finally {
			a.recycle();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// super.onDraw(canvas);
		if (galleryHasAdapter()) {
			int selectedItemPosition = mEcoGallery.getSelectedItemPosition();
			for (int i = 0; i < mItemsCount; i++) {
				int leftBoundary = getLeftBoundary(i);
				int topBoundary = getTopBoundary(i);
				mShape.setBounds(leftBoundary, topBoundary, leftBoundary + mSize, topBoundary + mSize);
				if (i == selectedItemPosition) {
					mShape.getPaint().setStyle(Style.FILL);
				} else {
					mShape.getPaint().setStyle(Style.STROKE);
				}
				mShape.draw(canvas);
			}
		}
	}

	private int getTopBoundary(int position) {
		int rowNumber = getRowNumber(position);
		return getPaddingTop() + (rowNumber * (mSize + getPaddingTop()));
	}

	private int getRowNumber(int position) {
		return position / mMaxItemsInLine;
	}

	private int getLeftBoundary(int position) {
		int colNumber = getColNumber(position);
		return colNumber * (2 * mSize);
	}

	private int getColNumber(int position) {
		return position % mMaxItemsInLine;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
		mMaxItemsInLine = countMaxItemsInLine(parentWidth - getPaddingLeft() - getPaddingRight());
		mLinesCount = countLines();
		setMeasuredDimension(measureWidth(), measureHeight());
	}

	private int countLines() {
		int fullLines = mItemsCount / mMaxItemsInLine;
		int overflowLines = (mItemsCount % mMaxItemsInLine) != 0 ? 1 : 0;
		return fullLines + overflowLines;
	}

	private int countMaxItemsInLine(int parentWidth) {
		return parentWidth / (2 * mSize);
	}

	private int measureHeight() {
		return ((getPaddingTop() + mSize) * mLinesCount) + getPaddingBottom();
	}

	private int measureWidth() {
		int actualItemsInLine = Math.min(mMaxItemsInLine, mItemsCount);
		return getPaddingLeft() + (((2 * actualItemsInLine) - 1) * mSize) + getPaddingRight();
	}

	public EcoGallery getEcoGallery() {
		return mEcoGallery;
	}

	public void setEcoGallery(EcoGallery mEcoGallery) {
		this.mEcoGallery = mEcoGallery;
		if (galleryHasAdapter()) {
			mItemsCount = mEcoGallery.getAdapter().getCount();
			invalidate();
			requestLayout();
		}
	}

	private boolean galleryHasAdapter() {
		return (mEcoGallery != null) && (mEcoGallery.getAdapter() != null);
	}

}
