package com.example.androidapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.snackbar.Snackbar;

public class Move extends AppCompatActivity {
    private ListView albumList;
    private PictureClass picture;
    private AlbumClass album;
    private int albumIndex;
    private int pictureIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.move);
        albumList=findViewById(R.id.albumListMove);
        Bundle bundle=getIntent().getExtras();
        albumIndex=bundle.getInt("albumIndex");
        pictureIndex=bundle.getInt("pictureIndex");
        album=DataStorage.albums.get(albumIndex);
        picture=album.pictures.get(pictureIndex);
        String[] albumNames;
        albumNames=new String[DataStorage.albums.size()];


        for(int i=0; i<albumNames.length;i++)
        {
            albumNames[i]=DataStorage.albums.get(i).getName();
        }

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, R.layout.album, albumNames);    //sets the layout used for each item in the list. second param is the layout, third param is the array of strings
        albumList.setAdapter(adapter);
        albumList.setOnItemClickListener((p, v, pos, id)->movePhoto(pos));
    }

    private void movePhoto(int pos)
    {
        if(picture.getBelongsToAlbum()==pos)
        {
            Snackbar.make(albumList, "picture belongs to this album already", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        String message="Are you sure you want to move picture: ";
        message+=picture.getName();
        message+=" from album: ";
        message+=album.getName();
        message+=" to album: ";
        message+=DataStorage.albums.get(pos).getName();
        message+="?";
        builder.setMessage(message);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                album.pictures.remove(picture);
                DataStorage.albums.get(pos).pictures.add(picture);
                picture.setBelongsToAlbum(pos);
                AndroidApp.saveData();
                setResult(1);
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