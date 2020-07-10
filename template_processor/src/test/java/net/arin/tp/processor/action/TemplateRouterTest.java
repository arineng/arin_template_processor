package net.arin.tp.processor.action;

import net.arin.tp.processor.action.converter.MailToTemplate;
import net.arin.tp.processor.action.converter.MessageConversionException;
import net.arin.tp.processor.message.MailMessage;
import net.arin.tp.processor.message.TemplateMessage;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TemplateRouterTest extends BaseActionTest
{
    @Test
    public void testSimpleConversion() throws Exception
    {
        MailToTemplate mailToTemplate = new MailToTemplate();

        String originalTemplateText = getPocTemplate();

        MimeMessage mimeMessage = getMimeMessage( "john@example.com",
                "jack@example.com",
                null,
                "a subject",
                originalTemplateText,
                new ArrayList<>(),
                new HashMap<>() );

        MailMessage mailMessage = new MailMessage( mimeMessage );
        List<TemplateMessage> messages = mailToTemplate.convert( mailMessage );

        Assert.assertNotNull( messages, "No messages came back" );
        Assert.assertEquals( messages.size(), 1, "Incorrect number of messages were received" );

        TemplateMessage templateMessage = messages.get( 0 );

        Assert.assertEquals( templateMessage.getApiKey(),
                "API-0000-0000-0000-0000",
                "The API key does not match" );

        Assert.assertEquals( templateMessage.getText(),
                originalTemplateText,
                "The template text does not match" );

        Assert.assertEquals( templateMessage.getFrom().size(), 1, "Incorrect number of 'from'" );
        Assert.assertEquals( templateMessage.getFrom().get( 0 ), "john@example.com",
                "The template 'from' does not match" );

        Assert.assertEquals( templateMessage.getTo().size(), 1, "Incorrect number of 'to'" );
        Assert.assertEquals( templateMessage.getTo().get( 0 ), "jack@example.com",
                "The template 'to' does not match" );

        Assert.assertEquals( templateMessage.getSubject(), "a subject",
                "The template 'subject' does not match" );

        Assert.assertEquals( templateMessage.getTemplate().getAttachments().size(), 0,
                "Incorrect number of attachments" );

        Assert.assertEquals( templateMessage.getTemplate().getTemplateName(), "ARIN-POC",
                "Incorrect template name" );
        Assert.assertEquals( templateMessage.getTemplate().getTemplateVersion(), "5.0",
                "Incorrect template version" );
    }

    @Test
    public void testMultipleTemplatesInBody() throws Exception
    {
        MailToTemplate mailToTemplate = new MailToTemplate();

        String originalTemplateText = getPocTemplate() + "\n\n" + getPocTemplate();

        MimeMessage mimeMessage = getMimeMessage( "john@example.com",
                "jack@example.com",
                null,
                "a subject",
                originalTemplateText,
                new ArrayList<>(),
                new HashMap<>() );

        MailMessage mailMessage = new MailMessage( mimeMessage );

        List<TemplateMessage> messages = mailToTemplate.convert( mailMessage );

        Assert.assertNotNull( messages, "No messages came back" );
        Assert.assertEquals( messages.size(), 2, "Incorrect number of messages were received" );

        TemplateMessage templateMessage = messages.get( 0 );

        Assert.assertEquals( templateMessage.getApiKey(),
                "API-0000-0000-0000-0000",
                "The API key does not match" );

        Assert.assertEquals( templateMessage.getText(),
                getPocTemplate(),
                "The template text does not match" );

        Assert.assertEquals( templateMessage.getFrom().size(), 1, "Incorrect number of 'from'" );
        Assert.assertEquals( templateMessage.getFrom().get( 0 ), "john@example.com",
                "The template 'from' does not match" );

        Assert.assertEquals( templateMessage.getTo().size(), 1, "Incorrect number of 'to'" );
        Assert.assertEquals( templateMessage.getTo().get( 0 ), "jack@example.com",
                "The template 'to' does not match" );

        Assert.assertEquals( templateMessage.getSubject(), "a subject",
                "The template 'subject' does not match" );

        Assert.assertEquals( templateMessage.getTemplate().getAttachments().size(), 0,
                "Incorrect number of attachments" );

        Assert.assertEquals( templateMessage.getTemplate().getTemplateName(), "ARIN-POC",
                "Incorrect template name" );
        Assert.assertEquals( templateMessage.getTemplate().getTemplateVersion(), "5.0",
                "Incorrect template version" );
    }

    @Test
    public void testMultipleTemplatesAsAttachments() throws Exception
    {
        MailToTemplate mailToTemplate = new MailToTemplate();

        String originalTemplateText = getPocTemplate() + "\n\n" + getPocTemplate();

        MimeMessage mimeMessage = getMimeMessage( "john@example.com",
                "jack@example.com",
                null,
                "a subject",
                originalTemplateText,
                Arrays.asList( originalTemplateText, getPocTemplate() ),
                new HashMap<>() );

        MailMessage mailMessage = new MailMessage( mimeMessage );

        List<TemplateMessage> messages = mailToTemplate.convert( mailMessage );

        Assert.assertNotNull( messages, "No messages came back" );
        Assert.assertEquals( messages.size(), 5, "Incorrect number of messages were received" );

        TemplateMessage templateMessage = messages.get( 0 );

        Assert.assertEquals( templateMessage.getApiKey(),
                "API-0000-0000-0000-0000",
                "The API key does not match" );

        Assert.assertEquals( templateMessage.getText(),
                getPocTemplate(),
                "The template text does not match" );

        Assert.assertEquals( templateMessage.getFrom().size(), 1, "Incorrect number of 'from'" );
        Assert.assertEquals( templateMessage.getFrom().get( 0 ), "john@example.com",
                "The template 'from' does not match" );

        Assert.assertEquals( templateMessage.getTo().size(), 1, "Incorrect number of 'to'" );
        Assert.assertEquals( templateMessage.getTo().get( 0 ), "jack@example.com",
                "The template 'to' does not match" );

        Assert.assertEquals( templateMessage.getSubject(), "a subject",
                "The template 'subject' does not match" );

        Assert.assertEquals( templateMessage.getTemplate().getAttachments().size(), 0,
                "Incorrect number of attachments" );

        Assert.assertEquals( templateMessage.getTemplate().getTemplateName(), "ARIN-POC",
                "Incorrect template name" );
        Assert.assertEquals( templateMessage.getTemplate().getTemplateVersion(), "5.0",
                "Incorrect template version" );
    }

    @Test
    public void testAttachmentOnMultipleTemplate()
    {
        MailToTemplate mailToTemplate = new MailToTemplate();

        String originalTemplateText = getPocTemplate();

        MimeMessage mimeMessage = getMimeMessage( "john@example.com",
                "jack@example.com",
                null,
                "a subject",
                originalTemplateText,
                Arrays.asList( getPocTemplate(), "This is an attachment that shouldn't be here." ),
                new HashMap<>() );

        MailMessage mailMessage = new MailMessage( mimeMessage );

        try
        {
            mailToTemplate.convert( mailMessage );

            Assert.fail( "The expected exception was never thrown." );
        }
        catch ( MessageConversionException ex )
        {
            Assert.assertEquals( ex.getMessage(), "Attachments are only allowed when you submit a single template." );
        }
        catch ( Exception ex )
        {
            Assert.fail( "Unexpected exception was thrown", ex );
        }
    }

    @Test
    public void testMultipleDifferentKeysOnTemplate()
    {
        MailToTemplate mailToTemplate = new MailToTemplate();

        String originalTemplateText = getPocTemplate();

        MimeMessage mimeMessage = getMimeMessage( "john@example.com",
                "jack@example.com",
                null,
                "API-0000-0000-0000-0001",
                originalTemplateText,
                new ArrayList<>(),
                new HashMap<>() );

        MailMessage mailMessage = new MailMessage( mimeMessage );

        try
        {
            mailToTemplate.convert( mailMessage );

            Assert.fail( "The expected exception was never thrown." );
        }
        catch ( MessageConversionException ex )
        {
            Assert.assertEquals( ex.getMessage(), "Multiple API keys found." );
        }
        catch ( Exception ex )
        {
            Assert.fail( "Unexpected exception was thrown", ex );
        }
    }

    @Test
    public void testMultipleDuplicateKeysOnTemplate()
    {
        MailToTemplate mailToTemplate = new MailToTemplate();

        String originalTemplateText = getPocTemplate();

        MimeMessage mimeMessage = getMimeMessage( "john@example.com",
                "jack@example.com",
                null,
                "API-0000-0000-0000-1111",
                originalTemplateText,
                new ArrayList<>(),
                new HashMap<>() );

        MailMessage mailMessage = new MailMessage( mimeMessage );

        try
        {
            mailToTemplate.convert( mailMessage );

            Assert.fail( "The expected exception was never thrown." );
        }
        catch ( MessageConversionException ex )
        {
            Assert.assertEquals( ex.getMessage(), "Multiple API keys found." );
        }
        catch ( Exception ex )
        {
            Assert.fail( "Unexpected exception was thrown", ex );
        }
    }

    private String getPocTemplate()
    {
        return "Template: ARIN-POC-5.0\n" +
                "**  As of July 2006\n" +
                "**  Detailed instructions are located below the template.\n" +
                "00. API Key: API-0000-0000-0000-0000\n" +
                "\n" +
                "01. Registration Action (N,M, or R): N\n" +
                "\n" +
                "02. Existing POC Handle:\n" +
                "** Skip field 02. if generating a new handle\n" +
                "\n" +
                "03. Contact Type (P or R): P\n" +
                "04. Last Name or Role Account: Last\n" +
                "05. First Name: First\n" +
                "06. Middle Name: Middle\n" +
                "07. Company Name: TestData\n" +
                "08. Address: 567 Fun Parkway\n" +
                "09. City: Town\n" +
                "10. State/Province: VA\n" +
                "11. Postal Code: 20151\n" +
                "12. Country Code: US\n" +
                "13. Office Phone Number: +1-555-666-9999\n" +
                "14. Office Phone Number Extension: \n" +
                "15. E-mail Address: first@last.com\n" +
                "\n" +
                "** OPTIONAL PHONE NUMBERS\n" +
                "16. Mobile: +1-333-444-5555\n" +
                "17. Fax: \n" +
                "\n" +
                "** OTHER OPTIONAL FIELDS\n" +
                "18. Public Comments: Email me! I love spam!\n" +
                "19. Additional Information:\n" +
                "\n" +
                "END OF TEMPLATE";
    }
}
