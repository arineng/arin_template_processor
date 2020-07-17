package net.arin.tp.api.payload;

import java.util.List;

public interface PayloadWithOriginAses
{
    List<String> getOriginAses();

    void setOriginAses( List<String> originAses );
}
