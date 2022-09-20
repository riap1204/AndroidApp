package com.example.androidapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;

import android.Manifest;
import android.content.Context;
import android.content.ContextParams;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ShowAlbum extends AppCompatActivity {
    private AlbumClass currentAlbum;
    private String[] pictureNames;
    private ArrayList<PictureClass> pictureList;
    private Uri[] imageUris;
    private ListView pictureListView;
    private UserData data;
    private TextView albumNameView;
    private int albumIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_album);


        //retrieve the album from the bundle, set all relevant variables accordingly
        //the bundle can not pass by reference, so we get the index of the album instead and retrieve from DataStorage
        Bundle bundle=getIntent().getExtras();
        albumIndex=bundle.getInt("albumIndex");
        currentAlbum=DataStorage.albums.get(albumIndex);
        String albumName= currentAlbum.getName();
        pictureList= currentAlbum.pictures;

        albumNameView=findViewById(R.id.album_name);   //displays the name of the album at the top
        albumNameView.setText(albumName);

        pictureListView=(ListView)findViewById(R.id.picture_list);
        updateList();

        pictureListView.setOnItemClickListener((p,v,pos,id)->displayPhoto(pos));
    }

    private void displayPhoto(int pos)
    {
        Bundle bundle=new Bundle();
        bundle.putInt("albumIndex", albumIndex);
        bundle.putInt("pictureIndex", pos);
        Intent intent=new Intent(this, PictureDisplay.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, 3);
    }

    public void updateList()    //similar to album list
    {
        //reset the array size
        pictureNames=new String[pictureList.size()];

        for(int i=0; i<pictureNames.length;i++)
        {
            pictureNames[i]=pictureList.get(i).getName();
        }

        Picture_List pictureListAdapter=new Picture_List(this, pictureNames, pictureList);
        pictureListView.setAdapter(pictureListAdapter);
    }


    //this whole section is copy and pasted from https://developer.android.com/guide/components/intents-common#Storage
    static final int REQUEST_IMAGE_GET = 1;

    public void selectImage(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK)
        {
            Uri fullPhotoUri = data.getData();
            DocumentFile file = DocumentFile.fromSingleUri(this, fullPhotoUri);
            PictureClass newPicture=new PictureClass(getFileName(fullPhotoUri),fullPhotoUri, albumIndex);
            if(currentAlbum.contains(newPicture))
            {
                Snackbar.make(findViewById(R.id.showAlbumConstraintLayout), "Picture is already in album", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            else
            {
                currentAlbum.add(newPicture);
                updateList();

                AndroidApp.saveData();
            }
        }
        if(requestCode==2 && resultCode==RESULT_OK) //reusing the AddAlbum screen.
        {
            Bundle bundle=data.getExtras();
            AlbumClass newAlbum=(AlbumClass)bundle.getSerializable("newAlbum");
            currentAlbum.setName(newAlbum.getName());
            albumNameView.setText(currentAlbum.getName());
            AndroidApp.saveData();
        }
        if(requestCode==3)
        {
            updateList();
        }
    }

    //pasted from stack overflow
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public void deleteAlbum(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        String message="Are you sure you want to delete Album: ";
        message+=currentAlbum.getName();
        message+="?";
        builder.setMessage(message);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                DataStorage.albums.remove(currentAlbum);
                AndroidApp.saveData();
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void renameAlbum(View view)
    {
        Intent intent=new Intent(this, AddAlbum.class);
        startActivityForResult(intent, 2);
    }

    public void slideshow(View view)
    {
        if(currentAlbum.pictures.isEmpty())
        {
            Snackbar.make(findViewById(R.id.showAlbumConstraintLayout), "no pictures to display", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }
        Bundle bundle=new Bundle();
        bundle.putInt("albumIndex", albumIndex);
        Intent intent=new Intent(this, Slideshow.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, 4);
    }
}