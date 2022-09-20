package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class AddAlbum extends AppCompatActivity {
    private TextInputEditText input;
    private ArrayList<AlbumClass> albums;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_album);
        input=findViewById(R.id.albumNameInputText);
    }

    public void createAlbum(View view)
    {
        String newAlbumName=input.getText().toString();
        if(newAlbumName.equals("")) return; //don't do anything if the user didn't input anything
        if(checkDuplicates(newAlbumName))
        {
            Snackbar.make(view, "Album already exists", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        else
        {
            AlbumClass newAlbum=new AlbumClass(newAlbumName);
            Bundle bundle=new Bundle();
            bundle.putSerializable("newAlbum",newAlbum);
            Intent result=new Intent();
            result.putExtras(bundle);
            setResult(Activity.RESULT_OK, result);
            Snackbar.make(view, "Album creation successful", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            finish();
        }
    }

    private boolean checkDuplicates(String newAlbumName)
    {
        for(int i=0;i<DataStorage.albums.size();i++)
        {
            if(DataStorage.albums.get(i).getName().equals(newAlbumName))
            {
                return true;
            }
        }
        return false;
    }

}