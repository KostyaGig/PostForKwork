package ru.kostya.postforkowrk.view.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ru.kostya.postforkowrk.PostActivity;
import ru.kostya.postforkowrk.R;
import ru.kostya.postforkowrk.constans.Firebase;
import ru.kostya.postforkowrk.models.User;
import ru.kostya.postforkowrk.view.auth.RegisterActivity;
import ru.kostya.postforkowrk.view.profile.ProfileActivity;
import ru.kostya.postforkowrk.viewmodles.MainViewModel;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_EDIT_PROFILE = 1;
    public static final int REQUEST_CODE_ADD_POST = 2;

    private Toolbar toolbar;

    private MainViewModel viewModel;
    private Observer<User> currentUserObserver;
    private User currentUser;
    private BottomNavigationView bottomBar;

    //Сделать профиль юзера

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        if (getIntent().hasExtra(Firebase.EXTRA_USER_EMAIL)){
            Toast.makeText(this, getIntent().getStringExtra(Firebase.EXTRA_USER_EMAIL), Toast.LENGTH_SHORT).show();
        }

        currentUserObserver = new Observer<User>() {
            @Override
            public void onChanged(User user) {
                currentUser = user;
                Log.d("CurrentUser","mainactivity " + currentUser.getEmail());
            }
        };


        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home_item:

                        return true;
                    case R.id.profile_item:
                      Intent profileActivity = new Intent(MainActivity.this, ProfileActivity.class).putExtra(Firebase.NAME_USER,currentUser.getName()).putExtra(Firebase.EMAIL_USER,currentUser.getEmail()).putExtra(Firebase.IMAGE_URL_USER,currentUser.getImageUrl()
                        );
                        startActivityForResult(profileActivity,REQUEST_EDIT_PROFILE);

                        return true;
                }

                return true;
            }
        });

    }

    private void init() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Лента");
        bottomBar = findViewById(R.id.bottom_bar);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("CurrentUser","onstart mainactivity");
        if (viewModel.getCurrentUser() == null) {
            Toast.makeText(this, "Войдите в аккаунт", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        } else {
            //Получить юзера,который вошел
            viewModel.getSignedUser(viewModel.getCurrentUser().getUid());
            viewModel.getLiveDataUser().observe(this,currentUserObserver);

            //Отображение firebaserecycler with all posts

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_EDIT_PROFILE && resultCode == RESULT_OK){

            Log.d("CurrentUser","onactresult mAINACTIVITY");
            String name = data.getStringExtra(Firebase.NAME_USER);
            Uri imageUri = data.getParcelableExtra(Firebase.IMAGE_URL_USER);

            Log.d("CurrentUser","image uri onactresult mainactivity --> " + imageUri.toString());

            String extensionUriUser = data.getStringExtra(Firebase.EXTENSION_IMAGE_URL_USER);

            viewModel.updateDataUser(viewModel.getCurrentUser().getUid(),name,currentUser.getEmail(),currentUser.getPassword(),imageUri,extensionUriUser);

        } else if (requestCode == REQUEST_CODE_ADD_POST && resultCode == RESULT_OK){
            Log.d("CurrentPost","onactivity result mainactivity POST");

            String titlePost = data.getStringExtra(Firebase.TITLE_POST);
            String textPost = data.getStringExtra(Firebase.TEXT_POST);
            String imageUrlPost = data.getStringExtra(Firebase.IMAGE_URL_POST);
            String hourPublishPost = data.getStringExtra(Firebase.HOUR_PUBLISH_POST);
            String publisherImageUrl = data.getStringExtra(Firebase.PUBLISHER_IMAGE_URL);
            String publisherName = data.getStringExtra(Firebase.PUBLISHER_NAME);

            viewModel.addPost(titlePost,textPost,imageUrlPost,hourPublishPost,publisherImageUrl,publisherName);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.add_post_item){

            Intent intent = new Intent(MainActivity.this, PostActivity.class);
            intent.putExtra(Firebase.PUBLISHER_IMAGE_URL,currentUser.getImageUrl());
            intent.putExtra(Firebase.PUBLISHER_NAME,currentUser.getName());

            startActivityForResult(intent, REQUEST_CODE_ADD_POST);
        }

        return true;
    }
}