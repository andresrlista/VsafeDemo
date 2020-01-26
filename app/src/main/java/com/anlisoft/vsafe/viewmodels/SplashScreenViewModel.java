package com.anlisoft.vsafe.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.anlisoft.vsafe.models.response.MunicipalidadResponse;
import com.anlisoft.vsafe.repositories.SplashScreenRepository;


public class SplashScreenViewModel extends ViewModel {

    private MutableLiveData<MunicipalidadResponse> municipalidadResponseMutableLiveData = new MutableLiveData<>();
    private SplashScreenRepository mSplashScreenRepository = null;

    public void initialize() {

        if (municipalidadResponseMutableLiveData == null) {
            return;
        }
        mSplashScreenRepository = SplashScreenRepository.getInstance();
        municipalidadResponseMutableLiveData = mSplashScreenRepository.getDataResponseMunicipalidades();
    }

    public LiveData<MunicipalidadResponse> getMunicipalidades() {
        return municipalidadResponseMutableLiveData;
    }


}
