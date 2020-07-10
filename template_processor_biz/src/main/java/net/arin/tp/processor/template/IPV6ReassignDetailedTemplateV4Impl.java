package net.arin.tp.processor.template;

import net.arin.tp.ipaddr.IPVersion;

import java.util.ArrayList;
import java.util.List;

public class IPV6ReassignDetailedTemplateV4Impl extends ReassignDetailedTemplateV4Impl
{
    public IPV6ReassignDetailedTemplateV4Impl()
    {
    }

    public IPV6ReassignDetailedTemplateV4Impl( String emailTemplate )
    {
        parseTemplate( emailTemplate );
    }

    @Override
    protected List<String> getFieldKeyValues()
    {
        List<String> mutableFields = new ArrayList<>();
        mutableFields.add( JUSTIFICATION_LABEL );
        mutableFields.addAll( super.getFieldKeyValues() );

        return mutableFields;
    }

    protected boolean isMultiLineField( String fieldName )
    {
        if ( fieldName.equals( normalizeKey( JUSTIFICATION_LABEL ) ) )
        {
            return true;
        }
        else
        {
            return super.isMultiLineField( fieldName );
        }
    }

    @Override
    public IPVersion getIPVersion()
    {
        return IPVersion.IPV6;
    }
}
