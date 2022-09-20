package com.example.androidapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class DeleteTag extends AppCompatActivity {
    private PictureClass currentPicture;
    private TextInputEditText input;
    private TagClass CurrentTag;
    private TagClass.Type t;
    private AlbumClass album;
    private ArrayList<TagClass> tags;
    private ListView listView;
    private int albumIndex;
    private int pictureIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_tag);
        Bundle bundle = getIntent().getExtras();
        albumIndex=bundle.getInt("albumIndex");
        pictureIndex=bundle.getInt("pictureIndex");
        album=DataStorage.albums.get(albumIndex);
        currentPicture=album.pictures.get(pictureIndex);
        tags= currentPicture.getTags();

        listView = findViewById(R.id.tagList);
        listView.setAdapter(
                new ArrayAdapter<TagClass>(this, R.layout.album,tags));

        // TO DO
        listView.setOnItemClickListener((list, view, pos, id) -> deleteTag(pos));

    }

    public void deleteTag(int pos)
    {
        CurrentTag=tags.get(pos);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        String message="Are you sure you want to delete Tag: ";
        message+=CurrentTag.getValue();
        message+="?";
        builder.setMessage(message);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                currentPicture.removeTag(CurrentTag);
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
}