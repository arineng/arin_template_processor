package net.arin.tp.processor.template;

import net.arin.tp.ipaddr.IPVersion;

public class IPV4ReassignDetailedTemplateV5Impl extends ReassignDetailedTemplateV5Impl
{
    public IPV4ReassignDetailedTemplateV5Impl()
    {
    }

    public IPV4ReassignDetailedTemplateV5Impl( String emailTemplate )
    {
        parseTemplate( emailTemplate );
    }

    @Override
    public IPVersion getIPVersion()
    {
        return IPVersion.IPV4;
    }
}
