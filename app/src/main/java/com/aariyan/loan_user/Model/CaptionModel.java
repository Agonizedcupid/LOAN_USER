package com.aariyan.loan_user.Model;

import android.net.Uri;

public class CaptionModel {
    private String editTextValue;
    private Uri multipleImage;

    public CaptionModel() {}

    public CaptionModel(Uri multipleImage) {
        this.multipleImage = multipleImage;
    }

    public Uri getMultipleImage() {
        return multipleImage;
    }

    public void setMultipleImage(Uri multipleImage) {
        this.multipleImage = multipleImage;
    }

    public String getEditTextValue() {
        return editTextValue;
    }

    public void setEditTextValue(String editTextValue) {
        this.editTextValue = editTextValue;
    }
}
