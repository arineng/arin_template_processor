package net.arin.tp.processor.template;

import net.arin.tp.ipaddr.IPVersion;

import java.util.ArrayList;
import java.util.List;

/**
 * Template implementation for IPV6 net modifications using the version 5 template.
 */
public class IPV6NetModifyTemplateV5Impl extends NetModifyTemplateImpl
{
    public static final String TEMPLATE_VERSION = "5.0";

    public IPV6NetModifyTemplateV5Impl()
    {
    }

    public IPV6NetModifyTemplateV5Impl( String emailTemplate )
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

    @Override
    public IPVersion getIPVersion()
    {
        return IPVersion.IPV6;
    }
}
