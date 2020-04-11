package org.hehh.cloud.validator;

import org.hehh.cloud.validator.annotation.PropertyScriptAssert;
import org.hibernate.validator.internal.util.Contracts;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.hibernate.validator.internal.util.logging.Messages;
import org.hibernate.validator.spi.scripting.ScriptEvaluator;
import org.hibernate.validator.spi.scripting.ScriptEvaluatorFactory;

import javax.script.ScriptException;
import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author: HeHui
 * @create: 2020-03-21 02:49
 * @description: 脚本验证起
 **/
public class PropertyScriptAssertValidator  implements ConstraintValidator<PropertyScriptAssert, Object> {


    private String script;
    private String languageName;
    private String alias;
    private String property;
    private String message;



    public void initialize(PropertyScriptAssert constraintAnnotation) {
        validateParameters(constraintAnnotation);

        this.script = constraintAnnotation.script();
        this.languageName = constraintAnnotation.lang();
        this.alias = constraintAnnotation.alias();
        this.property = constraintAnnotation.property();
        this.message = constraintAnnotation.message();
    }

    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {

//        Object evaluationResult;
//        ScriptEvaluator scriptEvaluator;
//
//        try {
//            ScriptEvaluatorFactory evaluatorFactory = ScriptEvaluatorFactory.getInstance();
//            scriptEvaluator = evaluatorFactory.getScriptEvaluatorByLanguageName(languageName);
//        } catch (ScriptException e) {
//            throw new ConstraintDeclarationException(e);
//        }
//
//        try {
//            evaluationResult = scriptEvaluator.evaluate(script, value, alias);
//        } catch (ScriptException e) {
//            throw log.getErrorDuringScriptExecutionException(script, e);
//        }
//
//        if (evaluationResult == null) {
//            throw log.getScriptMustReturnTrueOrFalseException(script);
//        }
//        if (!(evaluationResult instanceof Boolean)) {
//            throw log.getScriptMustReturnTrueOrFalseException(
//                    script,
//                    evaluationResult,
//                    evaluationResult.getClass().getCanonicalName()
//            );
//        }
//
//        if (Boolean.FALSE.equals(evaluationResult)) {
//            constraintValidatorContext.disableDefaultConstraintViolation();
//            constraintValidatorContext
//                    .buildConstraintViolationWithTemplate(message)
//                    .addPropertyNode(property)
//                    .addConstraintViolation();
//        }

        return Boolean.TRUE.equals(true);
    }

    private void validateParameters(PropertyScriptAssert constraintAnnotation) {
        Contracts.assertNotEmpty(constraintAnnotation.script(), Messages.MESSAGES.parameterMustNotBeEmpty("script"));
        Contracts.assertNotEmpty(constraintAnnotation.lang(), Messages.MESSAGES.parameterMustNotBeEmpty("lang"));
        Contracts.assertNotEmpty(constraintAnnotation.alias(), Messages.MESSAGES.parameterMustNotBeEmpty("alias"));
        Contracts.assertNotEmpty(constraintAnnotation.property(), Messages.MESSAGES.parameterMustNotBeEmpty("property"));
        Contracts.assertNotEmpty(constraintAnnotation.message(), Messages.MESSAGES.parameterMustNotBeEmpty("message"));
    }
}
