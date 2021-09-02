package interfaces;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Set;

public interface IObserver {
	void notifyMouseMoved(Point location);
	void notifyMousePressed(MouseEvent event);
	void notifyMouseReleased(MouseEvent event);
	void notifyMouseClicked(Point location);
	
	void notifyRightPress(Point location);
	void notifyRightRelease(Point location);
	
	void notifyLeftPress(Point location);
	void notifyLeftRelease(Point location);
	
	void notifyKeysPressed(boolean[] keyCodes);
	void notifyCharacterKeyPressed(Set<Character> keys);
}
