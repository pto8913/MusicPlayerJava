package pto.Controller.ListView;

import java.io.File;
import java.io.Serializable;

public class MusicData implements Serializable
{
    private static final long serialVersionUID = 32492L;

    private String absolutePath;
    protected String fileName;
    
    public MusicData(File file)
    {
        fileName = file.getName();
        absolutePath = file.getAbsolutePath();
    }
    public MusicData(String name)
    {
        fileName = name;
    }
    public MusicData()
    {
    }
    
    public String getName()
    {
        return fileName;
    }
    public void setName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getPath()
    {
        return absolutePath;
    }
    public void setPath(String absolutePath)
    {
        this.absolutePath = absolutePath;
    }

    @Override
    public int hashCode()
    {
        return fileName != null ? fileName.hashCode() : 0;
    }

    @Override
    public String toString()
    {
        if (absolutePath != null)
        {
            return String.format(
                "{\"name\":\"%s\", \"path\":\"%s\"}", 
                fileName, 
                absolutePath.replace("\\", "/")
            );
        }
        else
        {
            return String.format(
                "{\"name\":\"%s\"}", 
                fileName
            );
        }
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }

        MusicData musicData = (MusicData)obj;
        if (musicData != null)
        {
            return musicData.getName().equals(getName());
        }
        String musicString = (String)obj;
        if (musicString != null)
        {
            return musicString.equals(getName());
        }
        return false;
    }
}

