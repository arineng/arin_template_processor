package net.arin.tp.processor.template;

import net.arin.tp.processor.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TemplateParsingTest extends BaseTest
{
    private static final String KNOWN_TEMPLATE_TEXT = "Template: ARIN-POC-5.0\n";

    @Test
    public void testCreateTemplateFailsForSpamTemplate()
    {
        TemplateImpl template = TemplateImpl.createTemplate( "The scooby snack teaches the tornado." );
        Assert.assertNull( template );
    }

    @Test
    public void testCreateTemplateFailsForUnassignableTemplate()
    {
        TemplateImpl template = TemplateImpl.createTemplate( "Template: ARIN-FOOBAR-5.0\n" );
        Assert.assertNotNull( template );
        Assert.assertTrue( template instanceof UnassignableTemplate );
    }

    @Test
    public void testCreateTemplateFailsForRepliedToTemplate()
    {
        TemplateImpl template = TemplateImpl.createTemplate( ">" + KNOWN_TEMPLATE_TEXT );
        Assert.assertNull( template );
    }

    @Test
    public void testCreateTemplateSucceedsForKnownTemplateWithoutWhitespace()
    {
        TemplateImpl template = TemplateImpl.createTemplate( KNOWN_TEMPLATE_TEXT );
        Assert.assertNotNull( template );
        Assert.assertFalse( template instanceof UnassignableTemplate );
    }

    @Test
    public void testCreateTemplateSucceedsForKnownTemplateWithWhitespace()
    {
        TemplateImpl template = TemplateImpl.createTemplate( "\t " + KNOWN_TEMPLATE_TEXT );
        Assert.assertNotNull( template );
        Assert.assertFalse( template instanceof UnassignableTemplate );
    }
}
