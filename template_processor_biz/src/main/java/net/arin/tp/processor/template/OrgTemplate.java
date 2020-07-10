package net.arin.tp.processor.template;

import java.io.Serializable;
import java.util.List;

public interface OrgTemplate extends TemplateWithPublicComments, Serializable
{
    String getOrgHandle();

    void setOrgHandle( String orgHandle );

    String getLegalName();

    void setLegalName( String legalName );

    String getDba();

    void setDba( String dba );

    String getTaxId();

    void setTaxId( String taxId );

    List<String> getAddress();

    void setAddress( List<String> address );

    String getCity();

    void setCity( String city );

    String getState();

    void setState( String state );

    String getPostalCode();

    void setPostalCode( String postalCode );

    String getCountryCode();

    void setCountryCode( String countryCode );

    String getAdminPocHandle();

    void setAdminPocHandle( String adminPocHandle );

    List<String> getTechPocHandles();

    void setTechPocHandles( List<String> techPocHandles );

    List<String> getAbusePocHandles();

    void setAbusePocHandles( List<String> abusePocHandles );

    List<String> getNocPocHandles();

    void setNocPocHandles( List<String> nocPocHandles );

    String getReferralServer();

    void setReferralServer( String referralServer );

    List<String> getPublicComments();

    void setPublicComments( List<String> publicComments );
}
