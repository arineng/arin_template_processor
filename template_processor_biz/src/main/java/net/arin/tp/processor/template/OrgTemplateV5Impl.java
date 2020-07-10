package net.arin.tp.processor.template;

import java.util.ArrayList;
import java.util.List;

public class OrgTemplateV5Impl extends OrgTemplateImpl
{
    public static final String TEMPLATE_VERSION = "5.0";

    public OrgTemplateV5Impl()
    {
    }

    public OrgTemplateV5Impl( String emailTemplate )
    {
        parseTemplate( emailTemplate );
    }

    @Override
    protected List<String> getFieldKeyValues()
    {
        List<String> mutableFields = new ArrayList<>();
        mutableFields.add( API_KEY_LABEL );
        mutableFields.addAll( super.getFieldKeyValues() );

        return mutableFields;
    }

    @Override
    public String getTemplateVersion()
    {
        return TEMPLATE_VERSION;
    }
}
