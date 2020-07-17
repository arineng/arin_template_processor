package net.arin.tp.api.payload;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * The ErrorPayload is returned when any service call encounters errors and returns the reason for the error.
 */
@XmlRootElement( name = "error" )
@XmlAccessorType( XmlAccessType.NONE )
public class ErrorPayload implements Payload
{
    private Code code;
    private String message;
    private List<ComponentErrorPayload> componentErrors;
    private List<String> additionalMessages;

    public enum Code
    {
        /**
         * Denotes that the XML data sent to us didn't pass the RelaxNG schema validation. Please see the schema and try
         * validating your XML content against it prior to sending it to us.
         */
        E_SCHEMA_VALIDATION,

        /**
         * This code represents that the object failed to pass some of our business rule validation. It either was
         * missing fields, had invalid characters, etc.
         */
        E_ENTITY_VALIDATION,

        /**
         * The object you're trying to work with, probably denoted by a handle, was not found in our database.
         */
        E_OBJECT_NOT_FOUND,

        /**
         * The API key you're using couldn't be found, wasn't associated with the object or didn't have the proper
         * permissions.
         */
        E_AUTHENTICATION,

        /**
         * The action taken did not succeed because it's still linked to or associated to other objects in our database.
         * Remove those links/associations first and then try again.
         */
        E_NOT_REMOVEABLE,

        /**
         * The request you made was invalid. A lot of different things can cause this error, the most common reasons
         * are: a bad URL, invalid parameter types, invalid parameters or your MIME type wasn't properly set to
         * application/xml. The source of your error will likely be an error in your client implementation and the
         * error message will provide further details for the fix.
         */
        E_BAD_REQUEST,

        /**
         * The server is currently undergoing maintenance and is not available.
         */
        E_OUTAGE,

        /**
         * Used for throttling
         */
        E_TOO_MANY_REQUESTS,

        /**
         * A catch-all error code for unspecified errors. The use of this error code should be limited as it provides
         * very little useful information back to the customer.
         */
        E_UNSPECIFIED
    }

    public ErrorPayload()
    {
        componentErrors = new ArrayList<>();
        additionalMessages = new ArrayList<>();
    }

    public ErrorPayload( String message )
    {
        this();

        this.message = message;
    }

    @XmlElement( name = "code" )
    public Code getCode()
    {
        return code;
    }

    public void setCode( Code code )
    {
        this.code = code;
    }

    /**
     * A textual description of the error that occurred.
     */
    @XmlElement( name = "message" )
    public String getMessage()
    {
        return message;
    }

    public void setMessage( String message )
    {
        this.message = message;
    }

    @XmlElementWrapper( name = "components" )
    @XmlElement( name = "component" )
    public List<ComponentErrorPayload> getComponentErrors()
    {
        return componentErrors;
    }

    public void setComponentErrors( List<ComponentErrorPayload> componentErrors )
    {
        this.componentErrors = componentErrors;
    }

    public void addComponentError( String component, String message )
    {
        ComponentErrorPayload componentErrorPayload = new ComponentErrorPayload();

        componentErrorPayload.setName( component );
        componentErrorPayload.setMessage( message );

        getComponentErrors().add( componentErrorPayload );
    }

    @XmlElementWrapper( name = "additionalInfo" )
    @XmlElement( name = "message" )
    public List<String> getAdditionalMessages()
    {
        return this.additionalMessages;
    }

    public void addAdditionalMessage( String message )
    {
        this.additionalMessages.add( message );
    }

    /**
     * These represent individual component errors in the ErrorPayload.
     */
    @XmlRootElement( name = "component" )
    @XmlAccessorType( XmlAccessType.NONE )
    public static class ComponentErrorPayload implements Payload
    {
        private String name;
        private String message;

        public ComponentErrorPayload()
        {
        }

        public ComponentErrorPayload( String component, String message )
        {
            this.name = component;
            this.message = message;
        }

        @XmlElement( name = "name" )
        public String getName()
        {
            return name;
        }

        public void setName( String name )
        {
            this.name = name;
        }

        @XmlElement( name = "message" )
        public String getMessage()
        {
            return message;
        }

        public void setMessage( String message )
        {
            this.message = message;
        }

        @Override
        public boolean equals( Object o )
        {
            if ( this == o )
            {
                return true;
            }
            if ( o == null || getClass() != o.getClass() )
            {
                return false;
            }

            ComponentErrorPayload that = ( ComponentErrorPayload ) o;

            if ( message != null ? !message.equals( that.message ) : that.message != null )
            {
                return false;
            }
            if ( name != null ? !name.equals( that.name ) : that.name != null )
            {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode()
        {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + ( message != null ? message.hashCode() : 0 );
            return result;
        }
    }
}
