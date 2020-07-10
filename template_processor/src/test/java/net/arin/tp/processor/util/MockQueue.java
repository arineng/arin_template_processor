package net.arin.tp.processor.util;

import javax.jms.Message;
import javax.jms.Queue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockQueue implements Queue
{
    private static Map<String, MockQueue> m = new HashMap<>();
    private static String lastCall;
    private String name;
    private List<Message> messages;

    private MockQueue( String name )
    {
        this.name = name;
        messages = new ArrayList<>();
    }

    public static MockQueue getInstance( String name )
    {
        if ( !m.containsKey( name ) )
        {
            m.put( name, new MockQueue( name ) );
        }
        lastCall = name;
        return m.get( name );
    }

    @Override
    public String getQueueName()
    {
        return name;
    }

    private List<Message> getMessages()
    {
        return messages;
    }

    public static String getLastCall()
    {
        return lastCall;
    }

    public static void reset()
    {
        lastCall = null;
        for ( String s : m.keySet() )
        {
            m.get( s ).getMessages().clear();
        }
    }
}
