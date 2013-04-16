package z6;

/*
 * A common interface for all loaders that return a Map
 * A Map is simply a 2D grid of a set of tiles/sprites
 * It can be used to represent a level map, collision map, etc.
 */
 
public interface IMapLoader{
  Map getMap();
  void load();
}