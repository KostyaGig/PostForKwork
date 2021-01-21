package ru.kostya.postforkowrk.repositories;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.kostya.postforkowrk.constans.Firebase;
import ru.kostya.postforkowrk.view.interfaces.MainI;

public class MainRepositoryImpl implements MainI {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

}
