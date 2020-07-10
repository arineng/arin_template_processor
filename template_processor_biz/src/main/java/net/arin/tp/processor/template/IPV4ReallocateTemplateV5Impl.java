package net.arin.tp.processor.template;

import net.arin.tp.ipaddr.IPVersion;

public class IPV4ReallocateTemplateV5Impl extends ReallocateTemplateV5Impl
{
    public IPV4ReallocateTemplateV5Impl()
    {
    }

    public IPV4ReallocateTemplateV5Impl( String emailTemplate )
    {
        parseTemplate( emailTemplate );
    }

    @Override
    public IPVersion getIPVersion()
    {
        return IPVersion.IPV4;
    }
}
