package pto;

public class AppContext {
    public String tag = ".";

    @Override
    public boolean equals(Object o)
    {
        if (o == null)
        {
            return false;
        }
        AppContext that = (AppContext)o;
        return that.tag == this.tag;
    }
}