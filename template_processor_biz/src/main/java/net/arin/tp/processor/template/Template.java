package net.arin.tp.processor.template;

import java.util.List;
import java.util.Map;

public interface Template
{
    String getTemplateName();

    String getTemplateVersion();

    String getFullTemplateName();

    String getApiKey();

    void setApiKey( String apiKey );

    List<Attachment> getAttachments();

    void setAttachments( List<Attachment> attachments );

    Map<String, String> getXmlToFieldKeyMapping();
}
