package net.arin.tp.processor.template;

import net.arin.tp.utils.classfinder.ClassFinder;
import net.arin.tp.utils.classfinder.predicate.ConcreteClassPredicate;
import net.arin.tp.utils.classfinder.predicate.ExtendsClassPredicate;
import net.arin.tp.processor.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TemplateTest extends BaseTest
{
    @Test
    public void testGetFieldPatterns() throws Exception
    {

        ClassFinder cf = new ClassFinder( "net.arin.tp.processor.template.Template" );

        for ( Class clazz : cf.getClasses( new ConcreteClassPredicate(),
                new ExtendsClassPredicate( TemplateImpl.class ) ) )
        {
            TemplateImpl template = ( TemplateImpl ) clazz.newInstance();

            List<String> patterns = template.getFieldPatterns();
            List<String> fields = template.getFieldKeyValues();

            log.debug( "Here are the patterns that will be used to match field names with whitespaces:" );
            for ( String pattern : patterns )
            {
                log.debug( "Pattern: " + pattern );
            }

            log.info( "Complete field matching string: " + template.getFieldWithWhitespaceMatchString() );

            Pattern p = Pattern.compile( "^" + template.getFieldWithWhitespaceMatchString() + "$" );

            for ( String field : fields )
            {
                log.info( "Attempting to match: " + field );
                Assert.assertTrue( p.matcher( field ).matches() );
                log.info( "Field matched: " + field );
            }

            List<String> spaceyStrings = new ArrayList<>();

            String patternA, patternB, patternC, patternD, patternE, patternF;

            for ( String pattern : fields )
            {
                // If we explode existing whitespace, does it still match?
                patternA = pattern.replaceAll( " ", "  " );
                if ( !patternA.equals( pattern ) )
                {
                    spaceyStrings.add( patternA );
                }

                // If we put whitespace around commas, does it still match?
                patternB = pattern.replaceAll( ",", " , " );
                if ( !patternB.equals( pattern ) )
                {
                    spaceyStrings.add( patternB );
                }

                // If we put whitespace around left parens, does it still match?
                patternC = pattern.replaceAll( "\\(", " ( " );
                if ( !patternC.equals( pattern ) )
                {
                    spaceyStrings.add( patternC );
                }

                // If we put whitespace around right parens, does it still match?
                patternD = pattern.replaceAll( "\\)", " ) " );
                if ( !patternD.equals( pattern ) )
                {
                    spaceyStrings.add( patternD );
                }

                // If we put whitespace before colons, does it still match?
                patternE = pattern.replaceAll( ":", " :" );
                if ( !patternE.equals( pattern ) )
                {
                    spaceyStrings.add( patternE );
                }

                // If we put whitespace around slashes, does it still match?
                patternF = pattern.replaceAll( "/", " / " );
                if ( !patternF.equals( pattern ) )
                {
                    spaceyStrings.add( patternF );
                }
            }

            for ( String spaceyString : spaceyStrings )
            {
                log.info( "Attempting to match: " + spaceyString );
                Assert.assertTrue( p.matcher( spaceyString ).matches() );
                log.info( "Spacey field matched: " + spaceyString );
            }
        }
    }
}
