package org.jtwig.functions.extract;

import com.google.common.base.Optional;
import org.jtwig.functions.FunctionArgument;
import org.jtwig.functions.annotations.JtwigFunction;
import org.jtwig.functions.reference.FunctionReference;
import org.jtwig.reflection.MethodInvoker;
import org.jtwig.reflection.model.bean.BeanMethod;
import org.junit.Test;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

public class BeanMethodFunctionExtractorTest {
    private final FunctionNameExtractor functionNameExtractor = mock(FunctionNameExtractor.class);
    private final MethodInvoker<FunctionArgument> methodInvoker = mock(MethodInvoker.class);
    private BeanMethodFunctionExtractor underTest = new BeanMethodFunctionExtractor(methodInvoker, functionNameExtractor);

    @Test
    public void extractWithoutAliases() throws Exception {
        BeanMethod beanMethod = mock(BeanMethod.class, RETURNS_DEEP_STUBS);
        JtwigFunction jtwigFunction = mock(JtwigFunction.class);
        when(jtwigFunction.aliases()).thenReturn(new String[]{""});
        when(beanMethod.method().annotation(JtwigFunction.class)).thenReturn(Optional.of(jtwigFunction));

        Collection<FunctionReference> result = underTest.extract(beanMethod);

        assertThat(result.size(), is(1));
    }
    @Test
    public void extractWithAliases() throws Exception {
        BeanMethod beanMethod = mock(BeanMethod.class, RETURNS_DEEP_STUBS);
        JtwigFunction jtwigFunction = mock(JtwigFunction.class);
        when(jtwigFunction.aliases()).thenReturn(new String[]{"one"});
        when(beanMethod.method().annotation(JtwigFunction.class)).thenReturn(Optional.of(jtwigFunction));

        Collection<FunctionReference> result = underTest.extract(beanMethod);

        assertThat(result.size(), is(2));
    }
}