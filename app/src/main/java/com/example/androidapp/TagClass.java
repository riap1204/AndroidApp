package com.example.androidapp;

import java.io.Serializable;

public class TagClass implements Serializable
{
    enum Type
    {
        LOCATION,
        PERSON
    }

    private Type type;
    private String value;

    public TagClass(Type type, String value)
    {
        this.type=type;
        this.value=value;
    }

    public Type getType()
    {
        return type;
    }

    public String getValue()
    {
        return value;
    }

    public String toString()
    {
        String typeString="";
        if(this.type==Type.LOCATION)
        {
            typeString="Location:";
        }
        else if(this.type==Type.PERSON)
        {
            typeString="Person:";
        }
        String tagString=typeString+this.value;
        return tagString;
    }

    public boolean equals(Object o)
    {
        if(!(o instanceof TagClass))
        {
            return false;
        }
        else
        {
            TagClass temp=(TagClass)o;
            if(this.type==temp.type&&this.value.equals(temp.value))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }
}
