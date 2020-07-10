package net.arin.tp.processor.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Convenience class to print date objects in ARIN specific date-time format.
 */
public class DateTimeFormatter
{
    private static final String ARIN_DATE_TIME_FORMAT = "MM-dd-yyyy HH:mm:ss";
    private static final String WHOIS_DATE_FORMAT = "yyyy-MM-dd";
    private static final String TICKET_DATE_TIME_FORMAT = "yyyyMMddHHmmss";
    private static final String TICKET_DATE_FORMAT = "yyyyMMdd";
    private static final String EMPTY = "";

    private static final SimpleDateFormat arinDateTimeFormat = new SimpleDateFormat( ARIN_DATE_TIME_FORMAT );
    private static final SimpleDateFormat whoisDateFormat = new SimpleDateFormat( WHOIS_DATE_FORMAT );
    private static final SimpleDateFormat ticketDateTimeFormat = new SimpleDateFormat( TICKET_DATE_TIME_FORMAT );
    private static final SimpleDateFormat ticketDateFormat = new SimpleDateFormat( TICKET_DATE_FORMAT );

    public static String getCurrent()
    {
        return getFormattedDate( new Date() );
    }

    public static String getFormattedDate( Date date )
    {
        return date != null ? arinDateTimeFormat.format( date ) : EMPTY;
    }

    public static String getFormattedDate( Calendar cal )
    {
        return cal != null ? arinDateTimeFormat.format( cal.getTime() ) : EMPTY;
    }

    public static String getWhoisDate( Date date )
    {
        return date != null ? whoisDateFormat.format( date ) : EMPTY;
    }

    public static String getWhoisDate( Calendar cal )
    {
        return cal != null ? whoisDateFormat.format( cal.getTime() ) : EMPTY;
    }

    public static String getTicketDateTimeFormat( Date date )
    {
        return date != null ? ticketDateTimeFormat.format( date ) : EMPTY;
    }

    public static SimpleDateFormat getTicketDateTimeFormat()
    {
        return ticketDateTimeFormat;
    }

    public static String getTicketDateFormat( Date date )
    {
        return date != null ? ticketDateFormat.format( date ) : EMPTY;
    }
}
