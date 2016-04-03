package org.jtwig.render.expression.calculator.operation.binary.impl;

import org.jtwig.render.expression.calculator.operation.binary.BinaryOperator;

public class GreaterOrEqualOperator implements BinaryOperator {
    @Override
    public String symbol() {
        return ">=";
    }

    @Override
    public int precedence() {
        return 15;
    }
}
