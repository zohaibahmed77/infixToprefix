package com.example.infixprefix;

public class ConversionStep {
    private int step;
    private String symbol;
    private String stack;
    private String output;

    public ConversionStep(int step, String symbol, String stack, String output) {
        this.step = step;
        this.symbol = symbol;
        this.stack = stack;
        this.output = output;
    }

    public int getStep() {
        return step;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getStack() {
        return stack;
    }

    public String getOutput() {
        return output;
    }
}
