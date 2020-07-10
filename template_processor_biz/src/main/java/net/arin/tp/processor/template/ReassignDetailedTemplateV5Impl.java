package net.arin.tp.processor.template;

import java.util.ArrayList;
import java.util.List;

public abstract class ReassignDetailedTemplateV5Impl extends ReassignDetailedTemplateImpl
{
    public static final String TEMPLATE_VERSION = "5.0";

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
