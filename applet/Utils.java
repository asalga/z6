package z6;

public final class Utils{
	
	private Utils(){}
	
	/**
	 * Add check if one r2 fits inside r1
	 */
	public static Rectangle clampRect(Rectangle r1, Rectangle r2){
	  Rectangle clampedRect = new Rectangle(r1.x, r1.y, r1.w, r1.h);
	  
	  if(r1.x <= r2.x){
	    clampedRect.x = r2.x;
	  }
	  
	  if(r1.y <= r2.y){
	    clampedRect.y = r2.y;
	  }
	  
	  if(r1.x + r1.w >= r2.x + r2.w){
	    clampedRect.x = r2.x + r2.w - r1.w;
	  }
	  
	  if(r1.y + r1.h >= r2.y + r2.h){
	    clampedRect.y = r2.y + r2.h - r1.h;
	  }
	  
	  return clampedRect;
	  //if(r1.x + r1.w >= r2.w){
	    //r1.x = r2.x+r2.w;
	  //}
	}
	
	/*
	 */
	public static boolean isPointInBox(Vec2 p, Rectangle b){
	  if(p.x >= b.x && p.x <= b.x + b.w &&
	     p.y >= b.y && p.y <= b.y + b.h){
	       return true;
	     }
	  return false;
	}
	
	/**
	  Returns true if b1 touches box2.
	  @param b1
	  @param b2
	*/
	public static boolean testCollision(Rectangle b1, Rectangle b2){  
	  // Check if any of box2's corners are inside box1
	  if( isPointInBox(new Vec2(b2.x,        b2.y), b1) ||
	      isPointInBox(new Vec2(b2.x + b2.w, b2.y), b1) ||
	      isPointInBox(new Vec2(b2.x,        b2.y + b2.h), b1) ||
	      isPointInBox(new Vec2(b2.x + b2.w, b2.y + b2.h), b1) ||
	
	      // Check if any of box1's corners are inside box2
	      isPointInBox(new Vec2(b1.x,        b1.y), b2) ||
	      isPointInBox(new Vec2(b1.x + b1.w, b1.y), b2) ||
	      isPointInBox(new Vec2(b1.x,        b1.y + b1.h), b2) ||
	      isPointInBox(new Vec2(b1.x + b1.w, b1.y + b1.h), b2)){
	        return true;
	    }
	  
	  return false;
	}
	
	/*
	 * Get a random number from minVal to maxVal inclusive
	 */
	public static int getRandomInt(int minVal, int maxVal){
	  float scaleFloat = (float)Math.random();
	  return minVal + (int)(scaleFloat * (maxVal-minVal+1));
	}
	
	/*
	 * 
	 */
	public static float getRandomFloat(float minVal, float maxVal){
      float scaleFloat = (float)Math.random();
	  return minVal + (scaleFloat * (maxVal-minVal+1));
	}
	
	/*
	 * Add commas to large numbers to make them
	 * easier to read
	 */
	public static String addCommas(int value){
	  String withCommas = "";
	  String valueStr = "" + value;
	
	  int counter = valueStr.length()-1;
	  int i = 1;
	  for(; counter >= 0; counter--, i++){
	    withCommas += valueStr.charAt(counter);
	    
	    if(i%3 == 0 && counter > 0){
	      withCommas += ",";
	    }
	  }
	  
	  String correctOrder = "";
	  for(i = 0; i < withCommas.length(); i++){
	    correctOrder += withCommas.charAt(withCommas.length()-1-i);
	  }
	
	  return correctOrder;
	}	
}
