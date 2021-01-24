package ru.kostya.postforkowrk.viewmodles;

import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import ru.kostya.postforkowrk.models.User;
import ru.kostya.postforkowrk.repositories.MainRepositoryImpl;

public class MainViewModel extends ViewModel {

    private MainRepositoryImpl repository;

    private MutableLiveData<User> liveDataUser = new MutableLiveData<>();

    public MainViewModel(){
        repository = new MainRepositoryImpl();
    }

    public FirebaseUser getCurrentUser(){
        return repository.getCurrentUser();
    }

    //Получить юзера,который зашел
    public void getSignedUser(String uId){
        DatabaseReference currentUserReference = repository.getCurrentUserReference().child(uId);

        Log.d("CurrentUser","uid = " + uId);
        currentUserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("CurrentUser",dataSnapshot.getChildrenCount() + "");
                User currentUser = dataSnapshot.getValue(User.class);
                if (currentUser != null) {
                    Log.d("CurrentUser", currentUser.getEmail());
                    liveDataUser.setValue(currentUser);
                } else {
                    Log.d("CurrentUser","user == null " + currentUser.getEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public MutableLiveData<User> getLiveDataUser() {
        return liveDataUser;
    }


    public void updateDataUser(final String uId, final String name, final String email, final String password, Uri imageUri ,String extensionImageUri) {

        Log.d("CurrentUser","updatedATAUSER VIEWMODEL MAIN");

        Log.d("CurrentUser","extension mainviewmodel ->" + extensionImageUri);
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("ImagesProfiles/" + System.currentTimeMillis() + "." + extensionImageUri);

        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Log.d("CurrentUser","success download image");

                        // Get a URL to the uploaded content
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                if (uri != null){
                                    Log.d("CurrentUser","image url task -> " + uri.toString());

                                    DatabaseReference reference = repository.getCurrentUserReference().child(uId);

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("name",name);
                        userMap.put("email",email);
                        userMap.put("password",password);
                        userMap.put("imageUrl", uri.toString());

                        reference.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Log.d("CurrentUser","update success");
                                } else {
                                    Log.d("CurrentUser","update error " + task.getException().getMessage());
                                }
                            }
                        });

                                } else {
                                    Log.d("CurrentUser","image url task == null");
                                }
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("CurrentUser","failed download image " + exception.getMessage());
                    }
                });

    }

    public void addPost(String titlePost, String textPost, String imageUrlPost, String hourPublishPost, String publisherImageUrl, String publisherName) {


    }
}
