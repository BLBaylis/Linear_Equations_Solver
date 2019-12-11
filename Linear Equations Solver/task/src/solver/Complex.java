package solver;

import java.text.DecimalFormat;
import java.util.Arrays;

public class Complex {
    static final Complex ZERO = new Complex(0, 0);
    static final Complex ONE = new Complex(1, 0);
    private double real;
    private double imag;

    private Complex(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }

    Complex(String str) {
        this.real = Complex.parseReal(str);
        this.imag = Complex.parseImaginary(str);
    }

    private double getReal() {
        return this.real;
    }

    private double getImag() {
        return this.imag;
    }

    private static Complex conjugate(Complex number) {
        return new Complex(number.getReal(), -number.getImag());
    }

    boolean equals(Complex comparison) {
        return comparison.getReal() == real && comparison.getImag() == imag;
    }

    private static double parseReal(String str) {
        if (!Complex.isImaginaryNumber(str)) {
            return Double.parseDouble(str);
        }
        boolean hasOperator = str.contains("+") || str.contains("-");
        if (!hasOperator) {
            return 0;
        }
        String operator = str.contains("-") ? "-" : "[+]";
        String real = str.split(operator)[0];
        return "".equals(real) ? 0 : Double.parseDouble(real);
    }

    private static double parseImaginary(String str) {
        if (!Complex.isImaginaryNumber(str)) {
            return 0;
        }
        boolean hasMinus = str.contains("-");
        boolean hasOperator = str.contains("+") || hasMinus;
        if (!hasOperator) {
            return "i".equals(str) ? 1 : Double.parseDouble(str.substring(0, str.length() - 1));
        }
        String operator = hasMinus ? "-" : "+";
        int operatorIndex = str.indexOf(operator);
        String imag = str.substring(operatorIndex);
        if (imag.length() < 3) {
            imag = imag.replace("i", "1");
        } else {
            imag = imag.replace("i", "");
        }
        return Double.parseDouble(imag);

    }

    private static boolean isImaginaryNumber(String str) {
        return str.contains("i");
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("0.#####");
        String realStr = df.format(this.real);
        String imagStr = df.format(this.imag);
        imagStr = this.imag < 0 ? imagStr : "+" + imagStr;
        return this.imag == 0 ? realStr : realStr + imagStr + "i";
    }

    Complex subtract(Complex subtrahend) {
        double subtrahendReal = subtrahend.getReal();
        double subtrahendImag = subtrahend.getImag();
        return new Complex(this.real - subtrahendReal, this.imag - subtrahendImag);
    }

    Complex multiply(Complex multiplicand) {
        double multiplicandReal = multiplicand.getReal();
        double multiplicandImag = multiplicand.getImag();
        double newReal = this.real * multiplicandReal + -1 * this.imag * multiplicandImag;
        double newImag = this.real * multiplicandImag + this.imag * multiplicandReal;
        return new Complex(newReal, newImag);
    }

    Complex divide(Complex divisor) {
        Complex conjugate = conjugate(divisor);
        Complex numerator = this.multiply(conjugate);
        Complex denominator = divisor.multiply(conjugate);
        if (denominator.getReal() == 0) {
            throw new ArithmeticException();
        }
        double newReal = numerator.getReal()/denominator.getReal();
        double newImag = numerator.getImag()/denominator.getReal();
        return new Complex(newReal, newImag);
    }

}
