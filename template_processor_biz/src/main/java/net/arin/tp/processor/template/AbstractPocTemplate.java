package net.arin.tp.processor.template;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractPocTemplate extends TemplateImpl implements PocTemplate
{
    protected String pocHandle;
    protected String type;
    protected String firstName;
    protected String middleName;
    protected String lastName;
    protected String companyName;
    protected List<String> address = new LinkedList<>();
    protected String city;
    protected String stateProvince;
    protected String postalCode;
    protected String countryCode;
    protected List<String> officePhone = new LinkedList<>();
    protected List<String> officePhoneExt = new LinkedList<>();
    protected List<String> email = new LinkedList<>();
    protected List<String> mobilePhone = new LinkedList<>();
    protected List<String> faxPhone = new LinkedList<>();
    protected List<String> pubComment = new LinkedList<>();

    @Override
    public String getPocHandle()
    {
        return pocHandle;
    }

    @Override
    public void setPocHandle( String pocHandle )
    {
        this.pocHandle = pocHandle;
    }

    @Override
    public String getPocContactType()
    {
        return type;
    }

    @Override
    public void setPocContactType( String type )
    {
        this.type = type;
    }

    @Override
    public String getFirstName()
    {
        return firstName;
    }

    @Override
    public void setFirstName( String firstName )
    {
        this.firstName = firstName;
    }

    @Override
    public String getMiddleName()
    {
        return middleName;
    }

    @Override
    public void setMiddleName( String middleName )
    {
        this.middleName = middleName;
    }

    @Override
    public String getLastName()
    {
        return lastName;
    }

    @Override
    public void setLastName( String lastName )
    {
        this.lastName = lastName;
    }

    @Override
    public String getCompanyName()
    {
        return companyName;
    }

    @Override
    public void setCompanyName( String companyName )
    {
        this.companyName = companyName;
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
    public String getStateProvince()
    {
        return stateProvince;
    }

    @Override
    public void setStateProvince( String stateProvince )
    {
        this.stateProvince = stateProvince;
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
    public List<String> getOfficePhone()
    {
        return officePhone;
    }

    @Override
    public void setOfficePhone( List<String> officePhone )
    {
        this.officePhone = officePhone;
    }

    @Override
    public List<String> getOfficePhoneExt()
    {
        return officePhoneExt;
    }

    @Override
    public void setOfficePhoneExt( List<String> officePhoneExt )
    {
        this.officePhoneExt = officePhoneExt;
    }

    @Override
    public List<String> getEmail()
    {
        return email;
    }

    @Override
    public void setEmail( List<String> email )
    {
        this.email = email;
    }

    @Override
    public List<String> getMobilePhone()
    {
        return mobilePhone;
    }

    @Override
    public void setMobilePhone( List<String> mobilePhone )
    {
        this.mobilePhone = mobilePhone;
    }

    @Override
    public List<String> getFaxPhone()
    {
        return faxPhone;
    }

    @Override
    public void setFaxPhone( List<String> faxPhone )
    {
        this.faxPhone = faxPhone;
    }

    @Override
    public List<String> getPublicComments()
    {
        return pubComment;
    }

    @Override
    public void setPublicComments( List<String> pubComment )
    {
        this.pubComment = pubComment;
    }
}
