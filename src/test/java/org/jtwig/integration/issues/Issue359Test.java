package org.jtwig.integration.issues;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class Issue359Test {

    public interface TestInterface {
        default String testDefaultInterfaceMethod() {
            return "default interface method";
        }
    }

    public static class TestParentClass {
        public String testParentClassMethod() {
            return "parent class method";
        }
    }

    public static class ClassUnderTest extends TestParentClass implements TestInterface {

    }

    @Test
    public void issue359() throws Exception {
        ClassUnderTest classUnderTest = new ClassUnderTest();
        JtwigTemplate template = JtwigTemplate.inlineTemplate("[{{ value.testDefaultInterfaceMethod() }}] [{{ value.testParentClassMethod() }}]");

        String result = template.render(JtwigModel.newModel().with("value", classUnderTest));
        assertThat(result, is("[] [parent class method]"));

        assertThat(classUnderTest.testDefaultInterfaceMethod(), is("default interface method"));
        assertThat(classUnderTest.testParentClassMethod(), is("parent class method"));
    }
}
