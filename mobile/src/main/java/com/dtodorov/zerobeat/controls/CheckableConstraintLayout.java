package com.dtodorov.zerobeat.controls;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.Checkable;

import com.dtodorov.zerobeat.R;

/**
 * Created by diman on 7/15/2017.
 */

public class CheckableConstraintLayout extends ConstraintLayout implements Checkable
{
    private boolean isChecked = false;

    public CheckableConstraintLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public void setChecked(boolean checked)
    {
        this.isChecked = checked;
        changeColor(isChecked);
    }

    @Override
    public boolean isChecked()
    {
        return this.isChecked;
    }

    @Override
    public void toggle()
    {
        this.isChecked = !this.isChecked;
        changeColor(isChecked);
    }

    private void changeColor(boolean isChecked){
        if(this.isChecked)
        {
            setBackgroundColor(getResources().getColor(R.color.accent));
        }
        else
        {
            setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }
    }
}
