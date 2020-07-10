package net.arin.tp.utils;

import net.arin.tp.ipaddr.IPAddr;

public class Constants
{
    // Used by SwipTransformer.
    public static final String IPV6_REGEX_SHORT_FORM = "(?:[0-9a-fA-F]{1,4}:){1,7}[:]";
    public static final String SWIP_START_AND_CIDR_V4 = "(" + IPAddr.IPV4_REGEX + ")" + "\\s*/\\s*([0-9]+)";
    public static final String SWIP_START_AND_CIDR_V6 = "(" + IPAddr.IPV6_REGEX + "|" + IPV6_REGEX_SHORT_FORM + ")" + "\\s*/\\s*([0-9]+)";
    public static final String SWIP_START_AND_END_V4 = "(" + IPAddr.IPV4_REGEX + ")" + "\\s*-\\s*" + "(" + IPAddr.IPV4_REGEX + ")";
    public static final String SWIP_START_AND_END_V6 = "(" + IPAddr.IPV6_REGEX + "|" + IPV6_REGEX_SHORT_FORM + ")" + "\\s*-\\s*" + "(" + IPAddr.IPV6_REGEX + "|" + IPV6_REGEX_SHORT_FORM + ")";

    // Used by TemplateImpl.
    public static final String API_KEY = ".*(API-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}).*";

    // Mock queues for tests.
    public static final String ROUTER_QUEUE = "queue/regcoreRouterQueue";
    public static final String TEMPLATE_QUEUE = "queue/regcoreTemplateQueue";
}
