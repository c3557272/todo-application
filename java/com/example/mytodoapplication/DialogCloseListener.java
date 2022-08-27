package com.example.mytodoapplication;

import android.content.DialogInterface;

//Inteface for the method that is responsible for checking dialog box closes properly.
public interface DialogCloseListener {
    public void handleDialogClose(DialogInterface dialog);
}

