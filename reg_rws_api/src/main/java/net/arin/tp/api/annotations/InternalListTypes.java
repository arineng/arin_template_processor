package net.arin.tp.api.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is intended to be used alongside the @XmlElements annotation. When documentation is generated, list
 * payloads will be documented with the possible return type payload contents and exclude the payload types that are not
 * applicable since they are internally used.
 */
@Target( ElementType.METHOD )
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface InternalListTypes
{
    /**
     * Classes to exclude from generated documentation since they are internal types only.
     *
     * @return array of classes to exclude
     */
    Class[] value() default {};
}
