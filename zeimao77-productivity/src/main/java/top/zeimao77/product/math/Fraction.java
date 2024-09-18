package top.zeimao77.product.math;

import top.zeimao77.product.util.AssertUtil;
import static top.zeimao77.product.exception.ExceptionCodeDefinition.WRONG_SOURCE;

public class Fraction extends Number implements Comparable<Fraction> {

    char simplify;
    private long numerator;
    private long denominator;

    private static final long serialVersionUID = 6108874887143696465L;

    public Fraction(long numerator, long denominator) {
        AssertUtil.assertTrue(denominator != 0, WRONG_SOURCE,"分母不能为0");
        this.numerator = numerator;
        this.denominator = denominator;
        this.simplify = 0;
        this.simplify();
    }

    private long calc_greatest_common_divisor(long num1, long num2) {
        long c = num1 % num2;
        if( c == 0) return num2;
        long a = num2;
        long b = c;
        return calc_greatest_common_divisor(a,b);
    }

    public Fraction simplify() {
        if(this.simplify == 1) return this;
        long gcd = calc_greatest_common_divisor(this.numerator,this.denominator);
        this.numerator /= gcd;
        this.denominator /= gcd;
        this.simplify = 1;
        return this;
    }

    @Override
    public int compareTo(Fraction o) {
        long t1 = this.numerator * o.denominator;
        long t2 = o.numerator * this.denominator;
        if(t1 > t2) return 1;
        if(t1 < t2) return -1;
        return 0;
    }

    @Override
    public int intValue() {
        return (int) (this.numerator / this.denominator);
    }

    @Override
    public long longValue() {
        return (int) (this.numerator / this.denominator);
    }

    @Override
    public float floatValue() {
        return (float) this.numerator / this.denominator;
    }

    @Override
    public double doubleValue() {
        return (double) this.numerator / this.denominator;
    }

    public Fraction add(Fraction fraction) {
       return new Fraction(this.numerator * fraction.denominator + fraction.numerator * this.denominator
                , this.denominator * fraction.denominator).simplify();
    }

    public Fraction subtract(Fraction fraction) {
        return new Fraction(this.numerator * fraction.denominator - fraction.numerator * this.denominator
                , this.denominator * fraction.denominator).simplify();
    }

    public Fraction multiply(Fraction fraction) {
       return new Fraction(this.numerator * fraction.numerator, this.denominator * fraction.denominator).simplify();
    }

    public Fraction divide(Fraction fraction) {
        if(fraction.numerator == 0)
            return new Fraction(0,1);
       return new Fraction(this.numerator * fraction.denominator, this.denominator * fraction.numerator).simplify();
    }

    public Fraction negate() {
        Fraction r = new Fraction(-this.numerator, this.denominator);
        return r;
    }

    @Override
    public String toString() {
        return String.format("%d/%d",this.numerator,this.denominator);
    }
}
