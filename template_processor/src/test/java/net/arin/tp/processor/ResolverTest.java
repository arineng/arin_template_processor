package net.arin.tp.processor;

import net.arin.tp.processor.template.IPV4NetModifyTemplateV4Impl;
import net.arin.tp.processor.template.IPV4NetModifyTemplateV5Impl;
import net.arin.tp.processor.template.IPV4ReassignDetailedTemplateV4Impl;
import net.arin.tp.processor.template.IPV4ReassignDetailedTemplateV5Impl;
import net.arin.tp.processor.template.IPV6NetModifyTemplateV4Impl;
import net.arin.tp.processor.template.IPV6NetModifyTemplateV5Impl;
import net.arin.tp.processor.template.OrgTemplateV4Impl;
import net.arin.tp.processor.template.OrgTemplateV5Impl;
import net.arin.tp.processor.template.PocTemplateV4Impl;
import net.arin.tp.processor.template.PocTemplateV5Impl;
import net.arin.tp.processor.template.ReassignSimpleTemplateV4Impl;
import net.arin.tp.processor.template.ReassignSimpleTemplateV5Impl;
import net.arin.tp.processor.template.TemplateImpl;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResolverTest extends BaseTest
{
    @Test
    public void testGetTemplate()
    {
        Assert.assertNotNull( Resolver.getTemplate( "ARIN-POC-4.0" ) );
        Assert.assertNotNull( Resolver.getTemplate( "ARIN-POC-5.0" ) );

        Assert.assertNotNull( Resolver.getTemplate( "ARIN-ORG-4.0" ) );
        Assert.assertNotNull( Resolver.getTemplate( "ARIN-ORG-5.0" ) );

        Assert.assertNotNull( Resolver.getTemplate( "ARIN-REASSIGN-SIMPLE-4.1" ) );
        Assert.assertNotNull( Resolver.getTemplate( "ARIN-REASSIGN-SIMPLE-5.0" ) );

        Assert.assertNotNull( Resolver.getTemplate( "ARIN-REASSIGN-DETAILED-4.2" ) );
        Assert.assertNotNull( Resolver.getTemplate( "ARIN-REASSIGN-DETAILED-5.0" ) );

        Assert.assertNotNull( Resolver.getTemplate( "ARIN-NET-MOD-4.1" ) );
        Assert.assertNotNull( Resolver.getTemplate( "ARIN-NET-MOD-5.0" ) );

        // Shouldn't matter what the minor version is.
        Assert.assertNotNull( Resolver.getTemplate( "ARIN-POC-4.999" ) );
        Assert.assertEquals( PocTemplateV4Impl.class.getName(), Resolver.getTemplate( "ARIN-POC-4.999" ).getClass().getName() );
        Assert.assertNotNull( Resolver.getTemplate( "ARIN-POC-5.999" ) );
        Assert.assertEquals( PocTemplateV5Impl.class.getName(), Resolver.getTemplate( "ARIN-POC-5.999" ).getClass().getName() );

        Assert.assertNotNull( Resolver.getTemplate( "ARIN-ORG-4.999" ) );
        Assert.assertEquals( OrgTemplateV4Impl.class.getName(), Resolver.getTemplate( "ARIN-ORG-4.999" ).getClass().getName() );
        Assert.assertNotNull( Resolver.getTemplate( "ARIN-ORG-5.999" ) );
        Assert.assertEquals( OrgTemplateV5Impl.class.getName(), Resolver.getTemplate( "ARIN-ORG-5.999" ).getClass().getName() );

        Assert.assertNotNull( Resolver.getTemplate( "ARIN-REASSIGN-SIMPLE-4.999" ) );
        Assert.assertEquals( ReassignSimpleTemplateV4Impl.class.getName(), Resolver.getTemplate( "ARIN-REASSIGN-SIMPLE-4.999" ).getClass().getName() );
        Assert.assertNotNull( Resolver.getTemplate( "ARIN-REASSIGN-SIMPLE-5.999" ) );
        Assert.assertEquals( ReassignSimpleTemplateV5Impl.class.getName(), Resolver.getTemplate( "ARIN-REASSIGN-SIMPLE-5.999" ).getClass().getName() );

        Assert.assertNotNull( Resolver.getTemplate( "ARIN-REASSIGN-DETAILED-4.999" ) );
        Assert.assertEquals( IPV4ReassignDetailedTemplateV4Impl.class.getName(), Resolver.getTemplate( "ARIN-REASSIGN-DETAILED-4.999" ).getClass().getName() );
        Assert.assertNotNull( Resolver.getTemplate( "ARIN-REASSIGN-DETAILED-5.999" ) );
        Assert.assertEquals( IPV4ReassignDetailedTemplateV5Impl.class.getName(), Resolver.getTemplate( "ARIN-REASSIGN-DETAILED-5.999" ).getClass().getName() );

        Assert.assertNotNull( Resolver.getTemplate( "ARIN-NET-MOD-4.999" ) );
        Assert.assertEquals( IPV4NetModifyTemplateV4Impl.class.getName(), Resolver.getTemplate( "ARIN-NET-MOD-4.999" ).getClass().getName() );
        Assert.assertNotNull( Resolver.getTemplate( "ARIN-NET-MOD-5.999" ) );
        Assert.assertEquals( IPV4NetModifyTemplateV5Impl.class.getName(), Resolver.getTemplate( "ARIN-NET-MOD-5.999" ).getClass().getName() );

        Assert.assertNull( Resolver.getTemplate( "ARIN-UNKNOWN-5.0" ) );
    }

    @Test
    public void testGetTransformer()
    {
        Assert.assertNotNull( Resolver.getTransformer( new PocTemplateV4Impl() ) );
        Assert.assertNotNull( Resolver.getTransformer( new PocTemplateV5Impl() ) );
        Assert.assertNotNull( Resolver.getTransformer( new OrgTemplateV4Impl() ) );
        Assert.assertNotNull( Resolver.getTransformer( new OrgTemplateV5Impl() ) );
        Assert.assertNotNull( Resolver.getTransformer( new ReassignSimpleTemplateV4Impl() ) );
        Assert.assertNotNull( Resolver.getTransformer( new ReassignSimpleTemplateV5Impl() ) );
        Assert.assertNotNull( Resolver.getTransformer( new IPV4ReassignDetailedTemplateV4Impl() ) );
        Assert.assertNotNull( Resolver.getTransformer( new IPV4ReassignDetailedTemplateV5Impl() ) );
        Assert.assertNotNull( Resolver.getTransformer( new IPV4NetModifyTemplateV4Impl() ) );
        Assert.assertNotNull( Resolver.getTransformer( new IPV4NetModifyTemplateV5Impl() ) );
        Assert.assertNotNull( Resolver.getTransformer( new IPV6NetModifyTemplateV4Impl() ) );
        Assert.assertNotNull( Resolver.getTransformer( new IPV6NetModifyTemplateV5Impl() ) );

        Assert.assertNull( Resolver.getTransformer( new TemplateImpl()
        {
            @Override
            protected void mapKeyValuePair( String key, String value )
            {
            }

            @Override
            protected List<String> getFieldKeyValues()
            {
                return new ArrayList<>();
            }

            @Override
            public Map<String, String> getXmlToFieldKeyMapping()
            {
                return new HashMap<>();
            }

            @Override
            protected boolean isMultiLineField( String fieldName )
            {
                return true;
            }

            @Override
            public String getTemplateName()
            {
                return "SOMETEMPLATE";
            }

            @Override
            public String getTemplateVersion()
            {
                return "99.0";
            }
        } ) );
    }

    @Test
    public void testGetValidator()
    {
        Assert.assertNotNull( Resolver.getValidator( new PocTemplateV4Impl() ) );
        Assert.assertNotNull( Resolver.getValidator( new PocTemplateV5Impl() ) );
        Assert.assertNotNull( Resolver.getValidator( new OrgTemplateV4Impl() ) );
        Assert.assertNotNull( Resolver.getValidator( new OrgTemplateV5Impl() ) );
        Assert.assertNotNull( Resolver.getValidator( new ReassignSimpleTemplateV4Impl() ) );
        Assert.assertNotNull( Resolver.getValidator( new ReassignSimpleTemplateV5Impl() ) );
        Assert.assertNotNull( Resolver.getValidator( new IPV4ReassignDetailedTemplateV4Impl() ) );
        Assert.assertNotNull( Resolver.getValidator( new IPV4ReassignDetailedTemplateV5Impl() ) );
        Assert.assertNotNull( Resolver.getValidator( new IPV4NetModifyTemplateV4Impl() ) );
        Assert.assertNotNull( Resolver.getValidator( new IPV4NetModifyTemplateV5Impl() ) );
        Assert.assertNotNull( Resolver.getValidator( new IPV6NetModifyTemplateV4Impl() ) );
        Assert.assertNotNull( Resolver.getValidator( new IPV6NetModifyTemplateV5Impl() ) );

        Assert.assertNull( Resolver.getValidator( new TemplateImpl()
        {
            @Override
            protected void mapKeyValuePair( String key, String value )
            {
            }

            @Override
            protected List<String> getFieldKeyValues()
            {
                return new ArrayList<>();
            }

            @Override
            public Map<String, String> getXmlToFieldKeyMapping()
            {
                return new HashMap<>();
            }

            @Override
            protected boolean isMultiLineField( String fieldName )
            {
                return true;
            }

            @Override
            public String getTemplateName()
            {
                return "SOMETEMPLATE";
            }

            @Override
            public String getTemplateVersion()
            {
                return "99.0";
            }
        } ) );
    }
}
