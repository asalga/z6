package z6;

/**
 * 2D array of tile data.
 */
class Map{
  private Tile[][] levelData;
  
  public Tile getTile(int row, int col){
    return levelData[row][col];
  }
  
  public Map(int numTileRows, int numTileCols){
	// Renderer.println("" + numTileRows);
    levelData = new Tile[numTileCols][numTileRows];
  }
  
  public void setTile(int row, int col, Tile tile){
    // Add check
    levelData[col][row] = tile;
  }
}