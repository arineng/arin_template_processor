package net.arin.tp.api.payload;

/**
 * This indicates that a payload contains a country.
 */
public interface PayloadWithCountry extends Payload
{
    String FIELD_COUNTRY = "iso3166-1";

    CountryPayload getCountry();

    void setCountry( CountryPayload country );
}
