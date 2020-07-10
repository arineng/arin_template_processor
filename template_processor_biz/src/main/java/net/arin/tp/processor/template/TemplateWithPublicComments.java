package net.arin.tp.processor.template;

import java.util.List;

public interface TemplateWithPublicComments extends Template
{
    List<String> getPublicComments();

    void setPublicComments( List<String> publicComments );
}
