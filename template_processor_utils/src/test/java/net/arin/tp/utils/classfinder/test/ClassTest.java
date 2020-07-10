package net.arin.tp.utils.classfinder.test;

import com.google.common.base.Function;

public abstract class ClassTest implements Function<Class, Class>
{
    public final Class apply( Class clazz )
    {
        if ( !test( clazz ) )
        {
            throw new ClassTestException( this, this.getErrorMessage() );
        }

        return clazz;
    }

    /**
     * Override this method to determine what error message will be displayed upon failure of your test.
     */
    public String getErrorMessage()
    {
        return "Unknown reason. Override getErrorMessage() method on a given ClassTest "
                + "implementation if you want to see a more detailed message.";
    }

    public abstract boolean test( final Class clazz );
}
