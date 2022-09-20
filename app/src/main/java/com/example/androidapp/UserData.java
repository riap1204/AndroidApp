package com.example.androidapp;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class UserData implements Serializable
{
    public ArrayList<AlbumClass> albums;

    public static void writeapp(UserData savedata, Context context) throws IOException
    {
        File dir=context.getDir("dat", Context.MODE_PRIVATE);
        ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(dir.toString()+"save.dat"));
        oos.writeObject(savedata);
    }

    public static UserData readapp(Context context) throws IOException, ClassNotFoundException
    {
        File dir=context.getDir("dat", Context.MODE_PRIVATE);
        ObjectInputStream ois=new ObjectInputStream(new FileInputStream(dir.toString()+"save.dat"));
        UserData savedata=(UserData)ois.readObject();
        return savedata;
    }
}
