package org.jtwig;

import com.google.common.base.Optional;
import org.jtwig.reflection.model.Value;

import java.util.HashMap;
import java.util.Map;

public class JtwigModel {
    public static JtwigModel newModel(Map<String, Object> values) {
        JtwigModel model = newModel();
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            model.with(entry.getKey(), entry.getValue());
        }
        return model;
    }
    public static JtwigModel newModel () {
        return new JtwigModel();
    }

    private final Map<String, Value> values;

    public JtwigModel() {
        this.values = new HashMap<>();
    }

    public JtwigModel with(String name, Object value) {
        values.put(name, new Value(value));
        return this;
    }

    public Optional<Value> get (String key) {
        return Optional.fromNullable(values.get(key));
    }
}
