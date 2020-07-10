package net.arin.tp.utils.classfinder.test;

class ClassTestException extends RuntimeException
{
    ClassTestException( ClassTest clazz, String message )
    {
        super( "A ClassTest failed (" + clazz.getClass().getName() + "): " + message );
    }
}
