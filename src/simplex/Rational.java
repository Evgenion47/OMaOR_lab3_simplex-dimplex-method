/**
 * |_|>
 * |_|>    Created by Dimyasha on 25.10.2021
 * |_|>
 */

package simplex;

import java.util.Objects;

public class Rational {
    private int sign = 1;
    private int n;
    private int d;

    //todo использовать getter's, а не обращаться к полям
    public int getSign() {
        return sign;
    }

    public int getN() {
        return n;
    }

    public int getD() {
        return d;
    }

    private void changeSign() {
        sign *= -1;
        normSign();
    }

    private void normSign() {
        if (sign >= 0) sign = 1;
        else sign = -1;
    }

    public Rational(int n, int d) throws ArithmeticException {
        if (d == 0) throw new ArithmeticException("Деление на ноль, брат. Ты так не делай.");
        int divisor = gcd(Math.abs(n), Math.abs(d));
        this.n = n / divisor;
        this.d = d / divisor;
        if (this.n < 0) {
            changeSign();
            this.n *= -1;
        }
        if (this.d < 0) {
            changeSign();
            this.d *= -1;
        }
    }

    public Rational(int n, int d, int sign) throws ArithmeticException {
        this(n, d);
        this.sign = sign;
        normSign();
    }

    private static int gcd(int n, int d) {
        return (d == 0 ? n : gcd(d, n % d));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rational rational = (Rational) o;
        return getSign() == rational.getSign() && getN() == rational.getN() && getD() == rational.getD();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSign(), getN(), getD());
    }

    public static Rational add(Rational x, Rational y) {
        return new Rational(x.sign * x.n * y.d + y.sign * y.n * x.d, x.d * y.d);
    }

    public static Rational sub(Rational x, Rational y) {
        return new Rational(x.sign * x.n * y.d - y.sign * y.n * x.d, x.d * y.d);
    }

    public static Rational mul(Rational x, Rational y) {
        return new Rational(x.n * y.n, x.d * y.d, x.sign == y.sign ? 1 : -1);
    }

    public static Rational div(Rational x, Rational y) {
        return new Rational(x.n * y.d, x.d * y.n, x.sign == y.sign ? 1 : -1);
    }


    @Override
    public String toString() {
        int up = n;
        if (sign < 0) up *= -1;
        int down = d;
        if (down < 0) {
            up *= -1;
            down *= -1;
        }
        String output = String.format("%d", up);
        if (up == 0) return output;
        if (down != 1) {
            /*output += "\n" + "-".repeat(Math.max(((Integer) up).toString().length(),
                    ((Integer) down).toString().length()));
            output += String.format("\n%d", down);*/
            output +=String.format("/%d", down);
        }
        return output;
    }

    public double toDouble() {
        return (double) sign * n / d;
    }

    public static int compare(Rational x, Rational y) {
        return Double.compare(x.toDouble(), y.toDouble());
    }
}