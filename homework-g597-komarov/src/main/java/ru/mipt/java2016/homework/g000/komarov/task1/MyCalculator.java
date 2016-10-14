package ru.mipt.java2016.homework.g000.komarov.task1;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.ArrayList;

import ru.mipt.java2016.homework.base.task1.Calculator;
import ru.mipt.java2016.homework.base.task1.ParsingException;

public class MyCalculator implements Calculator {
   public double calculate(String expression) throws ParsingException {
       ArrayList<Token> parsedString = parse(expression);
       return stackCalculate(parsedString);
    }

    private ArrayList<Token> parse(String expression) throws ParsingException{
        if (expression == null) throw new ParsingException("Null expression");
        ArrayList<Token> resultArray = new ArrayList<>();
        String digits = "0123456789.";
        for (int i = 0; i < expression.length(); i++){
            Token bufToken;
            switch(expression.charAt(i)) {
                case ' ':
                    break;
                case '\n':
                    break;
                case '\t':
                    break;
                case '+':
                    bufToken = new Token('+');
                    resultArray.add(bufToken);
                    break;
                case '-':
                    bufToken = new Token('-');
                    if (resultArray.size() == 0){
                        resultArray.add(new Token(0));
                        resultArray.add(bufToken);
                        break;
                    }
                    else if (!resultArray.get(resultArray.size() - 1).type){
                        String bufString = "-";
                        String concatenatedString = "";
                        while (digits.contains(bufString) || (expression.charAt(i) == ' ') || (bufString == "-")) {
                            if (expression.charAt(i) == ' '){
                                i++;
                                bufString = Character.toString(expression.charAt(i));
                                continue;
                            }
                            concatenatedString += bufString;
                            i++;
                            if (i < expression.length()) {
                                bufString = Character.toString(expression.charAt(i));
                            }
                            else break;
                        }
                        i--;
                        Double someDouble = Double.valueOf(concatenatedString);
                        bufToken = new Token(someDouble);
                        resultArray.add(bufToken);
                        break;
                    }
                    else resultArray.add(bufToken);
                    break;
                case '*':
                    bufToken = new Token('*');
                    resultArray.add(bufToken);
                    break;
                case '/':
                    bufToken = new Token('/');
                    resultArray.add(bufToken);
                    break;
                case '(':
                    bufToken = new Token('(');
                    resultArray.add(bufToken);
                    break;
                case ')':
                    bufToken = new Token(')');
                    resultArray.add(bufToken);
                    break;
                default:
                    char[] charForConstructor = {expression.charAt(i)};
                    String bufString = new String(charForConstructor);
                    if(!digits.contains(bufString)){
                        throw new ParsingException("Wrong symbol");
                    }
                    else{
                        String concatenatedString = "";
                        while (digits.contains(bufString)) {
                            concatenatedString += bufString;
                            i++;
                            if (i < expression.length()) {
                                bufString = Character.toString(expression.charAt(i));
                            }
                            else break;
                        }
                        i--;
                        int checkDoubts = 0;
                        for (int j = 0; j < concatenatedString.length(); j++){
                            if (concatenatedString.charAt(j) == '.') checkDoubts++;
                        }
                        if (checkDoubts > 1) throw new ParsingException("Wrong expression");
                        Double someDouble = Double.valueOf(concatenatedString);
                        bufToken = new Token(someDouble);
                        resultArray.add(bufToken);
                    }
                    break;
            }
        }
        return resultArray;
    }

    private double stackCalculate(ArrayList<Token> expression) throws ParsingException{
        Stack<Double> operands = new Stack<>();
        Stack<Character> functions = new Stack<>();
        for (int i = 0; i < expression.size(); i++){
            if(expression.get(i).type){
                operands.push(expression.get(i).doubleToken);
            }
            else{
                switch (expression.get(i).charToken){
                    case '+':
                        if (!functions.empty() && ((functions.peek() == '*') || (functions.peek() == '/'))){
                            char popedOperator = functions.pop();
                            if (operands.empty()) throw new ParsingException("Wrong expression");
                            double secondOperand = operands.pop();
                            if (operands.empty()) throw new ParsingException("Wrong expression");
                            double firstOperand = operands.pop();
                            BinOperator someOperator = new BinOperator(firstOperand, secondOperand, popedOperator);
                            operands.push(someOperator.run());
                        }
                        functions.push('+');
                        break;
                    case '-':
                        if (!functions.empty() && ((functions.peek() == '*') || (functions.peek() == '/'))){
                            char popedOperator = functions.pop();
                            if (operands.empty()) throw new ParsingException("Wrong expression");
                            double secondOperand = operands.pop();
                            if (operands.empty()) throw new ParsingException("Wrong expression");
                            double firstOperand = operands.pop();
                            BinOperator someOperator = new BinOperator(firstOperand, secondOperand, popedOperator);
                            operands.push(someOperator.run());
                        }
                        functions.push('-');
                        break;
                    case '*':
                        functions.push('*');
                        break;
                    case '/':
                        functions.push('/');
                        break;
                    case '(':
                        functions.push('(');
                        break;
                    case ')':
                        if (!functions.empty()) {
                            try {
                                while (functions.peek() != '(') {
                                    if (functions.empty()) throw new ParsingException("Wrong expression");
                                    char popedOperator = functions.pop();
                                    if (operands.empty()) throw new ParsingException("Wrong expression");
                                    double secondOperand = operands.pop();
                                    if (operands.empty()) throw new ParsingException("Wrong expression");
                                    double firstOperand = operands.pop();
                                    BinOperator someOperator = new BinOperator(firstOperand, secondOperand, popedOperator);
                                    operands.push(someOperator.run());
                                }
                                functions.pop();
                            } catch (EmptyStackException e){
                                throw new ParsingException("Wrong expression");
                            }
                        }
                        else throw new ParsingException("Wrong expression");
                        break;
                }
            }
        }
        while (!functions.empty()){
            char popedOperator = functions.pop();
            if (operands.empty()) throw new ParsingException("Wrong expression");
            double secondOperand = operands.pop();
            if (operands.empty()) throw new ParsingException("Wrong expression");
            double firstOperand = operands.pop();
            if (!functions.empty()){
                if(functions.peek() == '-'){
                    BinOperator someOperator = new BinOperator(0 - firstOperand, secondOperand, popedOperator);
                    operands.push(someOperator.run());
                    functions.pop();
                    functions.push('+');
                }else if(functions.peek() == '/'){
                    BinOperator someOperator = new BinOperator(1 / firstOperand, secondOperand, popedOperator);
                    operands.push(someOperator.run());
                    functions.pop();
                    functions.push('*');
                } else{
                    BinOperator someOperator = new BinOperator(firstOperand, secondOperand, popedOperator);
                    operands.push(someOperator.run());
                }
            } else {
                BinOperator someOperator = new BinOperator(firstOperand, secondOperand, popedOperator);
                operands.push(someOperator.run());
            }
        }
        if (operands.size() == 1) return operands.peek();
        else throw new ParsingException("Wrong expression");
    }
}
