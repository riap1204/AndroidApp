package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Slideshow extends AppCompatActivity {
    private AlbumClass album;
    private int albumIndex;
    private ImageView image;
    private int index;
    private TextView tagsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slideshow);
        image=findViewById(R.id.pictureDisplay);
        Bundle bundle=getIntent().getExtras();
        albumIndex=bundle.getInt("albumIndex");
        album=DataStorage.albums.get(albumIndex);
        image.setImageURI(album.pictures.get(0).getPhotoUri());
        index=0;
        tagsText=findViewById(R.id.photoTags);
        updateText();
    }

    public void back(View view)
    {
        index--;
        display(index);
    }

    public void forward(View view)
    {
        index++;
        display(index);
    }

    public void display(int index)
    {
        this.index=index;
        if(index<0) //these 2 if statements allow for wrapping of the pictures
        {
            display(album.pictures.size()-1);
            return;
        }
        if(index>=album.pictures.size())
        {
            display(0);
            return;
        }
        image.setImageURI(album.pictures.get(index).getPhotoUri());
        updateText();
    }

    private void updateText()
    {
        tagsText.setText(album.pictures.get(index).allTagsToString());
    }

}