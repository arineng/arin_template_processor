package net.arin.tp.api.payload;

import net.arin.tp.api.BaseTest;
import org.apache.commons.lang.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class MultilineTextPayloadTest extends BaseTest
{
    @Test
    public void testPocMultilineFields()
    {
        List<String> publicComments = Arrays.asList( "These", "are", "some", "public", "comments" );
        PocPayload pocPayload = new PocPayload();

        // First, test that setting with a string works.
        pocPayload.setPublicComments( StringUtils.join( publicComments, "\n" ) );

        Assert.assertTrue( pocPayload.getMultilinePublicComments().getLines().size() > 0 );

        for ( Integer i = 0; i < publicComments.size(); i++ )
        {
            String payloadLineText = pocPayload.getMultilinePublicComments().getLines().get( i ).getText();
            Integer payloadLineNum = pocPayload.getMultilinePublicComments().getLines().get( i ).getNumber();
            String pubCommentLine = publicComments.get( i );

            log.debug( pubCommentLine + " ?= " + payloadLineText );
            Assert.assertEquals( pubCommentLine, payloadLineText );

            log.debug( i + " ?= " + payloadLineNum );
            Assert.assertEquals( i, payloadLineNum );
        }

        // Second, test that setting with a multiline payload works.
        MultilineTextPayload mtp = pocPayload.getMultilinePublicComments();

        pocPayload.setPublicComments( null );

        Assert.assertNull( pocPayload.getMultilinePublicComments() );

        pocPayload.setMultilinePublicComments( mtp );

        log.debug( StringUtils.join( publicComments, "\n" ) + " ?= " + pocPayload.getPublicComments() );
        Assert.assertEquals( StringUtils.join( publicComments, "\n" ), pocPayload.getPublicComments() );
    }

    @Test
    public void testEquality()
    {
        MultilineTextPayload mtp = new MultilineTextPayload( "Test comment - here we go\nAnd another line" );
        MultilineTextPayload mtp2 = new MultilineTextPayload( "Test comment - here we go\nAnd another line" );

        Assert.assertEquals( mtp, mtp2 );
        Assert.assertEquals( mtp2, mtp );

        MultilineTextPayload mtp3 = new MultilineTextPayload( "Test comment - here we go - single line" );
        MultilineTextPayload mtp4 = new MultilineTextPayload( "Test comment - here we go - single line" );

        Assert.assertEquals( mtp3, mtp4 );
        Assert.assertEquals( mtp4, mtp3 );

        MultilineTextPayload mtp5 = new MultilineTextPayload();
        MultilineTextPayload mtp6 = new MultilineTextPayload();

        Assert.assertEquals( mtp5, mtp6 );
        Assert.assertEquals( mtp6, mtp5 );
    }

    @Test
    public void testInequality()
    {
        MultilineTextPayload mtp = new MultilineTextPayload( "Test comment - here we go\nAnd another line" );
        MultilineTextPayload mtp2 = new MultilineTextPayload( "Test comment - here we go And another line" );

        Assert.assertFalse( mtp.equals( mtp2 ) );
        Assert.assertFalse( mtp2.equals( mtp ) );
    }
}
