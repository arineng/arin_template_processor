package net.arin.tp.utils.classfinder.predicate;

import java.lang.reflect.Modifier;

/**
 * A Predicate that will filter out all classes that aren't concrete.
 */
public class ConcreteClassPredicate implements ClassPredicate
{
    public ConcreteClassPredicate()
    {
    }

    public boolean apply( Class clazz )
    {
        int modifiers = clazz.getModifiers();

        return !( Modifier.isAbstract( modifiers ) || Modifier.isInterface( modifiers ) );
    }
}
