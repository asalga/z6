package z6.Math;

/**
 * 3D Vector
 * 
 */
public class Vec3{
	
	float x;
	float y;
	float z;
	
	public Vec3(){
		x = y = z = 0f;
	}
	
	public Vec3(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Get a copy of this vector
	 */
	public Vec3 clone(){
		return new Vec3(x, y, z);
	}
	
	/**
	 * Add the two vectors and return the result.
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static Vec3 add(Vec3 v1, Vec3 v2){
		return new Vec3(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
	}
	
	/**
	 * Add Vec3 v to this vector
	 * 
	 * Returns the current vector so user can chain the result.
	 * 
	 * @param v
	 * @return
	 */
	public Vec3 add(Vec3 v){
		this.x += v.x;
		this.y += v.y;
		this.z += v.z;
		return this;
	}
	
	/**
	 * Get a string representation of this vector.
	 */
	public String toString(){
		return new String("[" + x + ", " + y + "," + z + "]");
	}
}