package com.calc.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CalcService {

    // 运算符优先级
    private int getPriority(char op) {
        return switch (op) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            default -> 0;
        };
    }

    // 判断是不是运算符
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    /**
     * 对外暴露方法，controller调用这个
     * @param expr 输入数学表达式，例如 "1+2*(3.5-1)"
     * @return 计算结果字符串
     */
    public String evaluate(String expr) {
        // 去掉所有空格
        expr = expr.replaceAll("\\s+", "");
        List<String> postfix = infixToPostfix(expr);
        double result = calcPostfix(postfix);
        // 整数就输出整数，小数输出小数
        if (result == Math.floor(result)) {
            return String.valueOf((long) result);
        } else {
            return String.valueOf(result);
        }
    }

    // 中缀表达式 转 后缀表达式（逆波兰）
    private List<String> infixToPostfix(String s) {
        List<String> postfix = new ArrayList<>();
        Deque<Character> opStack = new ArrayDeque<>();
        int n = s.length();
        int i = 0;

        while (i < n) {
            char c = s.charAt(i);
            if (Character.isDigit(c) || c == '.') {
                // 读取完整数字（支持小数）
                StringBuilder numSb = new StringBuilder();
                while (i < n && (Character.isDigit(s.charAt(i)) || s.charAt(i) == '.')) {
                    numSb.append(s.charAt(i));
                    i++;
                }
                postfix.add(numSb.toString());
            } else if (c == '(') {
                opStack.push(c);
                i++;
            } else if (c == ')') {
                // 弹出直到左括号
                while (!opStack.isEmpty() && opStack.peek() != '(') {
                    postfix.add(opStack.pop().toString());
                }
                if (opStack.isEmpty()) {
                    throw new RuntimeException("括号不匹配");
                }
                opStack.pop(); // 弹出 '('
                i++;
            } else if (isOperator(c)) {
                // 处理一元负号：开头是负号，或者前面是 ( 或者运算符
                if (c == '-' && (i == 0 || s.charAt(i - 1) == '(' || isOperator(s.charAt(i - 1)))) {
                    postfix.add("0");
                }
                while (!opStack.isEmpty() && isOperator(opStack.peek())
                        && getPriority(opStack.peek()) >= getPriority(c)) {
                    postfix.add(opStack.pop().toString());
                }
                opStack.push(c);
                i++;
            } else {
                throw new RuntimeException("非法字符：" + c);
            }
        }
        // 剩余运算符全部弹出
        while (!opStack.isEmpty()) {
            char top = opStack.pop();
            if (top == '(') {
                throw new RuntimeException("括号不匹配");
            }
            postfix.add(String.valueOf(top));
        }
        return postfix;
    }

    // 计算后缀表达式
    private double calcPostfix(List<String> postfix) {
        Deque<Double> numStack = new ArrayDeque<>();
        for (String token : postfix) {
            if (token.length() == 1 && isOperator(token.charAt(0))) {
                double b = numStack.pop();
                double a = numStack.pop();
                double res = switch (token.charAt(0)) {
                    case '+' -> a + b;
                    case '-' -> a - b;
                    case '*' -> a * b;
                    case '/' -> {
                        if (b == 0) {
                            throw new ArithmeticException("除数不能为0");
                        }
                        yield a / b;
                    }
                    default -> throw new RuntimeException("未知运算符");
                };
                numStack.push(res);
            } else {
                // 数字
                numStack.push(Double.parseDouble(token));
            }
        }
        if (numStack.size() != 1) {
            throw new RuntimeException("表达式格式错误");
        }
        return numStack.pop();
    }
}