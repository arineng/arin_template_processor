package net.arin.tp.processor.template;

import java.util.ArrayList;
import java.util.List;

public class PocTemplateV5Impl extends PocTemplateImpl
{
    public static final String TEMPLATE_VERSION = "5.0";

    public PocTemplateV5Impl()
    {
    }

    public PocTemplateV5Impl( String emailTemplate )
    {
        parseTemplate( emailTemplate );
    }

    @Override
    public String getTemplateVersion()
    {
        return TEMPLATE_VERSION;
    }

    @Override
    protected List<String> getFieldKeyValues()
    {
        List<String> mutableFields = new ArrayList<>();
        mutableFields.add( API_KEY_LABEL );
        mutableFields.addAll( super.getFieldKeyValues() );

        return mutableFields;
    }
}
