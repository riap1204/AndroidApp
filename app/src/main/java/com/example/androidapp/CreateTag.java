package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class CreateTag extends AppCompatActivity {
    private PictureClass currentPicture;
    private RadioGroup TagType;
    private RadioButton TypePerson;
    private RadioButton TypeLocation;
    private TextInputEditText input;
    private TagClass tag;
    private TagClass.Type t;
    private AlbumClass album;
    private int albumIndex;
    private int pictureIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_tag);
        Bundle bundle = getIntent().getExtras();
        albumIndex=bundle.getInt("albumIndex");
        pictureIndex=bundle.getInt("pictureIndex");
        album=DataStorage.albums.get(albumIndex);
        currentPicture=album.pictures.get(pictureIndex);
        input = findViewById(R.id.tagValueText);
        TagType = (RadioGroup) findViewById(R.id.tagType);
        TypePerson = (RadioButton) findViewById(R.id.typePerson);
        TypeLocation = (RadioButton) findViewById(R.id.typeLocation);

    }

    public void confirmTag(View view)
    {
        if (TypeLocation.isChecked()) {
            t = TagClass.Type.LOCATION;
        }
        else if (TypePerson.isChecked()) {
            t = TagClass.Type.PERSON;
        }
        else
        {
            return;
        }

        String inp = input.getText().toString();
        if(!currentPicture.addTag(t, inp))
        {
            Snackbar.make(view, "Tag already exists", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }
        Bundle bundle= new Bundle();
        bundle.putSerializable("picture",currentPicture);
        Intent result=new Intent();
        result.putExtras(bundle);
        setResult(Activity.RESULT_OK, result);
        AndroidApp.saveData();
        finish();
    }
}