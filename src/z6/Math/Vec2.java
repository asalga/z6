package z6.Math;

/**
 * TODO: add epsilon
 * 
 * Instead of using PVector which may cause
 * issues when porting the game, use a custom
 * vector class.
 */
public class Vec2{

	// No protection level, user can this to what whatever is appropriate.
	public float x;
	public float y;

	/*
	 * Initialize to a zero vector.
	 */
	public Vec2(){
		makeZeroVector();
	}

	public Vec2(float x, float y){
		this.x = x;
		this.y = y;
	}

	public Vec2(Vec2 src){
		this.x = src.x;
		this.y = src.y;
	}


	public static Vec2 makeZeroVector(){
		return zeroVector();
	}

	public static Vec2 zeroVector(){
		return new Vec2(0, 0);
	}

	public static Vec2 upVector(){
		return new Vec2(0, 1);
	}

	public static Vec2 rightVector(){
		return new Vec2(1, 0);
	}

	public void add(Vec2 v){
		x += v.x;
		y += v.y;
	}

	public static Vec2 add(Vec2 v1, Vec2 v2){
		return new Vec2(v1.x + v2.x, v1.y + v2.y);  
	}
	
	/**
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static Vec2 sub(Vec2 v1, Vec2 v2){
		return new Vec2(v1.x - v2.x, v1.y - v2.y);
	}

	/**
	 * Uniformly scale the vector and return it.
	 * @param s
	 */
	public Vec2 scale(float s){
		x *= s;
		y *= s;
		return this;
	}
	
	/**
	 * 
	 * @param v
	 * @param s
	 * @return
	 */
	public static Vec2 scale(Vec2 v, float s){
		return new Vec2(v.x * s, v.y * s);
	}
	
	/**
	 * 
	 * @param sx
	 * @param sy
	 * @return
	 */
	public Vec2 scale(float sx, float sy){
		x *= sx;
		y *= sy;
		return this;
	}
	
	/**
	 * Get the length of this vector.
	 * @return
	 */
	public float magnitude(){
		return (float)Math.sqrt(x*x + y*y);
	}

	public static float magnitude(Vec2 v1, Vec2 v2){
		return Vec2.sub(v1, v2).magnitude();
	}
	
	/**
	 * 
	 * @return true if both components equal zero.
	 */
	public boolean isZeroVector(){
		return (x == 0 && y == 0);
	}
	
	/*
	 * Make this vector have a length of 1.
	 * 
	 * If this is a zero vector, this method does nothing.
	 */
	public void normalize(){
		
		// Prevent division by zero
		if(isZeroVector()){
			return;
		}
		
		float mag = magnitude();
		x = x / mag;
		y = y / mag;
	}

	public Vec2 clone(){
		return new Vec2(x, y);
	}

	public String toString(){
		return new String("[" + x + "," + y + "]");
	}
}

/*
Vector2D perp(Vector2D vec){
  return new Vector2D(vec.y, -vec.x);
}
 */
