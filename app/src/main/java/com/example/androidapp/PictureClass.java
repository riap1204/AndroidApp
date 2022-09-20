package com.example.androidapp;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

public class PictureClass implements Serializable
{
    private ArrayList<TagClass> tags=new ArrayList<TagClass>();
    private String name;
    private String photoUri;
    private int belongsToAlbum;     /*need this because search function bypasses selecting an album first
                                      but the delete photo button relies on already knowing which album it is in
                                      this makes it so we don't need that anymore and can instead call getBelongsToAlbum
                                      and then delete using that index
                                      */

    public ArrayList<TagClass> getTags()
    {
        return tags;
    }

    public boolean addTag(TagClass.Type type, String value)
    {
        TagClass tag=new TagClass(type, value);
        for(int i=0; i<tags.size();i++)
        {
            if(tags.get(i).equals(tag))
            {
                return false;
            }
        }
        this.tags.add(tag);
        return true;
    }

    public void removeTag(TagClass tag)
    {
        this.tags.remove(tag);
    }

    public PictureClass(String name, Uri photoUri, int album)
    {
        this.name=name;
        this.photoUri=photoUri.toString();
        belongsToAlbum=album;
    }

    public String allTagsToString()
    {
        String result="";
        for(int i=0; i<tags.size();i++)
        {
            result+=" "+tags.get(i).toString();
        }
        return result;
    }

    public String getName()
    {
        return name;
    }

    public boolean equals(Object o)
    {
        if(!(o instanceof PictureClass))
        {
            return false;
        }
        else
        {
            PictureClass temp=(PictureClass) o;
            if(temp.photoUri.equals(this.photoUri))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    public Uri getPhotoUri()
    {
        return Uri.parse(photoUri);
    }

    public void setBelongsToAlbum(int albumIndex)
    {
        belongsToAlbum=albumIndex;
    }

    public int getBelongsToAlbum()
    {
        return belongsToAlbum;
    }
}
