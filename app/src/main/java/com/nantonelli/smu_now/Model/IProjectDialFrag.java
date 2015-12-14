package com.nantonelli.smu_now.Model;

import android.support.v4.app.DialogFragment;

/**
 * Created by ndantonelli on 12/10/15.
 */
public interface IProjectDialFrag {

    public void onDialogPositiveClick(DialogFragment dialog, String name);
    public void onDialogNegativeClick(DialogFragment dialog);

}
