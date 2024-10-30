package com.utp.petipet.ui.about_me;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AboutMeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AboutMeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is YOYO fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}