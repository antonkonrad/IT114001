package client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
////added
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import core.BaseGamePanel;
import core.Countdown;

public class GamePanel extends BaseGamePanel implements Event {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1121202275148798015L;
	List<Player> players;
	Player myPlayer;
	String playerUsername;// caching it so we don't lose it when room is wiped
	List<Ship> ships;
	List<Attack> attacks;
	private final static Logger log = Logger.getLogger(GamePanel.class.getName());
	Dimension gameAreaSize = new Dimension();

//// added
	JPanel grid = new JPanel();
	Countdown timer;

	public void setPlayerName(String name) {
		playerUsername = name;
		if (myPlayer != null) {
			myPlayer.setName(playerUsername);
		}
	}

	@Override
	public synchronized void onClientConnect(String clientName, String message) {
		// TODO Auto-generated method stub
		System.out.println("Connected on Game Panel: " + clientName);
		boolean exists = false;
		Iterator<Player> iter = players.iterator();
		while (iter.hasNext()) {
			Player p = iter.next();
			if (p != null && p.getName().equalsIgnoreCase(clientName)) {
				exists = true;
				break;
			}
		}
		if (!exists) {
			Player p = new Player();
			p.setName(clientName);
			players.add(p);
			// want .equals here instead of ==
			// https://www.geeksforgeeks.org/difference-equals-method-java/
			if (clientName.equals(playerUsername)) {
				System.out.println("Reset myPlayer");
				myPlayer = p;
			}
		}
	}

	@Override
	public void onClientDisconnect(String clientName, String message) {
		System.out.println("Disconnected on Game Panel: " + clientName);
		Iterator<Player> iter = players.iterator();
		while (iter.hasNext()) {
			Player p = iter.next();
			if (p != null && !p.getName().equals(playerUsername) && p.getName().equalsIgnoreCase(clientName)) {
				iter.remove();
				break;
			}
		}
	}

	@Override
	public void onMessageReceive(String clientName, String message) {
		// TODO Auto-generated method stub
		System.out.println("Message on Game Panel");

	}

	@Override
	public void onChangeRoom() {
		// onResetShips();
		// onResetAttacks();
		// don't clear, since we're using iterators to loop, remove via iterator
		// players.clear();
		Iterator<Player> iter = players.iterator();
		while (iter.hasNext()) {
			iter.next();
			iter.remove();
		}

		myPlayer = null;
		System.out.println("Cleared players");
	}

	@Override
	public void awake() {
		players = new ArrayList<Player>();
		ships = new ArrayList<Ship>();
		attacks = new ArrayList<Attack>();
		GamePanel gp = this;
		// fix the loss of focus when typing in chat
		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				gp.getRootPane().grabFocus();
			}
		});
	}

	@Override
	public void start() {
		// TODO goes on server side, here for testing

	}

	@Override
	public void update() {
		// applyControls();
		localMovePlayers();
	}

	/**
	 * Gets the current state of input to apply movement to our player
	 */
	/*
	 * private void applyControls() { if (myPlayer != null) {
	 * 
	 * int x = 0, y = 0; // block input if we're sitting if (!myPlayer.isShip()) {
	 * if (KeyStates.W) { y = -1; } if (KeyStates.S) { y = 1; } if (!KeyStates.W &&
	 * !KeyStates.S) { y = 0; } if (KeyStates.A) { x = -1; } else if (KeyStates.D) {
	 * x = 1; } if (!KeyStates.A && !KeyStates.D) { x = 0; } } boolean changed =
	 * myPlayer.setDirection(x, y); if (changed) { // only send data if direction
	 * changed, otherwise we're creating unnecessary // network traffic
	 * System.out.println("Direction changed");
	 * SocketClient.INSTANCE.syncDirection(new Point(x, y)); } } }
	 */
	/**
	 * This is just an estimate/hint until we receive a position sync from the
	 * server
	 */
	private void localMovePlayers() {
		Iterator<Player> iter = players.iterator();
		while (iter.hasNext()) {
			Player p = iter.next();
			if (p != null) {
				p.move();
			}
		}
	}

	@Override
	public void lateUpdate() {
		// stuff that should happen at a slightly different time than stuff in normal
		// update()

	}

	@Override
	public synchronized void draw(Graphics g) {
		setBackground(Color.WHITE);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// drawShips(g);
		// drawAttacks(g);
		// drawPlayers(g);
		drawText(g);
		drawUI((Graphics2D) g);
		makeGrid((JPanel) grid);
	}

	/*
	 * private synchronized void drawShips(Graphics g) { Iterator<Ship> iter =
	 * ships.iterator(); while (iter.hasNext()) { Ship s = iter.next(); if (s !=
	 * null) { s.draw(g); } } }
	 * 
	 * private synchronized void drawAttacks(Graphics g) { Iterator<Attack> iter =
	 * attacks.iterator(); while (iter.hasNext()) { Attack a = iter.next(); if (a !=
	 * null) { a.draw(g); } } }
	 * 
	 * private synchronized void drawPlayers(Graphics g) { Iterator<Player> iter =
	 * players.iterator(); while (iter.hasNext()) { Player p = iter.next(); if (p !=
	 * null) { p.draw(g); } } }
	 */

	private void drawText(Graphics g) {
		g.setColor(Color.BLACK);
		g.setFont(new Font("Monospaced", Font.PLAIN, 12));
		if (myPlayer != null) {
			g.drawString("Debug MyPlayer: " + myPlayer.toString(), 10, 20);
		}

	}

	/*
	 * private void drawUI(Graphics2D g2) { Stroke oldStroke = g2.getStroke();
	 * g2.setStroke(new BasicStroke(2)); g2.drawRect(0, 0, gameAreaSize.width,
	 * gameAreaSize.height); g2.setStroke(oldStroke); }
	 */
	private void drawUI(Graphics2D g2) {
		drawTimer(g2);
		Stroke oldStroke = g2.getStroke();
		g2.setStroke(new BasicStroke(2));
		// showing border with padding
		// need to subtract the padding from left/top in addition to the desired padding
		// on bottom/right
		g2.drawRect(5, 5, gameAreaSize.width - 10, gameAreaSize.height - 10);
		g2.setStroke(oldStroke);
	}

	@Override
	public void quit() {
		log.log(Level.INFO, "GamePanel quit");
		this.removeAll();
	}

	// edit this
	private void makeGrid(JPanel grid) {
		grid.setLayout(new GridLayout(8, 8));
		grid.setBackground(Color.BLUE);
	}

	@Override
	public void attachListeners() {
		InputMap im = this.getRootPane().getInputMap();
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "up_pressed");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "up_released");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "down_pressed");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "down_released");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "left_pressed");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "left_released");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "right_pressed");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "right_released");
		// added spacebar
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "space_pressed");
		// im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true), "space_released");
		ActionMap am = this.getRootPane().getActionMap();

		am.put("up_pressed", new MoveAction(KeyEvent.VK_W, true));
		am.put("up_released", new MoveAction(KeyEvent.VK_W, false));

		am.put("down_pressed", new MoveAction(KeyEvent.VK_S, true));
		am.put("down_released", new MoveAction(KeyEvent.VK_S, false));

		am.put("left_pressed", new MoveAction(KeyEvent.VK_A, true));
		am.put("left_released", new MoveAction(KeyEvent.VK_A, false));

		am.put("right_pressed", new MoveAction(KeyEvent.VK_D, true));
		am.put("right_released", new MoveAction(KeyEvent.VK_D, false));

		// added spacebar
		am.put("space_pressed", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (myPlayer != null /* && !myPlayer.isShip() */ ) {
					if (myPlayer.getLastAction() < 0L || myPlayer.getTimeBetweenLastAction(e.getWhen()) >= 500) {
						myPlayer.setLastAction(e.getWhen());
						System.out.println("Sending action " + myPlayer.getLastAction());
						SocketClient.INSTANCE.syncPickupAttack();
					}
				}
			}

		});

		am.put("space_released", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) { // TODO Auto-generated method stub

			}

		});
	}

	@Override
	public void onSyncDirection(String clientName, Point direction) {
		Iterator<Player> iter = players.iterator();
		while (iter.hasNext()) {
			Player p = iter.next();
			if (p != null && p.getName().equalsIgnoreCase(clientName)) {
				// System.out.println("Syncing direction: " + clientName);
				p.setDirection(direction.x, direction.y);
				// System.out.println("From: " + direction);
				// System.out.println("To: " + p.getDirection());
				break;
			}
		}
	}

	@Override
	public void onSyncPosition(String clientName, Point position) {
		System.out.println("Got position for " + clientName);
		Iterator<Player> iter = players.iterator();
		while (iter.hasNext()) {
			Player p = iter.next();
			if (p != null && p.getName().equalsIgnoreCase(clientName)) {
				// System.out.println(clientName + " set " + position);
				p.setPosition(position);
				break;
			}
		}
	}

	@Override
	public void onGetRoom(String roomName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResize(Point p) {
		// TODO Auto-generated method stub
		gameAreaSize = new Dimension(p.x, p.y);
		this.setPreferredSize(gameAreaSize);
		this.setMinimumSize(gameAreaSize);
		this.setMaximumSize(gameAreaSize);
		this.setSize(gameAreaSize);
		System.out.println(this.getSize());
		this.invalidate();
		this.repaint();
	}

	@Override
	public void onSetCountdown(String message, int duration) {
		// TODO Auto-generated method stub
		if (timer != null) {
			timer.cancel();
		}
		timer = new Countdown(message, duration, (x) -> {
			System.out.println("expired");
			System.out.println(x);
		});
	}

	private void drawTimer(Graphics2D g2) {
		if (timer != null) {
			g2.setColor(Color.WHITE);
			g2.setFont(new Font("Monospaced", Font.PLAIN, 22));

			g2.drawString(timer.getTimeMessage(),
					(int) (gameAreaSize.width * .45) - (timer.getTimeMessage().length() * 6), 50);
		}
	}

	@Override
	public void onToggleLock(boolean isLocked) {
		// TODO Auto-generated method stub
		Iterator<Player> iter = players.iterator();
		while (iter.hasNext()) {
			Player p = iter.next();
			if (p != null) {
				if (p.isLocked() && !isLocked) {
					p.setDirection(0, 0);
				}
				p.setLocked(isLocked);
				if (isLocked) {
					p.setDirection(0, 0);
				}

				p.setKicked(false);
			}
		}
		if (!isLocked) {
			// here's an ok time to sort the players list in the UI
			ClientUI.Instance.resortUserList(players);
		}
	}
	/*
	 * @Override public void onGetShip(String shipName, Point position, Point
	 * dimension, String sitter) { // TODO Auto-generated method stub boolean exists
	 * = false; System.out.println("Available " + (sitter != null ? "true" :
	 * "false")); Iterator<Ship> iter = ships.iterator(); while (iter.hasNext()) {
	 * Ship s = iter.next(); if (s.getName().equalsIgnoreCase(shipName)) { exists =
	 * true; // for now will fill in player as empty player so it's !null // the
	 * player set only matters for the server Player p = s.getSitter(); if (p !=
	 * null) { p.unSit(); } if (sitter == null) {
	 * 
	 * s.setPlayer(null); } else { setShip(s, sitter); } break; } } if (!exists) {
	 * Ship s = new Ship(shipName); s.setPosition(position); s.setSize(dimension.x,
	 * dimension.y); if (sitter == null) { s.setPlayer(null); } else { setShip(s,
	 * sitter); } ships.add(s); } }
	 * 
	 * @Override public void onResetShips() { // TODO Auto-generated method stub
	 * Iterator<Ship> iter = ships.iterator(); while (iter.hasNext()) { Ship s =
	 * iter.next(); Player p = s.getSitter(); if (p != null) { p.unSit(); }
	 * s.setPlayer(null); iter.remove(); } }
	 * 
	 * void setShip(Ship s, String sitter) { Iterator<Player> piter =
	 * players.iterator(); while (piter.hasNext()) { Player p = piter.next(); if (p
	 * != null && p.getName().equalsIgnoreCase(sitter)) { s.setPlayer(p);
	 * p.setShip(s); break; } } }
	 * 
	 * void setHolder(Attack a, String holder) { Iterator<Player> piter =
	 * players.iterator(); while (piter.hasNext()) { Player p = piter.next(); if (p
	 * != null && p.getName().equalsIgnoreCase(holder)) {
	 * System.out.println("Set player holder to " + p.getName()); p.setAttack(a);
	 * a.setPlayer(p);
	 * 
	 * break; } } }
	 * 
	 * @Override public void onGetAttack(String attackName, Point position, Point
	 * dimension, String holder) {// boolean isAvailable) // { // TODO
	 * Auto-generated method stub boolean exists = false; Iterator<Attack> iter =
	 * attacks.iterator(); while (iter.hasNext()) { Attack a = iter.next(); if
	 * (a.getName().equalsIgnoreCase(attackName)) { exists = true; // for now will
	 * fill in player as empty player so it's !null // the player set only matters
	 * for the server if (holder == null) { if (!a.isAvailable()) { // remove ticket
	 * from player Attack h = a.getHolder().takeAttack(); } a.setPlayer(null); }
	 * else { setHolder(a, holder); } break; } } if (!exists) { Attack a = new
	 * Attack(attackName); a.setPosition(position); a.setSize(dimension.x,
	 * dimension.y); setHolder(a, holder); attacks.add(a); } }
	 * 
	 * @Override public void onResetAttacks() { // TODO Auto-generated method stub
	 * Iterator<Attack> iter = attacks.iterator(); while (iter.hasNext()) { Attack a
	 * = iter.next(); if (a.holder != null) { a.holder.takeAttack(); }
	 * a.setPlayer(null); iter.remove(); } }
	 * 
	 * @Override public void onGetPlace(String AttackName, Point position, Point
	 * dimension, String holder) {// boolean isAvailable) // { // TODO
	 * Auto-generated method stub boolean exists = false; Iterator<Attack> iter =
	 * attacks.iterator(); while (iter.hasNext()) { Attack a = iter.next(); if
	 * (a.getName().equalsIgnoreCase(AttackName)) { exists = true; // for now will
	 * fill in player as empty player so it's !null // the player set only matters
	 * for the server if (holder == null) { if (!a.isAvailable()) { // remove ticket
	 * from player Attack h = a.getHolder().takeAttack(); } a.setPlayer(null); }
	 * else { setHolder(a, holder); } break; } } if (!exists) { Attack a = new
	 * Attack(AttackName); a.setPosition(position); a.setSize(dimension.x,
	 * dimension.y); setHolder(a, holder); attacks.add(a); } }
	 * 
	 * @Override public void onResetPlace() { // TODO Auto-generated method stub
	 * Iterator<Attack> iter = attacks.iterator(); while (iter.hasNext()) { Attack a
	 * = iter.next(); if (a.holder != null) { a.holder.takeAttack(); }
	 * a.setPlayer(null); iter.remove(); } }
	 * 
	 * @Override public void onGetPick(String shipName, Point position, Point
	 * dimension, String sitter) { // TODO Auto-generated method stub boolean exists
	 * = false; System.out.println("Available " + (sitter != null ? "true" :
	 * "false")); Iterator<Ship> iter = ships.iterator(); while (iter.hasNext()) {
	 * Ship s = iter.next(); if (s.getName().equalsIgnoreCase(shipName)) { exists =
	 * true; // for now will fill in player as empty player so it's !null // the
	 * player set only matters for the server Player p = s.getSitter(); if (p !=
	 * null) { p.unSit(); } if (sitter == null) {
	 * 
	 * s.setPlayer(null); } else { setShip(s, sitter); } break; } } if (!exists) {
	 * Ship s = new Ship(shipName); s.setPosition(position); s.setSize(dimension.x,
	 * dimension.y); if (sitter == null) { s.setPlayer(null); } else { setShip(s,
	 * sitter); } ships.add(s); } }
	 * 
	 * @Override public void onResetPick() { // TODO Auto-generated method stub
	 * Iterator<Ship> iter = ships.iterator(); while (iter.hasNext()) { Ship s =
	 * iter.next(); Player p = s.getSitter(); if (p != null) { p.unSit(); }
	 * s.setPlayer(null); iter.remove(); } }
	 */

}
