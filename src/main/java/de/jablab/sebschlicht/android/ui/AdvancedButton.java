package de.jablab.sebschlicht.android.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.Button;

public class AdvancedButton extends Button {

    public AdvancedButton(
            Context context) {
        super(context);
    }

    public AdvancedButton(
            Context context,
            AttributeSet attrs) {
        super(context, attrs);
    }

    public AdvancedButton(
            Context context,
            AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            getBackground().setColorFilter(null);
        } else {
            getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SCREEN);
        }
    }
}
