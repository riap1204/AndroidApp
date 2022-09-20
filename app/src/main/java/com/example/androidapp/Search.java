package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ListView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class Search extends AppCompatActivity {
    private TextInputEditText searchInput;
    private ListView resultListView;
    private ArrayList<PictureClass> pictureList=new ArrayList<PictureClass>();
    private String[] pictureNames;
    private int pointer;    //why doesn't java have primitive pointers
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        resultListView=findViewById(R.id.searchResults);
        searchInput=findViewById(R.id.searchInputText);
        updateList();
        resultListView.setOnItemClickListener((p,v,pos,id)->displayPhoto(pos));
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                parseInput();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void updateList()
    {
        //reset the array size
        pictureNames=new String[pictureList.size()];

        for(int i=0; i<pictureNames.length;i++)
        {
            pictureNames[i]=pictureList.get(i).getName();
        }

        Picture_List pictureListAdapter=new Picture_List(this, pictureNames, pictureList);
        resultListView.setAdapter(pictureListAdapter);
    }

    private void displayPhoto(int pos)
    {
        Bundle bundle=new Bundle();
        bundle.putInt("albumIndex", pictureList.get(pos).getBelongsToAlbum());

        AlbumClass album=DataStorage.albums.get(pictureList.get(pos).getBelongsToAlbum());
        for(int i=0; i<album.pictures.size(); i++)  //pos is not the same the the index of the picture in the album. find the correct index using this for loop
        {
            if(pictureList.get(pos).equals(album.pictures.get(i)))
            {
                bundle.putInt("pictureIndex", i);
            }
        }
        Intent intent=new Intent(this, PictureDisplay.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, 3);
    }

    private void parseInput()
    {
        String input=searchInput.getText().toString();
        input=input.toLowerCase();
        TagClass firstTag=null;
        TagClass secondTag=null;
        int andor=0;
        pointer=0;

        if(input.length()==0)
        {
            search(null, 0, null);
            return;
        }
        firstTag=parseTag(input);
        if(firstTag==null)
        {
            search(null, 0, null);
            return;
        }
        if(pointer==input.length())
        {
            search(firstTag,0,null);
            return;
        }
        pointer++;
        skipWhitespace(input);
        if(pointer==input.length()) //if there was only whitespace (no andor operator)
        {
            search(firstTag, 0,null);
            return;
        }

        String operator="";
        while(pointer<input.length()&&!Character.isWhitespace(input.charAt(pointer)))
        {
            operator+=input.charAt(pointer);
            pointer++;
        }
        final String and="and";
        final String or="or";
        if(and.startsWith(operator))
        {
            andor=1;
        }
        else if(or.startsWith(operator))
        {
            andor=2;
        }
        else
        {
            andor=0;
        }
        if(pointer==input.length())
        {
            search(firstTag, andor, null);
            return;
        }
        pointer++;
        skipWhitespace(input);
        if(pointer==input.length())
        {
            search(firstTag, andor, null);
            return;
        }

        secondTag=parseTag(input);
        System.out.println(secondTag);
        search(firstTag, andor, secondTag);
        return;

    }

    private TagClass parseTag(String input)
    {
        String TagTypeString="";
        TagClass.Type tagType;
        for(; pointer<input.length()&&input.charAt(pointer)!='='; pointer++)  //parsing one character at a time because this has to be real time
        {
            TagTypeString+=input.charAt(pointer);
        }
        final String person="person";
        final String location="location";
        if(person.startsWith(TagTypeString))
        {
            tagType=TagClass.Type.PERSON;
        }
        else if(location.startsWith(TagTypeString))
        {
            tagType=TagClass.Type.LOCATION;
        }
        else
        {
            return null;
        }

        if(pointer==input.length()||pointer==input.length()-1)
        {
            return new TagClass(tagType, "");
        }
        pointer++;  //to skip past the equal sign
        if(pointer<input.length()&&input.charAt(pointer)!='\"')
        {
            Snackbar.make(findViewById(R.id.searchResults), "Syntax error: Please encase the tag's value with \"", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return new TagClass(tagType, "");
        }
        pointer++;  //skip past the first "
        String TagValue="";
        for(;pointer<input.length()&&input.charAt(pointer)!='\"';pointer++)
        {
            TagValue+=input.charAt(pointer);
        }
        return new TagClass(tagType, TagValue);
    }

    private void skipWhitespace(String input)
    {
        while(pointer<input.length()&&Character.isWhitespace(input.charAt(pointer)))
        {
            pointer++;
        }
    }

    private void search(TagClass first, int andor, TagClass second)
    {
        if(first==null)
        {
            pictureList=new ArrayList<PictureClass>();
            updateList();
            return;
        }
        ArrayList<PictureClass> firstMatches=new ArrayList<PictureClass>();
        ArrayList<PictureClass> secondMatches=new ArrayList<PictureClass>();

        findMatchingPhotos(firstMatches, first);
        if(andor==0||second==null)
        {
            pictureList=firstMatches;
            updateList();
            return;
        }
        findMatchingPhotos(secondMatches, second);
        if(andor==1)//1 is and
        {
            ArrayList<PictureClass> result=new ArrayList<PictureClass>();

            for(int i=0; i<firstMatches.size();i++)
            {
                PictureClass picture= firstMatches.get(i);
                if(secondMatches.contains(picture))
                {
                    result.add(picture);
                }
            }
            pictureList=result;
            updateList();
            return;
        }
        if(andor==2)//2 is or
        {
            for(int i=0; i<firstMatches.size();i++)
            {
                PictureClass picture=firstMatches.get(i);
                if(!secondMatches.contains(picture))
                {
                    secondMatches.add(picture);
                }
            }
            pictureList=secondMatches;
            updateList();
            return;
        }
    }

    private void findMatchingPhotos(ArrayList<PictureClass> result, TagClass tag)
    {
        ArrayList<AlbumClass> albums=DataStorage.albums;
        for(int i=0; i<albums.size();i++)
        {
            AlbumClass currentAlbum=albums.get(i);
            for(int j=0; j<currentAlbum.pictures.size();j++)
            {
                PictureClass currentPicture=currentAlbum.pictures.get(j);
                for(int k=0; k<currentPicture.getTags().size();k++)
                {
                    TagClass currentTag=currentPicture.getTags().get(k);
                    String currentTagValue=currentTag.getValue().toLowerCase();
                    if(currentTag.getType()==tag.getType()&&(currentTagValue.startsWith(tag.getValue())||tag.getValue().equals("")))
                    {
                        if(!result.contains(currentPicture))
                        {
                            result.add(currentPicture);
                        }
                        break;
                    }
                }
            }
        }
    }

    public void onActivityResult(int requestcode, int resultcode, Intent data)
    {
        super.onActivityResult(requestcode, resultcode, data);
        parseInput();
    }
}