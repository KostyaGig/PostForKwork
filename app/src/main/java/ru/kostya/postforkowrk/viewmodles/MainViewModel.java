package ru.kostya.postforkowrk.viewmodles;

import android.view.View;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

import ru.kostya.postforkowrk.repositories.MainRepositoryImpl;

public class MainViewModel extends ViewModel {

    private MainRepositoryImpl repository;

    public MainViewModel(){
        repository = new MainRepositoryImpl();
    }

    public FirebaseUser getCurrentUser(){
        return repository.getCurrentUser();
    }

}
