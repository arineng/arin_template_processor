package net.arin.tp.api.annotations;

import java.lang.annotation.*;

/**
 * This annotation signifies that the field in question is system generated and can never be modified by an end user.
 */
@Target( ElementType.METHOD )
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface SystemGenerated
{
}
