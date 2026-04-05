import java.util.*;

public class ExpressionEvaluator {

    public double evaluate(String expression, Map<String, Double> variables) {
        if (expression == null || expression.isBlank()) {
            throw new IllegalArgumentException("Пустое выражение");
        }

        List<String> tokens = tokenize(expression);
        List<String> rpn = toRpn(tokens);
        return evaluateRpn(rpn, variables == null ? Collections.emptyMap() : variables);
    }

    public Set<String> extractVariables(String expression) {
        if (expression == null) {
            throw new IllegalArgumentException("Выражение равно null");
        }

        Set<String> variables = new LinkedHashSet<>();
        for (String token : tokenize(expression)) {
            if (isVariable(token)) {
                variables.add(token);
            }
        }
        return variables;
    }

    private List<String> tokenize(String expression) {
        List<String> tokens = new ArrayList<>();
        int i = 0;

        while (i < expression.length()) {
            char ch = expression.charAt(i);

            if (Character.isWhitespace(ch)) {
                i++;
                continue;
            }

            if (Character.isDigit(ch) || ch == '.') {
                StringBuilder number = new StringBuilder();
                boolean hasDot = false;

                while (i < expression.length()) {
                    char c = expression.charAt(i);
                    if (Character.isDigit(c)) {
                        number.append(c);
                        i++;
                    } else if (c == '.') {
                        if (hasDot) {
                            throw new IllegalArgumentException("Некорректное число");
                        }
                        hasDot = true;
                        number.append(c);
                        i++;
                    } else {
                        break;
                    }
                }

                if (number.toString().equals(".")) {
                    throw new IllegalArgumentException("Некорректное число");
                }

                tokens.add(number.toString());
                continue;
            }

            if (Character.isLetter(ch)) {
                StringBuilder name = new StringBuilder();
                while (i < expression.length()) {
                    char c = expression.charAt(i);
                    if (Character.isLetterOrDigit(c) || c == '_') {
                        name.append(c);
                        i++;
                    } else {
                        break;
                    }
                }
                tokens.add(name.toString());
                continue;
            }

            if (isOperatorChar(ch) || ch == '(' || ch == ')') {
                tokens.add(String.valueOf(ch));
                i++;
                continue;
            }

            throw new IllegalArgumentException("Недопустимый символ: " + ch);
        }

        return tokens;
    }

    private List<String> toRpn(List<String> tokens) {
        List<String> output = new ArrayList<>();
        Deque<String> operators = new ArrayDeque<>();

        String previous = null;

        for (String token : tokens) {
            if (isNumber(token) || isVariable(token)) {
                output.add(token);
            } else if (isOperator(token)) {
                if (previous == null || isOperator(previous) || "(".equals(previous)) {
                    throw new IllegalArgumentException("Некорректное расположение оператора: " + token);
                }

                while (!operators.isEmpty()
                        && isOperator(operators.peek())
                        && precedence(operators.peek()) >= precedence(token)) {
                    output.add(operators.pop());
                }
                operators.push(token);
            } else if ("(".equals(token)) {
                operators.push(token);
            } else if (")".equals(token)) {
                boolean foundLeftBracket = false;
                while (!operators.isEmpty()) {
                    String top = operators.pop();
                    if ("(".equals(top)) {
                        foundLeftBracket = true;
                        break;
                    }
                    output.add(top);
                }
                if (!foundLeftBracket) {
                    throw new IllegalArgumentException("Несогласованные скобки");
                }
            } else {
                throw new IllegalArgumentException("Неизвестный токен: " + token);
            }

            previous = token;
        }

        if (previous != null && isOperator(previous)) {
            throw new IllegalArgumentException("Выражение не может заканчиваться оператором");
        }

        while (!operators.isEmpty()) {
            String top = operators.pop();
            if ("(".equals(top) || ")".equals(top)) {
                throw new IllegalArgumentException("Несогласованные скобки");
            }
            output.add(top);
        }

        return output;
    }

    private double evaluateRpn(List<String> rpn, Map<String, Double> variables) {
        Deque<Double> stack = new ArrayDeque<>();

        for (String token : rpn) {
            if (isNumber(token)) {
                stack.push(Double.parseDouble(token));
            } else if (isVariable(token)) {
                Double value = variables.get(token);
                if (value == null) {
                    throw new IllegalArgumentException("Не задано значение переменной: " + token);
                }
                stack.push(value);
            } else if (isOperator(token)) {
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("Некорректное выражение");
                }

                double b = stack.pop();
                double a = stack.pop();

                switch (token) {
                    case "+" -> stack.push(a + b);
                    case "-" -> stack.push(a - b);
                    case "*" -> stack.push(a * b);
                    case "/" -> {
                        if (b == 0.0) {
                            throw new ArithmeticException("Деление на ноль");
                        }
                        stack.push(a / b);
                    }
                    default -> throw new IllegalArgumentException("Неизвестная операция: " + token);
                }
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException("Некорректное выражение");
        }

        return stack.pop();
    }

    private boolean isNumber(String token) {
        return token.matches("\\d+(\\.\\d+)?|\\.\\d+");
    }

    private boolean isVariable(String token) {
        return token.matches("[a-zA-Z][a-zA-Z0-9_]*");
    }

    private boolean isOperator(String token) {
        return "+-*/".contains(token) && token.length() == 1;
    }

    private boolean isOperatorChar(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }

    private int precedence(String operator) {
        return switch (operator) {
            case "+", "-" -> 1;
            case "*", "/" -> 2;
            default -> 0;
        };
    }
}