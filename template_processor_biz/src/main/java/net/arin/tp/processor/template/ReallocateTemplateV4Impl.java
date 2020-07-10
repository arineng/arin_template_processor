package net.arin.tp.processor.template;

public abstract class ReallocateTemplateV4Impl extends ReallocateTemplateImpl
{
    public static final String TEMPLATE_VERSION = "4.2";

    @Override
    public String getTemplateVersion()
    {
        return TEMPLATE_VERSION;
    }
}
