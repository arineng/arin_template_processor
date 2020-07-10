package net.arin.tp.processor.template;

public class OrgTemplateV4Impl extends OrgTemplateImpl
{
    public static final String TEMPLATE_VERSION = "4.0";

    public OrgTemplateV4Impl()
    {
    }

    public OrgTemplateV4Impl( String emailTemplate )
    {
        parseTemplate( emailTemplate );
    }

    @Override
    public String getTemplateVersion()
    {
        return TEMPLATE_VERSION;
    }
}
