package server;

public enum PayloadType {
	CONNECT, DISCONNECT, MESSAGE, CLEAR_PLAYERS, SYNC_DIRECTION, SYNC_POSITION, CREATE_ROOM, JOIN_ROOM, GET_ROOMS,
	SYNC_CHAIR, SYNC_TICKET, SYNC_GAME_SIZE, PICKUP_TICKET,
	///// added
	SYNC_PLACE, SYNC_PICK, SYNC_HIT, SYNC_MISS, SYNC_TURN, RECEIVE_CLICK, SET_COUNTDOWN, AWAY
}