package net.arin.tp.utils.classfinder.predicate;

import com.google.common.base.Predicate;

/**
 * If you wish to provide a predicate to applyPredicates() or filter(), implement this interface.
 */
public interface ClassPredicate extends Predicate<Class>
{
}
