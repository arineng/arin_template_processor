package net.arin.tp.utils.classfinder.predicate;

/**
 * A Predicate that will filter out all classes that are not descendants of a specified class.
 */
public class ExtendsClassPredicate implements ClassPredicate
{
    private Class clazz;

    public ExtendsClassPredicate( Class clazz )
    {
        this.clazz = clazz;
    }

    @SuppressWarnings( "unchecked" )
    public boolean apply( Class clazz )
    {
        return this.clazz.isAssignableFrom( clazz );
    }
}
