package net.arin.tp.processor.template.validation;

import net.arin.tp.processor.template.OrgTemplateImpl;
import net.arin.tp.processor.template.OrgTemplateV5Impl;
import net.arin.tp.processor.template.TemplateImpl;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class OrgTemplateValidatorTest
{
    @Test
    public void testValidateModify()
    {
        OrgTemplateImpl template = new OrgTemplateV5Impl();
        template.setAction( TemplateImpl.Action.MODIFY );
        template.setOrgHandle( "ASDF1234" );

        validateNoError( template );
    }

    @Test
    public void testValidateModifyNoOrgHandle()
    {
        OrgTemplateImpl template = new OrgTemplateV5Impl();
        template.setAction( TemplateImpl.Action.MODIFY );

        validateError( template );
    }

    @Test
    public void testValidateRemove()
    {
        OrgTemplateImpl template = new OrgTemplateV5Impl();
        template.setAction( TemplateImpl.Action.MODIFY );
        template.setOrgHandle( "ASDF1234" );

        validateNoError( template );
    }

    @Test
    public void testValidateRemoveNoOrgHandle()
    {
        OrgTemplateImpl template = new OrgTemplateV5Impl();
        template.setAction( TemplateImpl.Action.MODIFY );

        validateError( template );
    }

    @Test
    public void testValidateCreate()
    {
        OrgTemplateImpl template = new OrgTemplateV5Impl();
        template.setAction( TemplateImpl.Action.CREATE );

        validateNoError( template );
    }

    private void validateError( OrgTemplateImpl template )
    {
        OrgTemplateValidator validator = new OrgTemplateValidator();
        Assert.assertFalse( validator.validate( template ), "Expected template to not be valid" );

        List<Validator.ValidationError> errors = validator.getValidationErrors();
        Assert.assertNotNull( errors );
        Assert.assertEquals( errors.size(), 1, "Number of validation errors" );
        Assert.assertEquals( errors.get( 0 ).getCode(), Validator.ValidationCode.MISSING_REQUIRED_ELEMENT );
        Assert.assertNotNull( errors.get( 0 ).getErrorMessage(), "Error message should not be null" );
    }

    private void validateNoError( OrgTemplateImpl template )
    {
        OrgTemplateValidator validator = new OrgTemplateValidator();
        Assert.assertTrue( validator.validate( template ), "Expected template to be valid" );

        List<Validator.ValidationError> errors = validator.getValidationErrors();
        Assert.assertNotNull( errors, "Expected error list to be non-null" );
        Assert.assertEquals( errors.size(), 0, "Expected error list to be empty" );
    }
}
