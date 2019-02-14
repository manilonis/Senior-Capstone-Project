/**
 * Michael E Anilonis
 * Feb 14, 2019
 */
package generalHelpers;

public class Tuple<X,Y> {
	private X x;
	private Y y;
	public Tuple(X xx, Y yy) {
		x = xx;
		y = yy;
	}
	
	public X getX() {
		return x;
	}
	
	public Y getY() {
		return y;
	}
	
	@Override
	public String toString() {
		return x.toString() + " " + y.toString();
	}
}
