package net.arin.tp.processor.template;

import net.arin.tp.ipaddr.IPVersion;

public abstract class ReassignDetailedTemplateImpl extends ComplexSwipTemplateImpl
{
    private static final String IPV4_TEMPLATE_NAME = "ARIN-REASSIGN-DETAILED";
    private static final String IPV6_TEMPLATE_NAME = "ARIN-IPv6-REASSIGN";

    @Override
    public String getTemplateName()
    {
        return getIPVersion() == IPVersion.IPV4 ? IPV4_TEMPLATE_NAME : IPV6_TEMPLATE_NAME;
    }
}
