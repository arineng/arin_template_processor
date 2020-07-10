package net.arin.tp.processor.template;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a holding class for anything we recognize as a template but is unsupported by us in the new software. It
 * allows it to pass through to the template processor where we'll determine it's not anything we can handle.
 */
public class UnassignableTemplate extends TemplateImpl
{
    @Override
    public String getTemplateName()
    {
        return "unassignable template";
    }

    @Override
    public String getTemplateVersion()
    {
        return null;
    }

    @Override
    protected void mapKeyValuePair( String key, String value )
    {
        // Do nothing.
    }

    @Override
    protected boolean isMultiLineField( String fieldName )
    {
        return true;
    }

    @Override
    protected List<String> getFieldKeyValues()
    {
        return Collections.singletonList( API_KEY_LABEL );
    }

    @Override
    public Map<String, String> getXmlToFieldKeyMapping()
    {
        return new HashMap<>();
    }
}
