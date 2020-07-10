package net.arin.tp.processor.template;

import net.arin.tp.ipaddr.IPVersion;

/**
 * Template implementation for IPV4 net modifications using the version 4 template.
 */
public class IPV4NetModifyTemplateV4Impl extends NetModifyTemplateImpl
{
    public static final String TEMPLATE_VERSION = "4.1";

    public IPV4NetModifyTemplateV4Impl()
    {
    }

    public IPV4NetModifyTemplateV4Impl( String emailTemplate )
    {
        parseTemplate( emailTemplate );
    }

    @Override
    public String getTemplateVersion()
    {
        return TEMPLATE_VERSION;
    }

    @Override
    public IPVersion getIPVersion()
    {
        return IPVersion.IPV4;
    }
}
