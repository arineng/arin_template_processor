package net.arin.tp.processor.template;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ReallocateTemplateTest extends ComplexSwipTemplateTest
{
    @Test
    public void testCreateTemplate40()
    {
        TemplateImpl template = TemplateImpl.createTemplate( getComplexTemplate( TemplateType.REALLOCATE, TemplateVersion.FOUR ) );
        Assert.assertNotNull( template );
        Assert.assertTrue( template instanceof ReallocateTemplateImpl );
        Assert.assertEquals( template.getTemplateVersion(), "4.2" );
        assertValuesMatch( ( ReallocateTemplateImpl ) template );
    }

    @Test
    public void testParseTemplate40()
    {
        ReallocateTemplateImpl template = new IPV4ReallocateTemplateV4Impl( getComplexTemplate( TemplateType.REALLOCATE, TemplateVersion.FOUR ) );
        assertValuesMatch( template );
    }

    @Test
    public void testCreateTemplate50()
    {
        TemplateImpl template = TemplateImpl.createTemplate( getComplexTemplate( TemplateType.REALLOCATE, TemplateVersion.FIVE ) );
        Assert.assertNotNull( template );
        Assert.assertTrue( template instanceof ReallocateTemplateImpl );
        Assert.assertEquals( template.getTemplateVersion(), "5.0" );
        assertValuesMatch( ( ReallocateTemplateImpl ) template );
    }

    @Test
    public void testParseTemplate50()
    {
        ReallocateTemplateImpl template = new IPV4ReallocateTemplateV5Impl( getComplexTemplate( TemplateType.REALLOCATE, TemplateVersion.FIVE ) );
        assertValuesMatch( template );
    }

    @Test
    public void testCreateTemplate40IPV6()
    {
        TemplateImpl template = TemplateImpl.createTemplate( getComplexTemplateIPVersion6( TemplateType.REALLOCATE, TemplateVersion.FOUR ) );
        Assert.assertNotNull( template );
        Assert.assertTrue( template instanceof IPV6ReallocateTemplateV4Impl );
        assertValuesMatchV6Template( ( IPV6ReallocateTemplateV4Impl ) template );
    }

    @Test
    public void testParseTemplate40IPV6()
    {
        ReallocateTemplateImpl template = new IPV6ReallocateTemplateV4Impl( getComplexTemplateIPVersion6( TemplateType.REALLOCATE, TemplateVersion.FOUR ) );
        assertValuesMatchV6Template( template );
    }

    @Test
    public void testCreateTemplate50IPV6()
    {
        TemplateImpl template = TemplateImpl.createTemplate( getComplexTemplateIPVersion6( TemplateType.REALLOCATE, TemplateVersion.FIVE ) );
        Assert.assertNotNull( template );
        Assert.assertTrue( template instanceof IPV6ReallocateTemplateV5Impl );
        assertValuesMatchV6Template( ( IPV6ReallocateTemplateV5Impl ) template );
    }

    @Test
    public void testParseTemplate50IPV6()
    {
        ReallocateTemplateImpl template = new IPV6ReallocateTemplateV5Impl( getComplexTemplateIPVersion6( TemplateType.REALLOCATE, TemplateVersion.FIVE ) );
        assertValuesMatchV6Template( template );
    }
}
