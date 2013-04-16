package z6;
import z6.Math.Vec2;

/**
 * 
 * 
 */
public interface IBroadcaster{
	public void addSubscriber(ISubscriber subscriber);
	public void removeSubscriber(ISubscriber subscriber);
	public void notifySubscribers();
}