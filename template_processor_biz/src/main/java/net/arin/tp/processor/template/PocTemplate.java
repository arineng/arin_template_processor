package net.arin.tp.processor.template;

import java.io.Serializable;
import java.util.List;

public interface PocTemplate extends TemplateWithPublicComments, Serializable
{
    String getPocHandle();

    void setPocHandle( String pocHandle );

    String getPocContactType();

    void setPocContactType( String type );

    String getFirstName();

    void setFirstName( String firstName );

    String getMiddleName();

    void setMiddleName( String middleName );

    String getLastName();

    void setLastName( String lastName );

    String getCompanyName();

    void setCompanyName( String companyName );

    List<String> getAddress();

    void setAddress( List<String> address );

    String getCity();

    void setCity( String city );

    String getStateProvince();

    void setStateProvince( String stateProvince );

    String getPostalCode();

    void setPostalCode( String postalCode );

    String getCountryCode();

    void setCountryCode( String countryCode );

    List<String> getOfficePhone();

    void setOfficePhone( List<String> officePhone );

    List<String> getOfficePhoneExt();

    void setOfficePhoneExt( List<String> officePhoneExt );

    List<String> getEmail();

    void setEmail( List<String> email );

    List<String> getMobilePhone();

    void setMobilePhone( List<String> mobilePhone );

    List<String> getFaxPhone();

    void setFaxPhone( List<String> faxPhone );
}
