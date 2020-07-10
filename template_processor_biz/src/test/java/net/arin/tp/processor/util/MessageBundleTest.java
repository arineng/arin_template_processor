package net.arin.tp.processor.util;

import net.arin.tp.processor.utils.MessageBundle;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageBundleTest
{
    private static Pattern PATTERN_MESSAGE = Pattern.compile( "(.*)=(.*)" );

    @Test
    public void verifyMessagesPropertiesFile() throws Exception
    {
        String path = "src/main/resources/template_validation_messages.properties";

        File file = new File( path );

        Assert.assertTrue( file.exists(), "Could not find template_validation_messages.properties file to verify it" );

        FileReader fileReader = new FileReader( file );
        BufferedReader bufferedReader = new BufferedReader( fileReader );

        while ( bufferedReader.ready() )
        {
            String line = bufferedReader.readLine();
            Matcher matcher = PATTERN_MESSAGE.matcher( line );

            if ( matcher.find() )
            {
                String key = matcher.group( 1 );
                MessageBundle value = null;

                try
                {
                    value = MessageBundle.valueOf( key );
                }
                catch ( Exception ex )
                {
                    // Ignore.
                }

                // If you're looking at this assertion, you probably need to go to the MessageBundle enum and add the
                // property you just added to the template_validation_messages.properties file.
                Assert.assertNotNull( value, "Unable to find MessageBundle definition for message property [" + key + "]" );
            }
            else
            {
                Assert.fail( "Garbage text found in template_validation_messages.properties file: " + line );
            }
        }
    }

    @Test
    public void verifyMessageBundleEnum() throws Exception
    {
        String path = "src/main/resources/template_validation_messages.properties";

        File file = new File( path );

        Assert.assertTrue( file.exists(), "Could not find template_validation_messages.properties file to verify it" );

        FileReader fileReader = new FileReader( file );
        BufferedReader bufferedReader = new BufferedReader( fileReader );

        StringBuilder builder = new StringBuilder();

        while ( bufferedReader.ready() )
        {
            builder.append( bufferedReader.readLine() );
            builder.append( "\n" );
        }

        for ( MessageBundle message : MessageBundle.values() )
        {
            Pattern pattern = Pattern.compile( "^" + message.name() + "=.*$", Pattern.MULTILINE );
            Matcher matcher = pattern.matcher( builder.toString() );

            // If this failed it means you have an old value in the MessageBundle enum. When you remove properties from
            // the template_validation_messages.properties file you must remove them from the MessageBundle enum too.
            Assert.assertTrue( matcher.find(), "An enum [" + message.name()
                    + "] was found in MessageBundle that was not defined in the template_validation_messages.properties file" );
        }
    }
}
