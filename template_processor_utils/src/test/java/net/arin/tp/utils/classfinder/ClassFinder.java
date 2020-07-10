package net.arin.tp.utils.classfinder;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import net.arin.tp.utils.classfinder.predicate.ClassPredicate;
import net.arin.tp.utils.classfinder.test.ClassTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This is a class that will find other classes in the same directory tree as the "seed" class you give it at
 * construction time.
 * <p/>
 * Remember, a directory is not the same as a package. This will allow you to find all classes in a DIRECTORY... NOT a
 * package.
 */
public class ClassFinder
{
    private final Class seed;
    private final ClassLoader classLoader;
    private Set<Class> classes = new HashSet<>();

    /**
     * Initialize a ClassFinder with all classes in the same directory as the given class.
     *
     * @param seedClassName Classes in the same directory as this class will be added to the ClassFinder.
     * @throws Exception Some exception
     */
    public ClassFinder( String seedClassName ) throws Exception
    {
        this.classLoader = this.getClass().getClassLoader();
        this.seed = classLoader.loadClass( seedClassName );

        ImmutableSet<ClassPath.ClassInfo> classInfos = ClassPath.from( this.classLoader ).getTopLevelClassesRecursive( this.seed.getName() );

        for ( ClassPath.ClassInfo classInfo : classInfos )
        {
            classes.add( classInfo.load() );
        }
    }

    private ClassFinder( Class seed, ClassLoader classLoader, Set<Class> classes )
    {
        this.seed = seed;
        this.classLoader = classLoader;
        this.classes = classes;
    }

    /**
     * Get all classes that this ClassFinder found.
     */
    public Set<Class> getClasses()
    {
        return Collections.unmodifiableSet( classes );
    }

    /**
     * Get all classes AFTER filtering by zero or more ClassPredicates.
     *
     * @param predicates Zero or more predicates on which to filter. For a class to be returned, it must meet the
     *                   requirements of EACH filter specified.
     * @return A set of classes
     */
    public Set<Class> getClasses( ClassPredicate... predicates )
    {
        List<ClassPredicate> listPredicates = new ArrayList<>( Arrays.asList( predicates ) );
        Set<Class> filtered = applyPredicates( classes, listPredicates );

        return Collections.unmodifiableSet( filtered );
    }

    /**
     * Similar to getClasses() except this method returns a ClassFinder. This is useful because after filtering, you can
     * applyTests() directly to the return value of filter().
     */
    public ClassFinder filter( ClassPredicate... predicates )
    {
        return new ClassFinder( seed, classLoader, getClasses( predicates ) );
    }

    /**
     * Apply zero or more ClassTests to the classes in this ClassFinder.
     */
    public Set<Class> applyTests( ClassTest... classTests )
    {
        List<ClassTest> listClassTests = new ArrayList<>( Arrays.asList( classTests ) );
        return applyTests( classes, listClassTests );
    }

    @SuppressWarnings( "unchecked" )
    private Set<Class> applyPredicates( Set<Class> unfiltered, List<ClassPredicate> predicates )
    {
        if ( predicates.size() == 0 )
        {
            return unfiltered;
        }
        else
        {
            return applyPredicates( new HashSet( Collections2.filter( unfiltered, predicates.remove( 0 ) ) ), predicates );
        }
    }

    @SuppressWarnings( "unchecked" )
    private Set<Class> applyTests( Set<Class> untested, List<ClassTest> tests )
    {
        if ( tests.size() == 0 )
        {
            return untested;
        }
        else
        {
            return applyTests( new HashSet( Collections2.transform( untested, tests.remove( 0 ) ) ), tests );
        }
    }
}
