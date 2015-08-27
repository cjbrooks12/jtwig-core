package org.jtwig.model.expression.lists;

import com.google.common.base.Optional;
import org.jtwig.value.JtwigValue;
import org.jtwig.value.JtwigValueFactory;
import org.jtwig.value.configuration.DefaultValueConfiguration;
import org.junit.Test;

import java.util.Collection;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class IntegerAscendingOrderEnumerationListStrategyTest {
    private IntegerAscendingOrderEnumerationListStrategy underTest = new IntegerAscendingOrderEnumerationListStrategy();

    @Test
    public void enumerateWhenDifferentTypes() throws Exception {
        JtwigValue left = JtwigValueFactory.value(1, new DefaultValueConfiguration());
        JtwigValue right = JtwigValueFactory.value('a', new DefaultValueConfiguration());

        Optional<Collection<Object>> result = underTest.enumerate(left, right);

        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void enumerateWhenNotNumber() throws Exception {
        JtwigValue left = JtwigValueFactory.value('b', new DefaultValueConfiguration());
        JtwigValue right = JtwigValueFactory.value('a', new DefaultValueConfiguration());

        Optional<Collection<Object>> result = underTest.enumerate(left, right);

        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void enumerateWhenLeftLowerThanRight() throws Exception {
        JtwigValue left = JtwigValueFactory.value(1, new DefaultValueConfiguration());
        JtwigValue right = JtwigValueFactory.value(3, new DefaultValueConfiguration());

        Optional<Collection<Object>> result = underTest.enumerate(left, right);

        assertThat(result.isPresent(), is(true));
    }

    @Test
    public void enumerateWhenLeftGreaterThanRight() throws Exception {
        JtwigValue left = JtwigValueFactory.value(3, new DefaultValueConfiguration());
        JtwigValue right = JtwigValueFactory.value(1, new DefaultValueConfiguration());

        Optional<Collection<Object>> result = underTest.enumerate(left, right);

        assertThat(result.isPresent(), is(false));
    }
}