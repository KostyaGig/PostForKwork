package ru.kostya.postforkowrk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.io.FileReader;

import ru.kostya.postforkowrk.constans.Firebase;

public class PostActivity extends AppCompatActivity {

    // for get nameUser and imageUser
    private String nameUser;
    private String imageUrlUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        if (getIntent().hasExtra(Firebase.PUBLISHER_NAME)){
            nameUser = getIntent().getStringExtra(Firebase.PUBLISHER_NAME);
            imageUrlUser = getIntent().getStringExtra(Firebase.PUBLISHER_IMAGE_URL);
        }
    }
}