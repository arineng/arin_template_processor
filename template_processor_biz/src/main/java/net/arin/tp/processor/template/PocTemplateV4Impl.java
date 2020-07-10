package net.arin.tp.processor.template;

public class PocTemplateV4Impl extends PocTemplateImpl
{
    public static final String TEMPLATE_VERSION = "4.0";

    public PocTemplateV4Impl()
    {
    }

    public PocTemplateV4Impl( String emailTemplate )
    {
        parseTemplate( emailTemplate );
    }

    @Override
    public String getTemplateVersion()
    {
        return TEMPLATE_VERSION;
    }
}
