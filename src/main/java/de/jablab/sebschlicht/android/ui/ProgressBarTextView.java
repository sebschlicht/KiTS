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
import de.jablab.sebschlicht.kits.R;

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
        this.init(attrs);
    }

    public ProgressBarTextView(
            Context context,
            AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(attrs);
    }

    protected void init(AttributeSet attrs) {
        TypedArray a =
                this.getContext().obtainStyledAttributes(attrs,
                        R.styleable.ProgressBarTextView);

        try {
            this.setIdleColor(a.getColor(R.styleable.ProgressBarTextView_idleColor,
                    color.black));
            this.setProgressColor(a.getColor(
                    R.styleable.ProgressBarTextView_progressColor, color.white));
            int progressBarId =
                    a.getResourceId(R.styleable.ProgressBarTextView_progressBar, -1);
            if (progressBarId != -1) {
                this.setProgressBar((ProgressBar) this.findViewById(progressBarId));
            }
        } finally {
            a.recycle();
        }
    }

    public void setProgressBar(ProgressBar progressBar) {
        this._progressBar = progressBar;
    }

    public void setProgressColor(int progressColor) {
        this._progressColor = progressColor;
    }

    public void setIdleColor(int idleColor) {
        this._idleColor = idleColor;
    }

    public void setDebugMode(boolean debug) {
        this._debug = debug;
    }

    @Override
    public void draw(Canvas canvas) {
        if (this._progressBar == null) {
            super.draw(canvas);
            Log.i("ProgressBarTextView", "No progress bar set.");
            return;
        }
        int absOverlap =
                (int) (this._progressBar.getWidth()
                        * (this._progressBar.getProgress() / (float) this._progressBar
                                .getMax()) - this.getLeft());
        float overlap = absOverlap / (float) this.getWidth();

        if (absOverlap > 0) {
            // text view is (up to fully) in progress
            if (this._debug) {
                Log.d("ProgressBarTextView", "Text view is to " + overlap
                        + " in progress bar.");
            }
            canvas.save();
            this.setTextColor(this._progressColor);
            canvas.clipRect(new Rect(0, 0, (int) (this.getWidth() * overlap), this
                    .getHeight()));
            super.draw(canvas);
            canvas.restore();
        }
        if (overlap < 1) {
            // text view is (up to fully) in idle range
            if (this._debug) {
                Log.d("ProgressBarTextView", "Text view isn't in progress bar.");
            }
            canvas.save();
            this.setTextColor(this._idleColor);
            canvas.clipRect(new Rect((int) (this.getWidth() * overlap), 0, this
                    .getWidth(), this.getHeight()));
            super.draw(canvas);
            canvas.restore();
        }
    }
}
