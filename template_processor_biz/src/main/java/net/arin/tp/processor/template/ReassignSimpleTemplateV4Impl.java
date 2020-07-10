package net.arin.tp.processor.template;

public class ReassignSimpleTemplateV4Impl extends ReassignSimpleTemplateImpl
{
    public static final String TEMPLATE_VERSION = "4.1";

    public ReassignSimpleTemplateV4Impl()
    {
    }

    public ReassignSimpleTemplateV4Impl( String emailTemplate )
    {
        parseTemplate( emailTemplate );
    }

    @Override
    public String getTemplateVersion()
    {
        return TEMPLATE_VERSION;
    }
}
