package myMath;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class JTest {

	@Test
	void testAddToZeroPolynom() {
		Polynom p1 = new Polynom();
		Polynom_able p2 = new Polynom();
		p1.add(new Monom(1,2));
		p1.add(new Monom(1,2));
		p1.add(new Monom(2,3));
		p1.add(new Monom(1,2));
		p2.add(p1);
		Polynom pexpected = new Polynom();
		pexpected.add(new Monom(1,2));
		pexpected.add(new Monom(1,2));
		pexpected.add(new Monom(2,3));
		pexpected.add(new Monom(1,2));
		boolean actual = pexpected.equals(p2);
		boolean expected = true;
		assertEquals(expected, actual, "");
	}

}
