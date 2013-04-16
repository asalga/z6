package z6;

import z6.Math.Vec2;

/*
 * This class was primarily created to handle the Turret/IGun relationship
 * along with other parent/child transformation relationships that may come up.
 */
public interface Node {
	public void addChild(Node node);

	public void setParent(Node node);

	public void setPosition(Vec2 position);

	public Vec2 getPosition();

	public void update(float deltaTime);

	public void render();
}
