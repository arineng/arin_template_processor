package net.arin.tp.processor.transform;

import net.arin.tp.api.payload.AttachmentPayload;
import net.arin.tp.api.payload.MessagePayload;
import net.arin.tp.api.payload.NetPayload;
import net.arin.tp.ipaddr.IPAddr;
import net.arin.tp.ipaddr.IPRange;
import net.arin.tp.ipaddr.IPVersion;
import net.arin.tp.processor.template.Attachment;
import net.arin.tp.processor.template.TemplateImpl;
import net.arin.tp.utils.Constants;
import net.arin.tp.api.payload.NetBlockPayload;
import net.arin.tp.processor.exception.TemplateException;
import net.arin.tp.processor.utils.MessageBundle;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Base class for net transformation logic.
 */
public abstract class NetTransformer extends Transformer
{
    private static final Pattern START_AND_CIDR_PATTERN_V4 = Pattern.compile( Constants.SWIP_START_AND_CIDR_V4 );
    private static final Pattern START_AND_END_PATTERN_V4 = Pattern.compile( Constants.SWIP_START_AND_END_V4 );
    private static final Pattern START_AND_CIDR_PATTERN_V6 = Pattern.compile( Constants.SWIP_START_AND_CIDR_V6 );
    private static final Pattern START_AND_END_PATTERN_V6 = Pattern.compile( Constants.SWIP_START_AND_END_V6 );

    static NetBlockPayload ipRangeToNetBlockPayloadNoCidr( IPRange ipRange )
    {
        NetBlockPayload netBlockPayload = new NetBlockPayload();
        netBlockPayload.setStartAddress( ipRange.getStartIPAddress().toShortNotation() );
        netBlockPayload.setEndAddress( ipRange.getEndIPAddress().toShortNotation() );
        return netBlockPayload;
    }

    public static IPRange convertIPAddressFieldToIPRange( String ipAddressField, IPVersion ipVersion )
    {
        if ( ipAddressField == null )
        {
            throw new TemplateException( MessageBundle.IP_ADDRESS_REQUIRED );
        }

        final IPRange ipRange;

        try
        {
            if ( ipVersion.equals( IPVersion.IPV4 ) )
            {
                Matcher startAndCiderMatcherV4 = START_AND_CIDR_PATTERN_V4.matcher( ipAddressField );
                Matcher startAndEndMatcherV4 = START_AND_END_PATTERN_V4.matcher( ipAddressField );

                log.debug( "Attempting IPv4 match for " + ipAddressField );

                if ( startAndCiderMatcherV4.matches() )
                {
                    ipRange = new IPRange( new IPAddr( startAndCiderMatcherV4.group( 1 ) ),
                            Integer.parseInt( startAndCiderMatcherV4.group( 5 ) ) );
                }
                else if ( startAndEndMatcherV4.matches() )
                {
                    ipRange = new IPRange( new IPAddr( startAndEndMatcherV4.group( 1 ) ),
                            new IPAddr( startAndEndMatcherV4.group( 5 ) ) );
                }
                else
                {
                    throw new TemplateException( MessageBundle.IPV4_ADDRESS_FORMAT );
                }
            }
            else
            {
                Matcher startAndCiderMatcherV6 = START_AND_CIDR_PATTERN_V6.matcher( ipAddressField );
                Matcher startAndEndMatcherV6 = START_AND_END_PATTERN_V6.matcher( ipAddressField );

                log.debug( "Attempting IPv6 match for " + ipAddressField );

                if ( startAndCiderMatcherV6.matches() )
                {
                    ipRange = new IPRange( new IPAddr( startAndCiderMatcherV6.group( 1 ) ),
                            Integer.parseInt( startAndCiderMatcherV6.group( 2 ) ) );
                }
                else if ( startAndEndMatcherV6.matches() )
                {
                    ipRange = new IPRange( new IPAddr( startAndEndMatcherV6.group( 1 ) ),
                            new IPAddr( startAndEndMatcherV6.group( 2 ) ) );
                }
                else
                {
                    throw new TemplateException( MessageBundle.IPV6_ADDRESS_FORMAT );
                }
            }

            log.debug( "start_ip=" + ipRange.getStartIPAddress() + " cidr=" + ipRange.getCidrs().get( 0 ) );

            return ipRange;
        }
        catch ( NumberFormatException e )
        {
            log.error( "Bad regex pattern matching", e );
            throw e;
        }
        catch ( IllegalArgumentException e )
        {
            // The IllegalArgumentException probably came from the IPAddr constructor. If so, it should provide a
            // friendly message.
            throw new TemplateException( e.getMessage() );
        }
    }

    static void setAttachmentsAndAdditionalInfo( NetPayload payload, TemplateImpl template )
    {
        List<Attachment> attachments = template.getAttachments();
        if ( attachments.size() > 0 )
        {
            MessagePayload messagePayload = MessagePayload.createAdditionalInformation( StringUtils.EMPTY );
            messagePayload.setCategory( MessagePayload.Category.JUSTIFICATION );

            int unnamed = 0;
            for ( Attachment attachment : attachments )
            {
                AttachmentPayload attachmentPayload = new AttachmentPayload();
                if ( StringUtils.isEmpty( attachment.getFilename() ) )
                {
                    unnamed++;
                    attachmentPayload.setFilename( "unnamed" + unnamed + ".txt" );
                }
                else
                {
                    attachmentPayload.setFilename( attachment.getFilename() );
                }
                attachmentPayload.setData( attachment.getData() );
                messagePayload.addAttachment( attachmentPayload );
            }

            if ( payload.getMessages() == null )
            {
                payload.setMessages( new ArrayList<>() );
            }
            payload.getMessages().add( messagePayload );
        }
    }
}
