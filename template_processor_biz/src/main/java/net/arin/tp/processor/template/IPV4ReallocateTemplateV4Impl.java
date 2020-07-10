package net.arin.tp.processor.template;

import net.arin.tp.ipaddr.IPVersion;

public class IPV4ReallocateTemplateV4Impl extends ReallocateTemplateV4Impl
{
    public IPV4ReallocateTemplateV4Impl()
    {
    }

    public IPV4ReallocateTemplateV4Impl( String emailTemplate )
    {
        parseTemplate( emailTemplate );
    }

    @Override
    public IPVersion getIPVersion()
    {
        return IPVersion.IPV4;
    }
}
