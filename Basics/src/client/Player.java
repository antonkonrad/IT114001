package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;

import core.GameObject;

public class Player extends GameObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6088251166673414031L;
	Color color = Color.RED;
	Point nameOffset = new Point(0, 5);
	Attack attack = null;
	Ship ship = null;
	static boolean isReady = false;
	long lastAction = -1L;
	int time = 90;
	private int kicks = 0;
	private int locks = 0;
	private boolean isKicked;
	private boolean isLocked;

	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
		if (isLocked) {
			locks++;
		}
	}

	public boolean isLocked() {
		return isLocked;
	}

	public int getLocks() {
		return locks;
	}

	public void setKicked(boolean isKicked) {
		this.isKicked = isKicked;
		if (isKicked) {
			kicks++;
		}
	}

	public boolean isKicked() {
		return isKicked;
	}

	public int getKicks() {
		return kicks;
	}

	public void setLastAction(Long l) {
		lastAction = l;
	}

	public long getTimeBetweenLastAction(Long compare) {
		return compare - lastAction;
	}

	public long getLastAction() {
		return lastAction;
	}

	public static void setReady(boolean r) {
		isReady = r;
	}

	public boolean isReady() {
		return isReady;
	}

	/*
	 * public boolean hasAttack() { return attack != null; }
	 * 
	 * public void setAttack(Attack n) { attack = n; }
	 * 
	 * public Attack takeAttack() { if (attack == null) { return null; } Attack a =
	 * attack; attack = null; return a; }
	 * 
	 * public void setShip(Ship s) { ship = s; }
	 * 
	 * public boolean isShip() { //// isSitting() maybe return something to alert if
	 * ship is there or not (hit/ //// miss) return ship != null; }
	 * 
	 * public void unSit() { /// what to do here? ship = null; }
	 */

////added
	public void roundTimer(int time) {
		int timer = time;
		for (int i = 0; i == timer; i++) {
			if (i == timer) {
				Player.setReady(true);
			}
		}

	}

///added
	public int getRoundTimer(int time) {
		return time;
	}

	/**
	 * Gets called by the game engine to draw the current location/size
	 */

	@Override
	public boolean draw(Graphics g) { // using a boolean here so we can block drawing if isActive is false via call to
										// // super
		if (super.draw(g)) {
			g.setColor(color);
			g.fillOval(position.x, position.y, size.width, size.height);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Monospaced", Font.PLAIN, 12));
			g.drawString("Name: " + name, position.x + nameOffset.x, position.y + nameOffset.y);

		}
		return true;
	}

	@Override
	public String toString() {
		return String.format("Name: %s, p: (%d,%d), s: (%d, %d), d: (%d, %d), isAcitve: %s, Round Timer: %s ", name,
				position.x, position.y, speed.x, speed.y, direction.x, direction.y, isActive, time);
	}

}