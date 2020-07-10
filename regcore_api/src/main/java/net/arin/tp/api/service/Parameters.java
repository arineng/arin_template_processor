package net.arin.tp.api.service;

/**
 * Used to define the web service request parameters so that we're consistent across the entire set of services. Not all
 * request parameters need to be defined here, only core ones that will be shared across all calls.
 */
public class Parameters
{
    static final public String API_KEY = "apikey";

    static final public String POC_HANDLE = "pocHandle";

    static final public String ORG_HANDLE = "orgHandle";

    static final public String NET_HANDLE = "netHandle";

    static final public String PARENT_NET_HANDLE = "parentNetHandle";

    static final public String EMAIL = "email";

    static final public String MAKE_LINK = "makeLink";

    static final public String POC_FUNCTION = "pocFunction";

    static final public String CUSTOMER_HANDLE = "customerHandle";

    static final public String START_IP = "startIp";

    static final public String END_IP = "endIp";
}
