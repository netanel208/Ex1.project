
package myMath;
/**
 * This class represents a simple "Monom" of shape a*x^b, where a is a real number and a is an integer (summed a none negative), 
 * see: https://en.wikipedia.org/wiki/Monomial 
 * The class implements function and support simple operations as: construction, value at x, derivative, add and multiply. 
 * @author Netanel
 *
 */
public class Monom implements function{

	/**
	 * The constructor of the class, which receives two parameters: coefficient and power.
	 * @param a  should be non negativ integer
	 * @param b  real number
	 */
	public Monom(double a, int b){
		if(b >= 0)
		{
			this.set_coefficient(a);
			this.set_power(b);
		}
		else
			System.out.println("the power should be non negative integer");
	}
	/**
	 * The constructor of the class, which receives Monom ot and send it parameters to the 
	 * other constructor
	 * @param ot
	 */
	public Monom(Monom ot) {
		this(ot.get_coefficient(), ot.get_power());
	}

	@Override
	/**
	 *@return   The function calculates the value of f (x) ie a*x^b and return it
	 */
	public double f(double x)
	{return this._coefficient*(Math.pow(x, this._power));}//f(x)=a*X^b

	/**
	 * 
	 * @return coefficient
	 */
	public double get_coefficient()
	{return _coefficient;}

	/**
	 * 
	 * @return power
	 */
	public int get_power()
	{return _power;}

	/**
	 * Compute connection coefficients of this with m
	 * @param m
	 */
	public void add(Monom m)
	{this.set_coefficient(this.get_coefficient()+m.get_coefficient()); } //f(x)=(a1+a2)x^b

	/**
	 * Compute the subtraction of the coefficients of this with m
	 * @param m
	 */
	public void substract(Monom m)
	{this.set_coefficient(this.get_coefficient()-m.get_coefficient()); }

	/**
	 * Compute the multiplication of two monom by multiplying the coefficients and connecting the powers
	 * @param m
	 */
	public void multiply(Monom m)
	{
		this.set_coefficient(this.get_coefficient()*m.get_coefficient());
		this.set_power(this.get_power()+m.get_power());
	}

	/**
	 * Compute the derivative of the two monoms by multiplying the coefficient by a strong then lowering the power value at 1
	 */
	public void derivative()
	{
		this.set_coefficient(this._power*this._coefficient);
		this.set_power(this._power-1);
	}

	/**
	 * Checks whether the values of this monom are equal to monom m values
	 * @param m
	 * @return true if the values equals
	 */
	public boolean equals(Monom m)
	{
		if(this.get_coefficient() == m.get_coefficient() && this.get_power() == m.get_power())
			return true;
		return false;
	}

	/**
	 * @param a
	 */
	public void set_coefficient(double a)
	{this._coefficient = a;}

	/**
	 * @param p
	 */
	public void set_power(int p) 
	{this._power = p;}

	public String toString() {
		return this._coefficient+"*"+"x^"+this._power;
	}


	//****************** Private Methods and Data *****************



	private double _coefficient; // 
	private int _power; 
}
