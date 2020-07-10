package net.arin.tp.processor.action;

import net.arin.tp.processor.BaseTest;

import javax.mail.internet.MimeMessage;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BaseActionTest extends BaseTest
{
    MimeMessage getMimeMessage()
    {
        Map<String, String> headers = new HashMap<>();

        headers.put( "x-header-test", "testing" );

        return getMimeMessage( "john@example.com", "jack@example.com", "jill@example.com",
                "a random subject", "main message body", Collections.singletonList( "an attachment" ),
                headers );
    }
}
