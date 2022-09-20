package com.example.androidapp;

import java.io.Serializable;
import java.util.ArrayList;

public class AlbumClass implements Serializable
{
    ArrayList<PictureClass> pictures=new ArrayList<PictureClass>();
    private String name;

    public AlbumClass(String name)
    {
        this.name=name;
    }

    public boolean add(PictureClass picture)	//returns false if the picture already exists in the album
    {
        if(!pictures.contains(picture))
        {
            pictures.add(picture);
            return true;
        }
        else
        {
            return false;
        }
    }

    public String getName()
    {
        return name;
    }

    public boolean contains(PictureClass picture)
    {
        for(int i=0; i< pictures.size();i++)
        {
            PictureClass current=pictures.get(i);
            if(current.equals(picture))
            {
                return true;
            }
        }
        return false;
    }

    public void setName(String name)
    {
        this.name=name;
    }
}
