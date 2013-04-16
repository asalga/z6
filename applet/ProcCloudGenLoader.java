package z6;
/*
 * Procedurally generates a map using noise
 */
public class ProcCloudGenLoader implements IMapLoader{
  private Map gameMap;
  
  public ProcCloudGenLoader(int numCols, int numRows){
    gameMap = new Map(numCols, numRows);
  }
  
  public void load(){
    //Renderer.noiseSeed(Renderer.millis()+ Utils.getRandomInt(10, 230));//(int)random(10,223));
    for(int r = 0; r < Constants.MAP_HEIGHT_IN_TILES; r++){
      for(int c = 0; c < Constants.MAP_WIDTH_IN_TILES; c++){
        float noiseVal = Renderer.noise(r/10f, c/10f);
        if(noiseVal > 0.6){
          gameMap.setTile(c, r, new Tile(Constants.CLOUD1));
          // levelData[r][c] =
          //new Tile(WATER);
        }
        else if(noiseVal > 0.3){
         // levelData[r][c] = new Tile(GRASS);
          gameMap.setTile(c, r, null);//new Tile(CLOUD2));
        }
        else{ //if(noiseVal > 0.0f){
           gameMap.setTile(c, r, null);
          //levelData[r][c] = new Tile(TREES);
        }
      }
    }
  }
  
  public Map getMap(){
    return gameMap;
  }
  
}