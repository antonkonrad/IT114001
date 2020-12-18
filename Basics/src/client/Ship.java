package client;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;

import core.GameObject;

//Ship
public class Ship extends GameObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6088251166673414031L;
	Color color = Color.LIGHT_GRAY;
	Point nameOffset = new Point(-10, -5);
	Player seated = null;

	// Ship
	public Ship(String name) {
		setName(name);
	}

	public boolean isAvailable() {
		return seated == null;
	}

	public void setPlayer(Player p) {
		seated = p;
	}

	public Player getSitter() { /// what to do here?
		return seated;
	}

	public String getSitterName() {
		if (seated == null) {
			return null;
		}
		return seated.getName();
	}

	/**
	 * Gets called by the game engine to draw the current location/size
	 */
	/*
	 * @Override public boolean draw(Graphics g) { // using a boolean here so we can
	 * block drawing if isActive is false via call to // super if (super.draw(g)) {
	 * g.setColor(color); g.drawRect(position.x, position.y, size.width,
	 * size.height); g.setColor(Color.WHITE); g.setFont(new Font("Monospaced",
	 * Font.PLAIN, 12)); g.drawString((isAvailable() ? "Available" : "Taken: " +
	 * getSitterName()), position.x + nameOffset.x, position.y + nameOffset.y); }
	 * return true; }
	 */

	@Override
	public String toString() {
		return String.format("Name: %s, p: (%d,%d), s: (%d, %d), d: (%d, %d), isAcitve: %s", name, position.x,
				position.y, speed.x, speed.y, direction.x, direction.y, isActive);
	}
}