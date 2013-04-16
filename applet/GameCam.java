package z6;


/**
 * GOOD
 */
public class GameCam{

  private Node target;
  private Rectangle viewport;
  
  public GameCam(){
    target = null;
    setViewport(new Rectangle(0, 0, 100, 100));
  }
  
  public void setViewport(Rectangle view){
    if(view.w > 0 && view.h > 0){
      viewport = view;
    }
  }
  
  public Vec2 getTopLeft(){
    return new Vec2(viewport.x, viewport.y);
  }
  
  public Rectangle getViewport(){
    return viewport;
  }
  
  public String toString(){
    return new String(viewport.x + ", " + viewport.y);
  }
  
  public void setTarget(Node target){
    this.target = target;
  }
  
  public void update(float deltaTime){
    
    if(target != null){
      viewport.x = target.getPosition().x - viewport.w/2;
      viewport.y = target.getPosition().y - viewport.h/2;
    }
    
    viewport = Utils.clampRect(viewport, new Rectangle(0, 0, Constants.MAP_WIDTH_IN_TILES * Constants.TILE_SIZE, Constants.MAP_HEIGHT_IN_TILES * Constants.TILE_SIZE));
  }
  
  public void render(){
  }
}