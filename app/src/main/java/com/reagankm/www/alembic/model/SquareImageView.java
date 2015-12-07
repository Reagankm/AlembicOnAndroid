package com.reagankm.www.alembic.model;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * A special ImageView class to create perfectly square images (for use
 * as the DirectoryActivity pictures).
 *
 * @author Reagan Middlebrook
 * @version Phase 2
 */
public class SquareImageView extends ImageView
{
    /**
     * Create a square image view with the given context.
     *
     * @param context the context
     */
    public SquareImageView(Context context)
    {
        super(context);
    }

    /**
     * Create a square image view with the given context and attributes.
     *
     * @param context the context
     * @param attrs the attributes
     */
    public SquareImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    /**
     * Create a square image view with the given context, attributes, and style.
     *
     * @param context the context
     * @param attrs the attributes
     * @param defStyle the style
     */
    public SquareImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    /**
     * Set measurement dimensions.
     *
     * @param widthMeasure  the width
     * @param heightMeasure the height
     */
    @Override
    protected void onMeasure(int widthMeasure, int heightMeasure)
    {
        super.onMeasure(widthMeasure, heightMeasure);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}
