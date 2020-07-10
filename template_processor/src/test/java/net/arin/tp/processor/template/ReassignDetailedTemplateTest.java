package net.arin.tp.processor.template;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ReassignDetailedTemplateTest extends ComplexSwipTemplateTest
{
    @Test
    public void testCreateTemplate40()
    {
        TemplateImpl template = TemplateImpl.createTemplate( getComplexTemplate( TemplateType.REASSIGN_DETAILED, TemplateVersion.FOUR ) );
        Assert.assertNotNull( template );
        Assert.assertTrue( template instanceof ReassignDetailedTemplateImpl );
        assertValuesMatch( ( ReassignDetailedTemplateImpl ) template );
    }

    @Test
    public void testParseTemplate40()
    {
        ReassignDetailedTemplateImpl template = new IPV4ReassignDetailedTemplateV4Impl( getComplexTemplate( TemplateType.REASSIGN_DETAILED, TemplateVersion.FOUR ) );
        assertValuesMatch( template );
    }

    @Test
    public void testCreateTemplate50()
    {
        TemplateImpl template = TemplateImpl.createTemplate( getComplexTemplate( TemplateType.REASSIGN_DETAILED, TemplateVersion.FIVE ) );
        Assert.assertNotNull( template );
        Assert.assertTrue( template instanceof ReassignDetailedTemplateImpl );
        assertValuesMatch( ( ReassignDetailedTemplateImpl ) template );
    }

    @Test
    public void testParseTemplate50()
    {
        ReassignDetailedTemplateImpl template = new IPV4ReassignDetailedTemplateV5Impl( getComplexTemplate( TemplateType.REASSIGN_DETAILED, TemplateVersion.FIVE ) );
        assertValuesMatch( template );
    }

    @Test
    public void testCreateTemplate40IPV6()
    {
        TemplateImpl template = TemplateImpl.createTemplate( getComplexTemplateIPVersion6( TemplateType.REASSIGN_DETAILED, TemplateVersion.FOUR ) );
        Assert.assertNotNull( template );
        Assert.assertTrue( template instanceof IPV6ReassignDetailedTemplateV4Impl );
        assertValuesMatchV6Template( ( IPV6ReassignDetailedTemplateV4Impl ) template );
    }

    @Test
    public void testParseTemplate40IPV6()
    {
        ReassignDetailedTemplateImpl template = new IPV6ReassignDetailedTemplateV4Impl( getComplexTemplateIPVersion6( TemplateType.REASSIGN_DETAILED, TemplateVersion.FOUR ) );
        assertValuesMatchV6Template( template );
    }

    @Test
    public void testCreateTemplate50IPV6()
    {
        TemplateImpl template = TemplateImpl.createTemplate( getComplexTemplateIPVersion6( TemplateType.REASSIGN_DETAILED, TemplateVersion.FIVE ) );
        Assert.assertNotNull( template );
        Assert.assertTrue( template instanceof IPV6ReassignDetailedTemplateV5Impl );
        assertValuesMatchV6Template( ( IPV6ReassignDetailedTemplateV5Impl ) template );
    }

    @Test
    public void testParseTemplate50IPV6()
    {
        ReassignDetailedTemplateImpl template = new IPV6ReassignDetailedTemplateV5Impl( getComplexTemplateIPVersion6( TemplateType.REASSIGN_DETAILED, TemplateVersion.FIVE ) );
        assertValuesMatchV6Template( template );
    }
}
