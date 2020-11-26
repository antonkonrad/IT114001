package client;

import java.awt.Point;

public interface Event {
	void onClientConnect(String clientName, String message);

	void onClientDisconnect(String clientName, String message);

	void onMessageReceive(String clientName, String message);

	void onChangeRoom();

	void onSyncDirection(String clientName, Point direction);

	void onSyncPosition(String clientName, Point position);

	void onGetRoom(String roomName);

	void onResize(Point p);

	void onGetShip(String chairName, Point position, Point dimension, String sitter);

	void onResetShips();

	void onGetAttack(String ticketName, Point position, Point dimension, String holder);// boolean isAvailable);

	void onResetAttacks();

///////////added
	void onGetPlace(String chairName, Point position, Point dimension, String sitter);

	void onResetPlace();

	void onGetPick(String ticketName, Point position, Point dimension, String holder);// boolean isAvailable);

	void onResetPick();

}