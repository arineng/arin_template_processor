package net.arin.tp.processor.template.validation;

import net.arin.tp.processor.BaseTest;
import net.arin.tp.processor.template.PocTemplateImpl;
import net.arin.tp.processor.template.PocTemplateV5Impl;
import net.arin.tp.processor.template.TemplateImpl;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class PocTemplateValidatorTest extends BaseTest
{
    @Test
    public void testValidateModify()
    {
        PocTemplateImpl template = new PocTemplateV5Impl();
        template.setAction( TemplateImpl.Action.MODIFY );
        template.setPocHandle( "ASDF1234" );

        validateNoError( template );
    }

    @Test
    public void testValidateModifyNoPocHandle()
    {
        PocTemplateImpl template = new PocTemplateV5Impl();
        template.setAction( TemplateImpl.Action.MODIFY );

        validateError( template );
    }

    @Test
    public void testValidateRemove()
    {
        PocTemplateImpl template = new PocTemplateV5Impl();
        template.setAction( TemplateImpl.Action.REMOVE );
        template.setPocHandle( "ASDF1234" );

        validateNoError( template );
    }

    @Test
    public void testValidateRemoveNoPocHandle()
    {
        PocTemplateImpl template = new PocTemplateV5Impl();
        template.setAction( TemplateImpl.Action.REMOVE );

        validateError( template );
    }

    @Test
    public void testValidateCreate()
    {
        PocTemplateImpl template = new PocTemplateV5Impl();
        template.setAction( TemplateImpl.Action.CREATE );

        validateNoError( template );
    }

    private void validateError( PocTemplateImpl template )
    {
        PocTemplateValidator validator = new PocTemplateValidator();
        Assert.assertFalse( validator.validate( template ), "Expected template to not be valid" );

        List<Validator.ValidationError> errors = validator.getValidationErrors();
        Assert.assertNotNull( errors );
        Assert.assertEquals( errors.size(), 1, "Number of validation errors" );
        Assert.assertEquals( errors.get( 0 ).getCode(), Validator.ValidationCode.MISSING_REQUIRED_ELEMENT );
        Assert.assertNotNull( errors.get( 0 ).getErrorMessage(), "Error message should not be null" );
    }

    private void validateNoError( PocTemplateImpl template )
    {
        PocTemplateValidator validator = new PocTemplateValidator();
        Assert.assertTrue( validator.validate( template ), "Expected template to be valid" );

        List<Validator.ValidationError> errors = validator.getValidationErrors();
        Assert.assertNotNull( errors, "Expected error list to be non-null" );
        Assert.assertEquals( errors.size(), 0, "Expected error list to be empty" );
    }
}
