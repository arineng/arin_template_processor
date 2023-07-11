package net.arin.tp.processor.action;

import net.arin.tp.utils.TemplateProcessorProperties;
import org.apache.commons.lang.StringUtils;
import org.testng.annotations.Test;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.SearchTerm;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProcessorTest extends BaseActionTest
{
    @Test(enabled = false) // TODO: Figure out why failing, and then re-enable.
    public void testExceptionsAreAllCaught() throws Exception
    {
        System.setProperty( TemplateProcessorProperties.PROP_POLLER_FOLDER, "INBOX" );

        Message badMessage1 = getMimeMessage();
        badMessage1.setFrom( new NoCheckInternetAddress( "@tmail.com" ) );
        Message badMessage2 = getMimeMessage();
        badMessage2.setRecipient( Message.RecipientType.TO, new NoCheckInternetAddress( "to@tmail.com^^^" ) );
        Message badMessage3 = getMimeMessage();
        badMessage3.setRecipient( Message.RecipientType.CC, new NoCheckInternetAddress( "cc@tmail.com^^^" ) );
        Message[] messages = new Message[]{
                badMessage1,
                badMessage2,
                badMessage3,
                getMimeMessage()
        };
        Folder mockIndex = mock( Folder.class );
        when( mockIndex.search( any( SearchTerm.class ) ) ).thenReturn( messages );
        Store mockStore = mock( Store.class );
        when( mockStore.getFolder( nullable( String.class ) ) ).thenReturn( mockIndex );
        Session mockSession = mock( Session.class );
        when( mockSession.getStore( nullable( String.class ) ) ).thenReturn( mockStore );

        Processor processor = new Processor();
        processor.mailSession = mockSession;
        processor.initialize();

        processor.trigger();
    }

    private static class NoCheckInternetAddress extends Address
    {
        private String address;

        NoCheckInternetAddress( String address )
        {
            this.address = address;
        }

        @Override
        public String getType()
        {
            return "rfc822";
        }

        @Override
        public String toString()
        {
            return address;
        }

        @Override
        public boolean equals( Object a )
        {
            if ( !( a instanceof NoCheckInternetAddress ) )
            {
                return false;
            }

            String s = ( ( NoCheckInternetAddress ) a ).address;
            return StringUtils.equalsIgnoreCase( s, address );
        }

        public int hashCode()
        {
            return address == null ? 0 : address.toLowerCase().hashCode();
        }
    }
}
