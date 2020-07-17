package net.arin.tp.api.payload;

/**
 * Interface defining methods related to the public comments on the payload.
 */
public interface PayloadWithPublicComments extends Payload
{
    String FIELD_PUBLICCOMMENTS = "comment";

    MultilineTextPayload getMultilinePublicComments();

    void setMultilinePublicComments( MultilineTextPayload pubComment );

    String getPublicComments();

    void setPublicComments( String pubComment );
}
