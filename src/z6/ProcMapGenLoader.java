package z6;

import processing.core.PApplet;

/*
 * Procedurally generates a map using noise
 */
public class ProcMapGenLoader implements IMapLoader{
  private Map gameMap;
  private int numRows, numCols;
  
  public ProcMapGenLoader(int numCols, int numRows){
    gameMap = new Map(numCols, numRows);
    this.numRows = numRows;
    this.numCols = numCols;
  }
  
  public int getHeight(){
	  return numRows;
  }
  
  public int getWidth(){
	  return numCols;
  }
  
  public Tile getTile(int r, int c){
	  return gameMap.getTile(r,	 c);
  }
  
  public void load(){

	Renderer.noiseSeed(0);
	
    for(int r = 0; r < Constants.MAP_HEIGHT_IN_TILES; r++){
      for(int c = 0; c < Constants.MAP_WIDTH_IN_TILES; c++){
        
    	float noiseVal = Renderer.noise(r/10f, c/10f);
    	  
        if(noiseVal > 0.6f){
          gameMap.setTile(c, r, new Tile(Constants.WATER));
          // levelData[r][c] =
          //new Tile(WATER);
        }
        else if(noiseVal > 0.3f){
         // levelData[r][c] = new Tile(GRASS);
          gameMap.setTile(c, r, new Tile(Constants.GRASS));
        }
        else{ //if(noiseVal > 0.0f){
           gameMap.setTile(c, r, new Tile(Constants.TREES));
          //levelData[r][c] = new Tile(TREES);
        }
      }
    }
  }
  
  public Map getMap(){
    return gameMap;
  }
  
}