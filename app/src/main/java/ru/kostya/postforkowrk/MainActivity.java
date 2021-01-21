package ru.kostya.postforkowrk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import ru.kostya.postforkowrk.constans.Firebase;
import ru.kostya.postforkowrk.models.User;
import ru.kostya.postforkowrk.view.auth.RegisterActivity;
import ru.kostya.postforkowrk.viewmodles.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private User currentUser;

    //Сделать профиль юзера

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        if (getIntent().hasExtra(Firebase.EXTRA_USER_EMAIL)){
            Toast.makeText(this, getIntent().getStringExtra(Firebase.EXTRA_USER_EMAIL), Toast.LENGTH_SHORT).show();
        }
    }

    private void init() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (viewModel.getCurrentUser() == null) {
            Toast.makeText(this, "Войдите в аккаунт", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        }

        //Получить юзера,который вошел,обернуть в livedata
//        currentUser = viewModel.getSignedUser();
    }
}