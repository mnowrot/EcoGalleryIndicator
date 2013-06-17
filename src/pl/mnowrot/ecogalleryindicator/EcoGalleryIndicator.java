/**
 * 
 */
package pl.mnowrot.ecogalleryindicator;

import us.feras.ecogallery.EcoGallery;
import us.feras.ecogallery.EcoGalleryAdapterView;
import us.feras.ecogallery.EcoGalleryAdapterView.OnItemSelectedListener;
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
	private OnItemSelectedListener mOnItemSelectedListener;

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
		super.onDraw(canvas);
		if (galleryHasAdapter()) {
			mShape.getPaint().setColor(mColor);
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

	private boolean galleryHasAdapter() {
		return (mEcoGallery != null) && (mEcoGallery.getAdapter() != null);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
		this.mEcoGallery.setOnItemSelectedListener(new SelectionChangedAdapter());
		if (galleryHasAdapter()) {
			mItemsCount = mEcoGallery.getAdapter().getCount();
			redraw();
		}
	}

	public int getColor() {
		return mColor;
	}

	/**
	 * Sets indicator items color. The color needs to be obtained via getResources().getColor(int colorId) before
	 * passing it to this method
	 * 
	 * @param mColor
	 */
	public void setColor(int mColor) {
		this.mColor = mColor;
		redraw();
	}

	public int getSize() {
		return mSize;
	}

	/**
	 * Size of the indicator item in raw pixels. Use getResources().getDimensionPixelSize(int dimensResourceId) to
	 * obtain it before passing to this method
	 * 
	 * @param mSize
	 */
	public void setSize(int mSize) {
		this.mSize = mSize;
		redraw();
	}

	private void redraw() {
		invalidate();
		requestLayout();
	}

	public OnItemSelectedListener getmOnItemSelectedListener() {
		return mOnItemSelectedListener;
	}

	/**
	 * Routing method for the OnItemSelectedListener used previously directly on the EcoGallery
	 * 
	 * @param mOnItemSelectedListener
	 */
	public void setmOnItemSelectedListener(OnItemSelectedListener mOnItemSelectedListener) {
		this.mOnItemSelectedListener = mOnItemSelectedListener;
	}

	private class SelectionChangedAdapter implements OnItemSelectedListener {

		@Override
		public void onItemSelected(EcoGalleryAdapterView<?> parent, View view, int position, long id) {
			// execute the externally set listener first
			if (mOnItemSelectedListener != null) {
				mOnItemSelectedListener.onItemSelected(parent, view, position, id);
			}
			redraw();
		}

		@Override
		public void onNothingSelected(EcoGalleryAdapterView<?> parent) {
			if (mOnItemSelectedListener != null) {
				mOnItemSelectedListener.onNothingSelected(parent);
			}
			redraw();
		}
	}

}
