package com.example.androidapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PictureDisplay extends AppCompatActivity {

    private PictureClass picture;
    private AlbumClass album;
    private TextView tagText;
    private ArrayList<TagClass> tags;
    private int albumIndex;
    private int pictureIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_display);
        tagText=findViewById(R.id.photoTags);

        Bundle bundle=getIntent().getExtras();
        albumIndex=bundle.getInt("albumIndex");
        album=DataStorage.albums.get(albumIndex);
        pictureIndex=bundle.getInt("pictureIndex");
        picture=album.pictures.get(pictureIndex);
        ImageView pictureView=findViewById(R.id.pictureDisplay);
        pictureView.setImageURI(picture.getPhotoUri());
        tags= picture.getTags();
        tagText.setText(picture.allTagsToString());

    }

    public void deleteImage(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        String message="Are you sure you want to delete picture: ";
        message+=picture.getName();
        message+="?";
        builder.setMessage(message);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                album.pictures.remove(picture);
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

    public void addTag(View view)
    {
        Bundle bundle= new Bundle();
        bundle.putInt("albumIndex", albumIndex);
        bundle.putInt("pictureIndex", pictureIndex);
        Intent intent=new Intent(this, CreateTag.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

    public void deleteTag(View view)
    {
        Bundle bundle= new Bundle();
        bundle.putInt("albumIndex", albumIndex);
        bundle.putInt("pictureIndex", pictureIndex);
        Intent intent=new Intent(this, DeleteTag.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, 2);
    }

    public void onActivityResult(int requestcode, int resultcode, Intent data)  //update the tag text when returning from add tag or delete tag
    {
        super.onActivityResult(resultcode, resultcode, data);
        tagText.setText(picture.allTagsToString());
        if(requestcode==3 && resultcode==1)
        {
            finish();
        }
    }

    public void movePhoto(View view)
    {
        Bundle bundle= new Bundle();
        bundle.putInt("albumIndex", albumIndex);
        bundle.putInt("pictureIndex", pictureIndex);
        Intent intent=new Intent(this, Move.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, 3);
    }
}