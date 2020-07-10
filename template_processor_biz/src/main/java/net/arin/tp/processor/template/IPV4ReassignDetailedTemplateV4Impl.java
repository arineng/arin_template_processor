package net.arin.tp.processor.template;

import net.arin.tp.ipaddr.IPVersion;

public class IPV4ReassignDetailedTemplateV4Impl extends ReassignDetailedTemplateV4Impl
{

    public IPV4ReassignDetailedTemplateV4Impl()
    {
    }

    public IPV4ReassignDetailedTemplateV4Impl( String emailTemplate )
    {
        parseTemplate( emailTemplate );
    }

    @Override
    public IPVersion getIPVersion()
    {
        return IPVersion.IPV4;
    }
}
