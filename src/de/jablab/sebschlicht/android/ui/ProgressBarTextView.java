package de.jablab.sebschlicht.android.ui;

import android.R.color;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import de.jablab.sebschlicht.series.R;

public class ProgressBarTextView extends TextView {

    protected boolean _debug;

    protected ProgressBar _progressBar;

    protected int _progressColor;

    protected int _idleColor;

    public ProgressBarTextView(
            Context context) {
        super(context);
    }

    public ProgressBarTextView(
            Context context,
            AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ProgressBarTextView(
            Context context,
            AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    protected void init(AttributeSet attrs) {
        TypedArray a =
                getContext().obtainStyledAttributes(attrs,
                        R.styleable.ProgressBarTextView);

        try {
            setIdleColor(a.getColor(R.styleable.ProgressBarTextView_idleColor,
                    color.black));
            setProgressColor(a.getColor(
                    R.styleable.ProgressBarTextView_progressColor, color.white));
            int progressBarId =
                    a.getResourceId(
                            R.styleable.ProgressBarTextView_progressBar, -1);
            if (progressBarId != -1) {
                setProgressBar((ProgressBar) findViewById(progressBarId));
            }
        } finally {
            a.recycle();
        }
    }

    public void setProgressBar(ProgressBar progressBar) {
        _progressBar = progressBar;
    }

    public void setProgressColor(int progressColor) {
        _progressColor = progressColor;
    }

    public void setIdleColor(int idleColor) {
        _idleColor = idleColor;
    }

    public void setDebugMode(boolean debug) {
        _debug = debug;
    }

    @Override
    public void draw(Canvas canvas) {
        if (_progressBar == null) {
            super.draw(canvas);
            Log.i("ProgressBarTextView", "No progress bar set.");
            return;
        }
        int absOverlap =
                (int) (_progressBar.getWidth()
                        * (_progressBar.getProgress() / (float) _progressBar
                                .getMax()) - getLeft());
        float overlap = absOverlap / (float) getWidth();

        if (absOverlap > 0) {
            // text view is (up to fully) in progress
            if (_debug) {
                Log.d("ProgressBarTextView", "Text view is to " + overlap
                        + " in progress bar.");
            }
            canvas.save();
            setTextColor(_progressColor);
            canvas.clipRect(new Rect(0, 0, (int) (getWidth() * overlap),
                    getHeight()));
            super.draw(canvas);
            canvas.restore();
        }
        if (overlap < 1) {
            // text view is (up to fully) in idle range
            if (_debug) {
                Log.d("ProgressBarTextView", "Text view isn't in progress bar.");
            }
            canvas.save();
            setTextColor(_idleColor);
            canvas.clipRect(new Rect((int) (getWidth() * overlap), 0,
                    getWidth(), getHeight()));
            super.draw(canvas);
            canvas.restore();
        }
    }
}
