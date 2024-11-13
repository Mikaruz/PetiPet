package com.utp.petipet.ui.about_me;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AboutMeViewModel extends ViewModel {

    private final MutableLiveData<String> email = new MutableLiveData<>();
    private final MutableLiveData<String> provider = new MutableLiveData<>();

    public void setEmail(String email) {
        this.email.setValue(email);
    }

    public LiveData<String> getEmail() {
        return email;
    }

    public void setProvider(String provider) {
        this.provider.setValue(provider);
    }

    public LiveData<String> getProvider() {
        return provider;
    }
}