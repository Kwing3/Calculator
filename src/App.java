import java.util.*;
import java.math.*;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("計算式を入力してください。\n"); //計算式の入力を求める
        String formula = sc.nextLine(); //入力された計算式をformulaに格納
        BigDecimal result = calculate(formula); //入力された計算式を計算し、結果を取得
        System.out.println("=" + result); //計算結果を出力
        sc.close();
    }

    public static BigDecimal calculate(String formula) {
        Stack<BigDecimal> numbers = new Stack<>(); //数字を格納するスタック
        Stack<Character> operators = new Stack<>(); //演算子を格納するスタック

        for (int i = 0; i < formula.length(); i++) {
            char ch = formula.charAt(i); //1文字ずつ取得
            if (ch == ' ') continue; //スペースは無視

            if (ch == '(') { //演算子スタックに追加
                operators.push(ch);
            } else if (Character.isDigit(ch)) { //Character.isDigitメソッドで判定し数字スタックに追加
                StringBuilder num = new StringBuilder(); //数字を一時保存する
                while (i < formula.length() && Character.isDigit(formula.charAt(i))) { //次の文字も数字か判定する
                    num.append(formula.charAt(i++)); //数字なら一時保存
                }
                i--; //数字ではなかったので１つ戻る
                numbers.push(new BigDecimal(num.toString())); //数字スタックに追加
            } else if (ch == ')') { //)なら、(までの計算を行う
                while (operators.peek() != '(') { //演算を実行
                    performOperation(numbers, operators);
                }
                operators.pop(); //スタックから(を取り除く
            } else {
                //もし演算子なら、優先順位に応じて計算を行う
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(ch)) {
                    performOperation(numbers, operators); //演算を実行
                }
                operators.push(ch); //演算子スタックに追加
            }
        }

        while (!operators.isEmpty()) { //演算子スタックに残っている演算子があったら、全て計算を行う
            performOperation(numbers, operators); //演算を実行
        } 

        return numbers.pop(); //数字スタックから取り除く
    }

     //演算子の優先順位を返すメソッド
    private static int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/') return 2;
        if (op == '^') return 3;
        return 0;
    }

     //演算に応じて計算を行う処理
    private static void performOperation(Stack<BigDecimal> numbers, Stack<Character> operators) {
        char op = operators.pop(); //演算子を取り出す
        BigDecimal num2 = numbers.pop(); //先に取り出したで数字をnum2
        BigDecimal num1 = numbers.pop(); //次に取り出したで数字をnum1
        BigDecimal result;
        switch (op) {
            case '+': //+の処理
                result = num1.add(num2);
                break;
            case '-': //+の処理
                result = num1.subtract(num2);
                break;
            case '*': //+の処理          
                result = num1.multiply(num2);
                break;
            case '/': //+の処理   
                if (num2.compareTo(BigDecimal.ZERO) == 0) {
                    result = BigDecimal.ZERO; //ゼロ除算の場合は0を返す
                } else {
                    result = num1.divide(num2, 10, RoundingMode.HALF_UP); //小数点以下10桁まで
                }
                break;
            case '^': //^の処理
                result = num1.pow(num2.intValue());
                break;
                default:
                System.out.println("無効な演算子です: " + op); // 未知の演算子が式に含まれている場合にエラーメッセージを表示
                result = null; 

        }
        numbers.push(result); //計算結果を数字スタックに追加
    }
}