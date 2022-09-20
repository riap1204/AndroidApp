package com.example.androidapp;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Picture_List extends ArrayAdapter {

    private ArrayList<PictureClass> pictureList;
    private String[] picture_names;
    private Activity context;

    public Picture_List(Activity context, String[] picture_names, ArrayList<PictureClass> pictureList)
    {
        super(context, R.layout.pictures, picture_names);
        this.picture_names=new String[pictureList.size()];
        for(int i=0; i< picture_names.length;i++)
        {
            picture_names[i]=pictureList.get(i).getName();
        }
        this.picture_names=picture_names;
        this.context=context;
        this.pictureList=pictureList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row=convertView;
        LayoutInflater inflater=context.getLayoutInflater();
        if(convertView==null)
        {
            row=inflater.inflate(R.layout.pictures,null, true);
        }
        TextView textViewName=(TextView) row.findViewById(R.id.picture_name);
        ImageView thumbnail=(ImageView) row.findViewById(R.id.thumbnail);
        textViewName.setText(picture_names[position]);
        thumbnail.setImageURI(pictureList.get(position).getPhotoUri());
        return row;
    }
}
