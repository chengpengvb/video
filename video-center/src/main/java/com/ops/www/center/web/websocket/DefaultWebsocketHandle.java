package com.ops.www.center.web.websocket;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.ops.www.common.util.RecordHandler;

/**
 *
 *
 * @author cp
 * @version 创建时间：2020年1月6日 上午11:03:32
 */
public abstract class DefaultWebsocketHandle implements WebSocketHandle {

	protected Logger logger = LogManager.getLogger();

	private static Map<String, Session> id2session = new ConcurrentHashMap<>();

	private static RecordHandler recordHandler;

	public static Map<String, Session> selectSessions() {
		return id2session;
	}

	@OnOpen
	public void onOpen(Session session, @PathParam("uuid") String uuid) {
		id2session.put(uuid, session);
		logger.debug("WebSocket onOpen:{}.", session.getId());
		openHandle(session, uuid);
	}

	@OnError
	public void onError(Session session, Throwable thr) {
		onClose(session);
		logger.warn("WebSocket Error Close:{}.", session.getId());
	}

	@OnClose
	public void onClose(Session session) {
		logger.debug("WebSocket onClose:{}.", session.getId());
		closeHandle(session);
		String uuid = getUUID(session);
		if (uuid != null) {
			id2session.remove(uuid);
		}
	}

	public static String getUUID(Session session) {
		Set<String> keySet = id2session.keySet();
		for (String uuid : keySet) {
			Session temp = id2session.get(uuid);
			if (temp.getId().equals(session.getId())) {
				return uuid;
			}
		}
		return null;
	}

	public static Session getSession(String uuid) {
		return id2session.get(uuid);
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		onMessageHandle(message, session);
	}

	public synchronized void sendTextMsg(Session session, String message) {
		try {
			session.getBasicRemote().sendText(message);
		} catch (IOException e) {
			recordHandler.handleError(e);
		}
	}

	@Autowired
	public static void setRecordHandler(RecordHandler recordHandler) {
		DefaultWebsocketHandle.recordHandler = recordHandler;
	}

}
