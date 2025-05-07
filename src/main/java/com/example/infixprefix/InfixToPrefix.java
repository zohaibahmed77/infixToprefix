package com.example.infixprefix;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Stack;

public class InfixToPrefix {

    public static class ConversionResult {
        private String result;
        private List<ConversionStep> steps;

        public ConversionResult(String result, List<ConversionStep> steps) {
            this.result = result;
            this.steps = steps;
        }

        public String getResult() {
            return result;
        }

        public List<ConversionStep> getSteps() {
            return steps;
        }
    }

    static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    public static int precedence(char operator) {
        if (operator == '+' || operator == '-') return 1;
        else if (operator == '*' || operator == '/') return 2;
        else if (operator == '^') return 3;
        return -1;
    }

    static boolean isValidExpression(String input) {
        // Base check: only valid characters
        Pattern pattern = Pattern.compile("[a-zA-Z0-9+\\-*/^()]+");
        Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) return false;

        // Reject expressions starting with an operator (likely prefix)
        if (input.length() > 0 && isOperator(input.charAt(0))) return false;

        // Must have at least one operator and one operand
        boolean hasOperator = input.matches(".*[+\\-*/^].*");
        boolean hasOperand = input.matches(".*[a-zA-Z0-9].*");

        return hasOperator && hasOperand;
    }

    public static ConversionResult infixToPrefixWithSteps(String infix) {
        List<ConversionStep> steps = new ArrayList<>();

        if (!isValidExpression(infix)) {
            return new ConversionResult("Invalid Expression!", steps);
        }

        StringBuilder reversed = new StringBuilder(infix).reverse();
        for (int i = 0; i < reversed.length(); i++) {
            char c = reversed.charAt(i);
            if (c == '(') reversed.setCharAt(i, ')');
            else if (c == ')') reversed.setCharAt(i, '(');
        }

        String revInfix = reversed.toString();
        StringBuilder result = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < revInfix.length(); i++) {
            char c = revInfix.charAt(i);
            String symbol = String.valueOf(c);

            if (Character.isLetterOrDigit(c)) {
                result.append(c);
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    result.append(stack.pop());
                }
                if (!stack.isEmpty()) stack.pop();
            } else if (isOperator(c)) {
                while (!stack.isEmpty() && precedence(c) < precedence(stack.peek())) {
                    result.append(stack.pop());
                }
                while (!stack.isEmpty() && precedence(c) == precedence(stack.peek()) && c != '^') {
                    result.append(stack.pop());
                }
                stack.push(c);
            }

            steps.add(new ConversionStep(
                    steps.size() + 1,
                    symbol,
                    stackToString(stack),
                    new StringBuilder(result).reverse().toString()
            ));
        }

        while (!stack.isEmpty()) {
            char popped = stack.pop();
            result.append(popped);

            steps.add(new ConversionStep(
                    steps.size() + 1,
                    "POP (" + popped + ")",
                    stackToString(stack),
                    new StringBuilder(result).reverse().toString()
            ));
        }

        return new ConversionResult(result.reverse().toString(), steps);
    }

    private static String stackToString(Stack<Character> stack) {
        return stack.toString().replaceAll("[\\[\\], ]", "");
    }
}

