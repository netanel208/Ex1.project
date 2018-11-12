package myMath;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Predicate;

import myMath.Monom;
/**
 * This class represents a Polynom with add, multiply functionality, it also should support the following:
 * 1. Riemann's Integral: https://en.wikipedia.org/wiki/Riemann_integral
 * 2. Finding a numerical value between two values (currently support root only f(x)=0).
 * 3. Derivative
 * 
 * @author Netanel
 *
 */
public class Polynom implements Polynom_able{
	private ArrayList<Monom> MonomsCollection;     //Polynom(Monoms collection)
	private final Monom_Comperator cmByPower = new Monom_Comperator();  //comperator- compare monom by power

	/**
	 * Class constructor creating a data structure (ArrayList) 
	 * that will hold objects Monom.
	 */
	public Polynom()                               //Init() - constructor
	{MonomsCollection = new ArrayList<Monom>();}

	/**
	 * Class constructor accepts a String type variable and converts it to Polynom object.
	 * <p>
	 * Divides the string into segments by "split",
	 * each cell in the structure contains a string that is converted to a Monom.
	 * 
	 * First, before the constructor converts the string in the cell
	 *  in which it is currently located, 
	 *  it sends the string to the IsItTrueExpression(String str) function
	 *  to check whether the string can be converted to Monom.
	 *  If the string is invalid -
	 *  turn the function Error() which returns an error message.
	 *  Each Monom insertion after a conversion
	 *  is performed by the class's add(Monom m) function.
	 * Finally, if all is normal,
	 *  sorting and ordering of the data structure is carried out by the Comperator variable.
	 *  @param polynom      the string that the function need to convert to Polynom
	 */
	
	
	public Polynom (String str) { 
		this();
		str= str.replace("-", "+-");
		String [] subStr = str.split("\\+"); // split the string by "+" or "-"
		for (int i = 0; i < subStr.length; i++) {
			if (!subStr[i].equals("")) 
			this.add(new Monom(subStr[i]));


		}
	}

	/**
	 * Add m1 to this Polynom.
	 * <p>
	 * The function accepts as a parameter the Monom that must be added to this Polynom.
	 * First go over the elements of the polynom and compare the powers of each monom.
	 * If there is an organ that is equal in its power to the monom m1 holder, 
	 * it is necessary to connect their coefficients,
	 * this is done by the add(monom m) function which is in Monom class.
	 * Otherwise, perform the data structure add(Monom m) of ArrayList is added
	 * Finally arrange and sort.
	 * @param m1   Monom that the function should add to polynom
	 */
	@Override
	public void add(Monom m1)
	{
		if(!this.isZero())
		{
			//add if the powers similar
			boolean found = false;                              //found similar powers       
			for(int i=0 ; i<this.MonomsCollection.size() ; i++)
			{
				if(this.MonomsCollection.get(i).get_power() == m1.get_power())
				{
					found =true;
					this.MonomsCollection.get(i).add(m1);
					break;                                      //if found similar powers - end
				}
			}
			if(!found)
			{
				this.MonomsCollection.add(m1);       //add of ArrayList
			}
		}
		else
		{
			this.MonomsCollection.add(m1);           //add of ArrayList
		}
		this.MonomsCollection.sort(cmByPower);       //sort use comperator
		DeleteZeroMonoms();                          //delete : 0*x^b                
	}

	/**
	 * Add p1 to this Polynom.
	 * <p>
	 * The function accepts Polynom_able and adds it (if it is an instance of Polynom) to this polynomial.
	 * By using iteretor(), we go through all the polynomials we have received in the function
	 * and perform the function add(Monom m) of the class to each monom.
	 * @param p1   the Polynom_able that function need add to this polynom
	 */
	@Override
	public void add(Polynom_able p1)
	{ 
		if(p1 instanceof Polynom)
		{
			Polynom pol = (Polynom)p1;    
			pol.DeleteZeroMonoms();                //delete : 0*x^b
			Iterator<Monom> polIter = pol.iteretor();
			while(polIter.hasNext())
			{
				Monom polMon = polIter.next();
				this.add(new Monom(polMon));
			}
		}
	}
	
	/**
	 * The function returns the value y = f (x) where f (x) is this polynom
	 * <p>
	 * There is a loop where each iteration calculates the value of the monom
	 * with the x that the user receives.
	 * @param x     x is value that user insert to find y=f(x)
	 * @return      return the value of y=f(x)
	 */
	@Override
	public double f(double x) {
		double ans = 0;
		if(!this.isZero())
		{
			for(int i=0 ; i<this.MonomsCollection.size() ; i++)
			{
				ans += this.MonomsCollection.get(i).f(x);
			}
		}
		return ans;
	}
	/**
	 * Subtract p1 from this Polynom
	 * <p>
	 * A function that subtracts between two polynoms 
	 * and uses an auxiliary function that subtracts a single mono from a polynom.
	 * @param p1     the function will substract p1(if it Polynom object) from this polynom 
	 */
	@Override
	public void substract(Polynom_able p1) {
		if(p1 instanceof Polynom)
		{
			Polynom pol = (Polynom)p1;
			pol.DeleteZeroMonoms();                  //delete : 0*x^b
			if(!pol.isZero())
			{
				pol.MonomsCollection.sort(cmByPower);//sort of arrayList uses Comperator
				Iterator<Monom> polIter = pol.iteretor();
				while(polIter.hasNext())
				{
					Monom polMon = polIter.next();
					this.substract(new Monom(polMon));          //substract(Monom m) of Polynom 
				}
			}
		}
	}

	
	/**
	 * Multiply this Polynom by p1.
	 * The function accepts Polynom_able and multiplies this polynomial in each monom of the polynomial it receives in the function.
	 * <p>
	 * First it deletes monom which is 0, and creates a new polynomial that is formatted by the empty constructor of the class.
	 * Then the function checks whether the two polynomials are not null ie not zero The function will sort
	 * the polynomial obtained by the sorting function that uses compertor.
	 * Then by loop will run on all monom of the polynomial received by the function and will multiply between this polynomial and monom. 
	 * The doubling between monom and polynom is performed by the auxiliary function.
	 * Finally, if the two polynomials were zero then in this polynomial zero would be the result of the polynomial multiplication.
	 * @param p1
	 */
	@Override
	public void multiply(Polynom_able p1) {
		if(p1 instanceof Polynom)
		{
			Polynom pol =(Polynom)p1;
			pol.DeleteZeroMonoms();       //delete : 0*x^b
			this.DeleteZeroMonoms();      //delete : 0*x^b
			Polynom res = new Polynom();  //new polynom
			if(!this.isZero() && !pol.isZero())
			{
				pol.MonomsCollection.sort(cmByPower);  //sort by powers before to get order 
				for(int i=0 ; i<pol.MonomsCollection.size() ; i++)
				{
					Polynom temp = this.multiply(pol.MonomsCollection.get(i)); 
					res.add(temp); //add(Polynom)
				}
			}
			this.MonomsCollection = res.MonomsCollection;
			this.MonomsCollection.sort(cmByPower);       //sort use comperator
			DeleteZeroMonoms();                          //delete : 0*x^b
		}	
	}

	
	/**
	 * Test if this Polynom is logically equals to p1.
	 * The function accepts a polynomial and returns the truth if the polynomial it receives
	 * and this polynomial are logically equal, ie if the values of the coefficients and the powers equals are equal.
	 * <p>
	 * The function first sorts and deletes a monom that is zero and initializes a boolean variable that is false.
	 * The function first checks whether the sizes of the two polynomials are equal and then continues, otherwise a lie.
	 * Then, the loop executes a loop that checks monom against monom of each of the polynomials accordingly and performs an action on the Monoms that belongs to Monom.
	 * If the function finds that the monoms does not equal then the ans will lie and exit from the loop.
	 * @param p1
	 * @return true iff this pulynom represents the same function ans p1
	 */
	@Override
	public boolean equals(Polynom_able p1) {
		boolean ans = false;
		if(p1 instanceof Polynom)
		{
			Polynom pol =(Polynom)p1;
			pol.MonomsCollection.sort(cmByPower);
			pol.DeleteZeroMonoms();                   //delete : 0*x^b
			if(this.MonomsCollection.size() == pol.MonomsCollection.size())
			{
				for(int i=0 ; i<this.MonomsCollection.size() ; i++)
				{
					Monom myMonom = this.MonomsCollection.get(i);
					Monom othMonom = pol.MonomsCollection.get(i);
					ans = myMonom.equals(othMonom);
					if(!ans)
						break;
				}
				if(this.isZero() && pol.isZero())
					ans=true;
			}
		}
		return ans;
	}

	
	/**
	 * Test if this is the Zero Polynom
	 * Meaning if there are no null elements that returns true.
	 * @return
	 */
	@Override
	public boolean isZero() {
		if(this.MonomsCollection.size() == 0)
			return true;
		return false;
	}

	
	/**
	 * Finding a numerical value between two values (currently support root only f(x)=0).
	 * <p>
	 * The function uses the crossover rule and the intermediate value theorem, 
	 * the function receives a start and end value of the field in which the function will work and
	 * also accepts an eps value that determines the deviation error.
	 * First, the function determines the value f (x) = y of x0 and x1 and then checks whether f (x0) * f (x1) 
	 * is less than zero if this does not mean that the function is either above the X axis or below the axis.
	 * If so, then the point is to cut the x-axis according to the intermediate value theorem.
	 * The stop condition of the function is if the deltaX and deltaY intervals are smaller than eps 
	 * and if(f(x0)*f(midx) < 0).
	 * if f(x0)*f(midx)<0 the recursive call to function root with values x0,midx(x0+x1/2),eps it mean to find root between this values.
	 * otherwise the function perform recursive call to function root with values midx(x0+x1/2),x1,eps it mean to find root between this values.
	 * 
	 * @param x0 starting point
	 * @param x1 end point
	 * @param eps step (positive) value
	 * @return
	 */
	@Override
	public double root(double x0, double x1, double eps) {
		double y0 = this.f(x0);
		double y1 = this.f(x1);
		if(y0*y1>0)
		{
			System.out.println("At first the functions should be opposite such that: f(x0)*f(x1)<=0");
			double err = Double.POSITIVE_INFINITY;
			return err;
		}
		else
		{
			double deltaX = Math.abs(x0-x1);   //|x0-x1| the interval between x0 to x1
			double deltaY = Math.abs(y0-y1);   //|y0-y1|
			if(deltaX>eps || deltaY>eps)
			{
				double midX = (x0+x1)/2;
				if(y0*this.f(midX) < 0)        //if(f(x0)*f(midx) < 0)
					return root(x0, midX, eps);// finding root in interval between x0 to midX
				else
					return root(midX, x1, eps);// finding root in interval between midX to x1
			}
			return x0;   //return a approximate value of root(f(x)=0)
		}
	}

	
	/**
	 * Create a deep copy of this Polynom.
	 * <p>
	 * The function creates a new polynom which will contain a collection
	 * of monoms that are actually a deep copy of the polynomial that triggered the function.
	 * By the add function in the polynom class add each monom to a new polynomial.
	 * @return 
	 */
	@Override
	public Polynom_able copy() { //creat new object and enter this variable to this object
		Polynom_able pol = new Polynom();
		for(int i=0 ; i<this.MonomsCollection.size() ; i++)
		{
			Monom m = this.MonomsCollection.get(i);
			pol.add(new Monom(m));
		}
		return pol;
	}

	
	/**
	 * Compute a new Polynom which is the derivative of this Polynom.
	 * <p>
	 * The function runs in a loop on a copy of this polynomial and checks whether there is
	 * a monom whose holding is zero because the derivative of such monom is 0.
	 * Otherwise the function triggers the derivative function on each monom of the Monom class.
	 * @return
	 */
	@Override
	public Polynom_able derivative() {
		Polynom_able p = this.copy();
		Polynom ans = (Polynom)p;
		if(!ans.isZero())
		{
			for(int i=0 ; i<ans.MonomsCollection.size() ; i++)
			{
				if(ans.MonomsCollection.get(i).get_power() > 0) //if(the power of X != 0) because (a*X^0)'=0
				{
					ans.MonomsCollection.get(i).derivative();
				}
				else
				{
					ans.MonomsCollection.get(i).set_coefficient(0);
				}
			}
			ans.DeleteZeroMonoms();
			ans.MonomsCollection.sort(cmByPower);
		}
		return ans;
	}

	
	/**
	 * Compute Riemann's Integral over this Polynom starting from x0, till x1 using eps size steps.
	 * <p>
	 * This function computes space by Riemann's integral, which aims to sum areas of rectangles whose width is eps.
	 * The function accepts the edges where it needs to calculate the area and width of each rectangle eps.
	 * First, the function calculates the distance between the defined edges.
	 * And then runs in the loop where it calculates the height of each rectangle by the value of the X point 
	 * that is located between the vertices of the rectangle.
	 * If the Y value of all these points is above the X axis, the area of the rectangle must be calculated,
	 * otherwise the function will advance.
	 * @return double     the approximated area above the x-axis below this Polynom and between the [x0,x1] range.
	 */
	@Override
	public double area(double x0, double x1, double eps) {
		double area = 0;
		double distancePass = 0;
		double Xs = x0;                                        //x_source
		double Xd = Xs + eps;                                  //x_destination
		double deltaX = Math.abs(x1-x0);
		while(distancePass < deltaX)
		{                   
			double midX = (Xd+Xs)/2;                           //midX belongs to [Xs,Xd]
			if(this.f(midX)>0 && this.f(Xs)>0 && this.f(Xd)>0) //if the function above the x-axis continue
			{
				double height = this.f(midX);                  //height = f(midX)
				area += height*eps;                            //rectangle area = height*base
			}
			Xs += eps;                          
			Xd += eps;
			distancePass += eps;
		}
		return area;
	}

	
	/**
	 *  @return an Iterator (of Monoms) over this Polynom
	 */
	@Override
	public Iterator<Monom> iteretor() {
		return this.MonomsCollection.iterator();
	}
	/**
	 * A function for the subtraction function in the class.
	 * <p>
	 * The function initializes a false Boolean variable.
	 * The function runs in a loop on this polynomial and checks whether there is a monom with the same power of m1
	 * so you can subtract between them by activating the subtraction function in the Monom class.
	 * If it is able to find a similar and missing power, change the value of the Boolean variable to true and finish.
	 * Otherwise, if it does not find a similar strength, 
	 * multiply the monom coefficient by 1 and add it to the polynomial by the add function.
	 * @param m1
	 */
	private void substract(Monom m1)
	{
		boolean found = false;
		//substract if the powers similar       
		for(int i=0 ; i<this.MonomsCollection.size() ; i++)
		{
			if(this.MonomsCollection.get(i).get_power() == m1.get_power())
			{
				this.MonomsCollection.get(i).substract(m1);
				found = true;
				break;
			}
		}
		//substract if the powers not similar
		if(!found)
		{
			m1.set_coefficient((-1)*m1.get_coefficient());
			this.add(m1);
		}
		this.MonomsCollection.sort(cmByPower);       //sort use comperator
		DeleteZeroMonoms();                          //delete : 0*x^b
	}

	/**
	 * Auxiliary function multiplication function departments.
	 * The function multiplies this polynom in the monom it received.
	 * The function passes through the loop and uses the multiplication function of the Monom class, multiplying each monom in this polynom.
	 * @param m
	 * @return  the polynom after multiply
	 */
	private Polynom multiply(Monom m) { //return Polynom
		Polynom ans = new Polynom();
		for(int i=0 ; i<this.MonomsCollection.size() ; i++)
		{
			Monom temp = new Monom(m);
			temp.multiply(this.MonomsCollection.get(i)); //multiply(Monom) - of Monom
			ans.add(temp);
		}
		return ans;
	}

	/**
	 * 
	 * print error in screen
	 */
	private void Error()
	{System.out.println("Error: invalid input");}

	/**
	 * The constructor function The Polynom (String) function accepts a string that is supposed to represent a valid monom,
	 * the function will check the integrity of the string to be worthy of being monom.
	 * <p>
	 * The function uses a split of strings and divides the string into two parts that should contain valid numbers.
	 * One side contains a string that must be a double number and the other side (the second cell) 
	 * contains a string that must be a non-int int number.
	 * There should be no spaces in the string. The size of the X should be small. Need to insert a number, multiplication, X, power, integer non-negative number.
	 * The function uses the IsNumber auxiliary function to check whether a string is really a number.
	 * @param str
	 * @return true if the string is valid to convert to monom
	 */
	private boolean IsItTrueExpression(String str)
	{
		//find the Asterisk - if it exist
		if(!str.contains("*x^"))
			return false;
		else
		{
			String[] s = str.split("\\*x\\^");
			if(s.length > 2)
				return false;
			//check left side of monom (___.___)
			int count=0;
			int indexOf=Integer.MIN_VALUE;
			for(int i=0 ; i<s[0].length() ;i++)
			{
				if(s[0].charAt(i) == ' ')
				{
					System.out.println("Please enter string without spaces");
					return false;
				}
				if(s[0].charAt(i) == '.')
				{
					indexOf = i;
					count++;
				}
			}
			if(count > 1)  //there is just 1 point(.)
				return false;
			if(count == 1)
			{
				boolean flag=false;
				for(int i=0 ; i<indexOf ; i++)
				{
					if(s[0].charAt(0) == '-' && !flag)
					{
						flag = true;
						continue;
					}
					if(!IsNumber(s[0].charAt(i)))
						return false;
				}
				for(int i=indexOf+1 ; i<s[0].length() ; i++)
				{
					if(!IsNumber(s[0].charAt(i)))
						return false;
				}
			}
			if(count == 0)
			{
				boolean flag2=false;
				for(int i=0 ; i<s[0].length() ; i++)
				{
					if(s[0].charAt(0) == '-' && !flag2)
					{
						flag2 = true;
						continue;
					}
					if(!IsNumber(s[0].charAt(i)))
						return false;
				}
			}
			//check right side of monom 
			for(int i=0 ; i<s[1].length() ; i++)
			{
				if(s[1].charAt(i) == ' ')
				{
					System.out.println("Please enter string without spaces");
					return false;
				}
				if(!IsNumber(s[1].charAt(i)))
					return false;
			}
			return true;
		}
	}

	/**
	 * @param c
	 * @return true if c is number
	 */
	private boolean IsNumber(char c)
	{
		for(int i=48 ; i<58 ; i++) //ascii value
		{
			if(c == i)
				return true;
		}
		return false;
	}
	/**
	 * The function deletes an extra monom with coefficient 0.
	 */
	private void DeleteZeroMonoms()
	{
		if(!this.isZero())
		{
			for(int i=0 ; i<this.MonomsCollection.size() ; i++)
			{
				if(this.MonomsCollection.get(i).get_coefficient() == 0)
					this.MonomsCollection.remove(i);
			}
		}
	}

	
	public String toString()
	{
		//"f(x) =";
		String ans = "";
		if(this.isZero())
			return ans + "0";
		Iterator<Monom> itr = this.MonomsCollection.iterator();
		while(itr.hasNext())
		{
			Monom m = itr.next();
			if(itr.hasNext())
				ans = ans + m.toString() + "+";
			else
				ans = ans + m.toString();
		}
		return ans;
	}



}
