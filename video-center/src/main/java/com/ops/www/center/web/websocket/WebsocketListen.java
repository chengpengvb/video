package com.ops.www.center.web.websocket;

import javax.websocket.Session;

public interface WebsocketListen {

	void open(Session session, String uuid);

	void close(Session session, String uuid);
}
