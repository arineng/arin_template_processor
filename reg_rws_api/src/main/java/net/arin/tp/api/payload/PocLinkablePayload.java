package net.arin.tp.api.payload;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Base class used to provide POC-related functionality to payload classes (currently OrgPayload and NetPayload).
 */
@XmlAccessorType( XmlAccessType.NONE )
public abstract class PocLinkablePayload
{
    static final public String FIELD_POCLINKS = "pocLinks";
    static final public String FIELD_POCLINK = "pocLinkRef";

    protected List<PocLinkPayload> pocLinks = new ArrayList<>( 0 );

    @XmlElement( name = FIELD_POCLINK, type = PocLinkPayload.class )
    @XmlElementWrapper( name = FIELD_POCLINKS )
    public List<PocLinkPayload> getPocLinks()
    {
        return pocLinks;
    }

    public Set<String> getTechPocs()
    {
        return getPocHandlesForFunction( PocLinkPayload.Function.T );
    }

    public Set<String> getAbusePocs()
    {
        return getPocHandlesForFunction( PocLinkPayload.Function.AB );
    }

    public Set<String> getNocPocs()
    {
        return getPocHandlesForFunction( PocLinkPayload.Function.N );
    }

    public Set<String> getRoutingPocs()
    {
        return getPocHandlesForFunction( PocLinkPayload.Function.R );
    }

    public Set<String> getDnsPocs()
    {
        return getPocHandlesForFunction( PocLinkPayload.Function.D );
    }

    /**
     * Replace current Tech POCs with those specified by pocHandles.
     */
    public void setTechPocs( Set<String> pocHandles )
    {
        clearPocLinksForFunction( PocLinkPayload.Function.T );
        addPocLinksForFunction( pocHandles, PocLinkPayload.Function.T );
    }

    /**
     * Replace current Abuse POCs with those specified by pocHandles.
     */
    public void setAbusePocs( Set<String> pocHandles )
    {
        clearPocLinksForFunction( PocLinkPayload.Function.AB );
        addPocLinksForFunction( pocHandles, PocLinkPayload.Function.AB );
    }

    /**
     * Replace current NOC POCs with those specified by pocHandles.
     */
    public void setNocPocs( Set<String> pocHandles )
    {
        clearPocLinksForFunction( PocLinkPayload.Function.N );
        addPocLinksForFunction( pocHandles, PocLinkPayload.Function.N );
    }

    /**
     * Replace current Routing POCs with those specified by pocHandles.
     */
    public void setRoutingPocs( Set<String> pocHandles )
    {
        clearPocLinksForFunction( PocLinkPayload.Function.R );
        addPocLinksForFunction( pocHandles, PocLinkPayload.Function.R );
    }

    /**
     * Replace current DNS POCs with those specified by pocHandles.
     */
    public void setDnsPocs( Set<String> pocHandles )
    {
        clearPocLinksForFunction( PocLinkPayload.Function.D );
        addPocLinksForFunction( pocHandles, PocLinkPayload.Function.D );
    }

    private void addPocLinksForFunction( Set<String> pocHandles, PocLinkPayload.Function function )
    {
        for ( String pocHandle : pocHandles )
        {
            getPocLinks().add( new PocLinkPayload( pocHandle, function ) );
        }
    }

    /**
     * Add a Tech POC. Silently ignore duplicates.
     */
    public void addTechPoc( String pocHandle )
    {
        getPocLinks().add( new PocLinkPayload( pocHandle, PocLinkPayload.Function.T ) );
    }

    /**
     * Add an Abuse POC. Silently ignore duplicates.
     */
    public void addAbusePoc( String pocHandle )
    {
        getPocLinks().add( new PocLinkPayload( pocHandle, PocLinkPayload.Function.AB ) );
    }

    /**
     * Add a NOC POC. Silently ignore duplicates.
     */
    public void addNocPoc( String pocHandle )
    {
        getPocLinks().add( new PocLinkPayload( pocHandle, PocLinkPayload.Function.N ) );
    }

    /**
     * Add a Routing POC. Silently ignore duplicates.
     */
    public void addRoutingPoc( String pocHandle )
    {
        getPocLinks().add( new PocLinkPayload( pocHandle, PocLinkPayload.Function.R ) );
    }

    /**
     * Add a DNS POC. Silently ignore duplicates.
     */
    public void addDnsPoc( String pocHandle )
    {
        getPocLinks().add( new PocLinkPayload( pocHandle, PocLinkPayload.Function.D ) );
    }

    public Set<PocLinkPayload> getPocLinksForFunction( PocLinkPayload.Function function )
    {
        Set<PocLinkPayload> linksToReturn = new HashSet<>();
        for ( PocLinkPayload px : pocLinks )
        {
            if ( px.getFunction().equals( function ) || function == null )
            {
                linksToReturn.add( px );
            }
        }

        return linksToReturn;
    }

    public List<String> getPocHandles()
    {
        Set<String> handles = getPocHandlesForFunction( null );
        List<String> toReturn = new ArrayList<>();
        for ( String handle : handles )
        {
            toReturn.add( handle );
        }
        return toReturn;
    }

    protected Set<String> getPocHandlesForFunction( PocLinkPayload.Function function )
    {
        Set<String> toReturn = new HashSet<>();

        Set<PocLinkPayload> links = getPocLinksForFunction( function );
        for ( PocLinkPayload plx : links )
        {
            toReturn.add( plx.getHandle() );
        }

        return toReturn;
    }

    public void clearPocLinksForFunction( PocLinkPayload.Function function )
    {
        PocLinkPayload plx;
        for ( Iterator<PocLinkPayload> it = this.pocLinks.iterator(); it.hasNext(); )
        {
            plx = it.next();
            if ( plx.getFunction().equals( function ) )
            {
                it.remove();
            }
        }
    }

    public void removePocLink( String pocHandle, PocLinkPayload.Function function )
    {
        PocLinkPayload plx;
        for ( Iterator<PocLinkPayload> it = this.pocLinks.iterator(); it.hasNext(); )
        {
            plx = it.next();
            if ( plx.getFunction().equals( function ) && plx.getHandle().equals( pocHandle ) )
            {
                it.remove();
            }
        }
    }
}
