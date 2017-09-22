package org.jtwig.integration.issues;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class Issue359Test {

    public interface TestDefaultInterface {
        default String testDefaultInterfaceMethod() {
            return "default interface method";
        }
    }

    public interface TestNonDefaultInterface {
        String testNonDefaultInterfaceMethod();
    }

    public static class TestParentClass {
        public String testParentClassMethod() {
            return "parent class method";
        }
    }

    public static class ClassUnderTest extends TestParentClass implements TestDefaultInterface, TestNonDefaultInterface {
        @Override
        public String testNonDefaultInterfaceMethod() {
            return "non-default interface method";
        }
    }

    @Test
    public void issue359() throws Exception {
        ClassUnderTest classUnderTest = new ClassUnderTest();
        JtwigTemplate template = JtwigTemplate.inlineTemplate("[{{ value.testDefaultInterfaceMethod() }}] [{{ value.testNonDefaultInterfaceMethod() }}] [{{ value.testParentClassMethod() }}]");

        String result = template.render(JtwigModel.newModel().with("value", classUnderTest));
        assertThat(result, is("[] [non-default interface method] [parent class method]"));

        assertThat(classUnderTest.testDefaultInterfaceMethod(), is("default interface method"));
        assertThat(classUnderTest.testNonDefaultInterfaceMethod(), is("non-default interface method"));
        assertThat(classUnderTest.testParentClassMethod(), is("parent class method"));
    }
}
