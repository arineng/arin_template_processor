package net.arin.tp.processor.exception;

import net.arin.tp.processor.message.TemplateMessage;
import net.arin.tp.processor.template.ComplexSwipTemplateImpl;
import org.jboss.resteasy.client.ClientResponseFailure;

import java.util.Map;

/**
 * This class is similar to the RESTResponseFailure but will allow you to use one of the embedded templates.
 */
public class EmbeddedOrgRESTResponseFailure extends RESTResponseFailure
{
    public EmbeddedOrgRESTResponseFailure( ClientResponseFailure crf )
    {
        super( crf );
    }

    @Override
    protected Map<String, String> getTemplateMapping( TemplateMessage template )
    {
        ComplexSwipTemplateImpl tmp = ( ComplexSwipTemplateImpl ) template.getTemplate();

        return tmp.getEmbeddedOrg().getXmlToFieldKeyMapping();
    }
}
