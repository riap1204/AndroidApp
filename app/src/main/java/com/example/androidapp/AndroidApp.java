package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;


public class AndroidApp extends AppCompatActivity {

    private ListView albumList;
    private String[] albumNames;
    private String[] albumInfo;
    private static UserData data;
    private static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this.getApplicationContext();
        try     //loads save data
        {
            data=UserData.readapp(context);
        }
        catch (Exception e)
        {

        }
        if(data!=null)
        {
            DataStorage.albums= data.albums;
        }
        else
        {
            context.getDir("dat", Context.MODE_PRIVATE);   //creates a dat directory
            data=new UserData();
            data.albums=DataStorage.albums;
        }
                /*TODO store the save data when closing the app
                    I can't figure out how to store the data only on app close
                    so I might just store the data every time there's new data
                */
        //initializes the list
        albumList=findViewById(R.id.albumList); //find using the list's id

        updateList();

        albumList.setOnItemClickListener((p, v, pos, id)->showDetails(pos));
    }

    private void showDetails(int pos)
    {
        Bundle bundle=new Bundle();
        bundle.putInt("albumIndex", pos);
        Intent intent=new Intent(this, ShowAlbum.class);
        intent.setType(Intent.ACTION_OPEN_DOCUMENT);
        intent.putExtras(bundle);
        startActivityForResult(intent,2);
    }

    public void addAlbum(View view)
    {
        Intent intent=new Intent(this, AddAlbum.class);
        startActivityForResult(intent, 1);  //deprecated function but the new one is complicated so I'll just use this
    }

    public void updateList()    //necessary to update the list when an album is added and when first launching the app
    {
        //reset the array size
        albumNames=new String[DataStorage.albums.size()];


        for(int i=0; i<albumNames.length;i++)
        {
            albumNames[i]=DataStorage.albums.get(i).getName();
        }

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, R.layout.album, albumNames);    //sets the layout used for each item in the list. second param is the layout, third param is the array of strings
        albumList.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK && requestCode==1)   //1 means it's from add album and we create a new album here
        {
            Bundle bundle=data.getExtras();
            AlbumClass newAlbum=(AlbumClass)bundle.getSerializable("newAlbum");
            DataStorage.albums.add(newAlbum);
            updateList();
            saveData();
        }
        else if(requestCode==2) //2 is from showAlbum, update the list in case any deletions or renames happened
        {
            updateList();
        }
    }

    public static void saveData()
    {
        try {
            UserData.writeapp(data, context);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void search(View view)
    {
        Intent intent=new Intent(this, Search.class);
        startActivityForResult(intent, 1);  //deprecated function but the new one is complicated so I'll just use this
    }

}