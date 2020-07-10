package net.arin.tp.api.annotations;

import java.lang.annotation.*;

/**
 * When this annotation is applied to getter methods, it will signify that the field is not modifiable after it's
 * initial creation.
 */
@Target( ElementType.METHOD )
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface Immutable
{
}
