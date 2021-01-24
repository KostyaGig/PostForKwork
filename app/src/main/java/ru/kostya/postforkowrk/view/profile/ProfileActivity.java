package ru.kostya.postforkowrk.view.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import ru.kostya.postforkowrk.R;
import ru.kostya.postforkowrk.constans.Firebase;
import ru.kostya.postforkowrk.view.main.MainActivity;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int REQUEST_IMAGE = 1;
    private BottomNavigationView bottomBar;
    private Toolbar toolbar;

    private EditText fieldName;
    private ImageView profileImage;
    private Button updateButton;

    private Uri imageUri;

    //Поля для получения юзера из mainactivty
    private String name;
    private String email;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        init();

        if (getIntent().hasExtra(Firebase.NAME_USER)){

            name = getIntent().getStringExtra(Firebase.NAME_USER);
            email = getIntent().getStringExtra(Firebase.EMAIL_USER);
            imageUrl = getIntent().getStringExtra(Firebase.IMAGE_URL_USER);

            //getIntent form mainActivity
            getSupportActionBar().setTitle(name);
            toolbar.setTitleTextColor(getResources().getColor(R.color.defaultLineEditText));

            setUpView();
        }

        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home_item:
                        startActivity(new Intent(ProfileActivity.this, MainActivity.class));

                        break;
                    case R.id.profile_item:

                        break;
                }

                return true;
            }
        });

        updateButton.setOnClickListener(this);
        profileImage.setOnClickListener(this);
    }

    private void setUpView() {
        fieldName.setText(name);
        if (imageUrl.equals("null")){
            //Пусть будет фотка по дефолту
            Log.d("CurrentUser","imageurl == null setup in profile activity");
        } else {
            Log.d("CurrentUser","load url - " + imageUrl);
            Glide.with(ProfileActivity.this).load(imageUrl).into(profileImage);
        }
    }

    private void init() {

        fieldName = findViewById(R.id.field_name);
        profileImage = findViewById(R.id.profile_image);
        updateButton = findViewById(R.id.update_btn);
        bottomBar = findViewById(R.id.bottom_bar);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public void onClick(View view) {
         switch (view.getId()){
             case R.id.update_btn:
                 //Проверка на заполненные поля
                 verifyUserData();
                 break;
             case R.id.profile_image:
                 Intent imageIntent = new Intent(Intent.ACTION_GET_CONTENT);
                 imageIntent.setType("image/*");
                 startActivityForResult(imageIntent, REQUEST_IMAGE);
                 break;
         }
    }

    private void verifyUserData() {

        if (TextUtils.isEmpty(fieldName.getText().toString())){
            Toast.makeText(this, "Введите новое имя!", Toast.LENGTH_SHORT).show();
        } else if(imageUri == null){
            Toast.makeText(this, "Выберите новое фото!", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent();

            intent.putExtra(Firebase.NAME_USER,fieldName.getText().toString());
            intent.putExtra(Firebase.IMAGE_URL_USER,imageUri);
            intent.putExtra(Firebase.EXTENSION_IMAGE_URL_USER,getFileExtension(imageUri));

            setResult(RESULT_OK,intent);
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK &&  data.getData() != null){
            imageUri = data.getData();

            Log.d("CurrentUser","image uri onactresult profileactivity --> " + imageUri.toString());
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                profileImage.setImageBitmap(bitmap);
            }catch (Exception e){
                Log.d("CurrentUser","failed set bitmap profile image in PROFILEACTIVITY");
                e.printStackTrace();
            }

        } else {
            Toast.makeText(this, "Выбор фотографии отменен", Toast.LENGTH_SHORT).show();
        }

    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}