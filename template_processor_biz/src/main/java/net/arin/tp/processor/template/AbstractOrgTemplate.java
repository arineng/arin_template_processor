package net.arin.tp.processor.template;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractOrgTemplate extends TemplateImpl implements OrgTemplate
{
    protected String orgHandle;
    protected String legalName;
    protected String dba;
    protected String taxId;
    protected List<String> address = new LinkedList<>();
    protected String city;
    protected String state;
    protected String postalCode;
    protected String countryCode;
    protected String adminPocHandle;
    protected List<String> techPocHandles = new LinkedList<>();
    protected List<String> abusePocHandles = new LinkedList<>();
    protected List<String> nocPocHandles = new LinkedList<>();
    protected String referralServer;
    protected List<String> publicComments = new LinkedList<>();

    @Override
    public String getOrgHandle()
    {
        return orgHandle;
    }

    @Override
    public void setOrgHandle( String orgHandle )
    {
        this.orgHandle = orgHandle;
    }

    @Override
    public String getLegalName()
    {
        return legalName;
    }

    @Override
    public void setLegalName( String legalName )
    {
        this.legalName = legalName;
    }

    @Override
    public String getDba()
    {
        return dba;
    }

    @Override
    public void setDba( String dba )
    {
        this.dba = dba;
    }

    @Override
    public String getTaxId()
    {
        return taxId;
    }

    @Override
    public void setTaxId( String taxId )
    {
        this.taxId = taxId;
    }

    @Override
    public List<String> getAddress()
    {
        return address;
    }

    @Override
    public void setAddress( List<String> address )
    {
        this.address = address;
    }

    @Override
    public String getCity()
    {
        return city;
    }

    @Override
    public void setCity( String city )
    {
        this.city = city;
    }

    @Override
    public String getState()
    {
        return state;
    }

    @Override
    public void setState( String state )
    {
        this.state = state;
    }

    @Override
    public String getPostalCode()
    {
        return postalCode;
    }

    @Override
    public void setPostalCode( String postalCode )
    {
        this.postalCode = postalCode;
    }

    @Override
    public String getCountryCode()
    {
        return countryCode;
    }

    @Override
    public void setCountryCode( String countryCode )
    {
        this.countryCode = countryCode;
    }

    @Override
    public String getAdminPocHandle()
    {
        return adminPocHandle;
    }

    @Override
    public void setAdminPocHandle( String adminPocHandle )
    {
        this.adminPocHandle = adminPocHandle;
    }

    @Override
    public List<String> getTechPocHandles()
    {
        return techPocHandles;
    }

    @Override
    public void setTechPocHandles( List<String> techPocHandles )
    {
        this.techPocHandles = techPocHandles;
    }

    @Override
    public List<String> getAbusePocHandles()
    {
        return abusePocHandles;
    }

    @Override
    public void setAbusePocHandles( List<String> abusePocHandles )
    {
        this.abusePocHandles = abusePocHandles;
    }

    @Override
    public List<String> getNocPocHandles()
    {
        return nocPocHandles;
    }

    @Override
    public void setNocPocHandles( List<String> nocPocHandles )
    {
        this.nocPocHandles = nocPocHandles;
    }

    @Override
    public String getReferralServer()
    {
        return referralServer;
    }

    @Override
    public void setReferralServer( String referralServer )
    {
        this.referralServer = referralServer;
    }

    @Override
    public List<String> getPublicComments()
    {
        return publicComments;
    }

    @Override
    public void setPublicComments( List<String> publicComments )
    {
        this.publicComments = publicComments;
    }
}
