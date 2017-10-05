package com.nassagroup.tools;

import android.graphics.drawable.Drawable;

/**
 * Created by hollyn.derisse on 17/08/2017.
 */

public class AppDetail {
    public CharSequence getLabel() {
        return label;
    }

    public void setLabel(CharSequence label) {
        this.label = label;
    }

    public CharSequence getName() {
        return name;
    }

    public void setName(CharSequence name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    CharSequence label;
    CharSequence name;
    Drawable icon;
}