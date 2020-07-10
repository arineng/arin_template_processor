package net.arin.tp.ipaddr;

import java.util.ArrayList;
import java.util.List;

public class CandidateIPAddr implements IsIPVersionAware
{
    private String possibleIPAddress;

    public CandidateIPAddr( CandidateIPAddr possiblyNullValue )
    {
        if ( possiblyNullValue == null )
        {
            possibleIPAddress = null;
        }
        else
        {
            this.possibleIPAddress = possiblyNullValue.possibleIPAddress;
        }
    }

    public CandidateIPAddr( String possibleIPAddress )
    {
        this.possibleIPAddress = possibleIPAddress;
    }

    public IPAddr toIPAddr()
    {
        return new IPAddr( possibleIPAddress );
    }

    public IPAddr toOptionalIPAddr()
    {
        if ( possibleIPAddress == null )
        {
            return null;
        }
        return toIPAddr();
    }

    public boolean isValid()
    {
        return possibleIPAddress != null && IPAddr.ipAddrIfValid( possibleIPAddress ) != null;
    }

    @Override
    public String toString()
    {
        return possibleIPAddress;
    }

    public boolean isV4()
    {
        return isValid() && toIPAddr().isV4();
    }

    public boolean isV6()
    {
        return isValid() && toIPAddr().isV6();
    }

    public IPVersion getVersion()
    {
        if ( isValid() )
        {
            return toIPAddr().getVersion();
        }
        return null;
    }

    public boolean hasAValue()
    {
        return possibleIPAddress != null;
    }

    public static List<IPAddr> convertToIPAddrs( List<CandidateIPAddr> ipAddrs )
    {
        List<IPAddr> list = new ArrayList<>();
        for ( CandidateIPAddr networkStart : ipAddrs )
        {
            list.add( networkStart.toIPAddr() );
        }
        return list;
    }
}
