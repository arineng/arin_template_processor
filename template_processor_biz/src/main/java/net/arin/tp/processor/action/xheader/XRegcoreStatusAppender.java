package net.arin.tp.processor.action.xheader;

public class XRegcoreStatusAppender extends XHeaderAppender
{
    static final public String NAME = "X-Regcore-Status";

    @Override
    public String getHeaderName()
    {
        return NAME;
    }

    @Override
    public String getHeaderValue()
    {
        return "";
    }
}
