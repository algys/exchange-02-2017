package com.cyclic.models.game;

import com.cyclic.LOG;
import com.cyclic.configs.Enums;
import com.cyclic.models.game.net.toclient.ConnectionError;
import com.cyclic.models.game.net.toclient.RNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import static com.cyclic.configs.Enums.DisconnectReason.DISCONNECT_REASON_API_HACKER;

/**
 * Created by serych on 01.04.17.
 */
public class Player {

    private String nickname;
    private long id;
    private long units;
    private int beginX = 0;
    private int beginY = 0;
    private transient Room room = null;
    private transient WebSocketSession webSocketSession;
    private transient Gson gson;
    private transient Node mainNode;
    private transient HashMap<Node, HashSet<Node>> nodesMap;
    private boolean readyForGameStart = false;


    public Player(WebSocketSession webSocketSession, String nickname, long id) {
        this.webSocketSession = webSocketSession;
        this.gson = new GsonBuilder().create();
        this.nickname = nickname;
        this.id = id;
        this.nodesMap = new HashMap<>();
    }

    public String getNickname() {
        return nickname;
    }

    public long getId() {
        return id;
    }

    public long getUnits() {
        return units;
    }

    public void setUnits(long units) {
        this.units = units;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void sendString(String data) {
        try {
            webSocketSession.sendMessage(new TextMessage(data));
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    public void sendDatatype(String datatype) {
        sendString("{datatype:" + datatype + "}");
    }

    public WebSocketSession getWebSocketSession() {
        return webSocketSession;
    }

    public boolean isReadyForGameStart() {
        return readyForGameStart;
    }

    public void setReadyForGameStart(boolean readyForGameStart) {
        boolean changed = false;
        if (this.readyForGameStart != readyForGameStart)
            changed = true;
        this.readyForGameStart = readyForGameStart;
        if (room != null && changed)
            room.start();
    }

    public void disconnect(Enums.DisconnectReason code, String data) {
        sendString(gson.toJson(new ConnectionError(code, data)));
        try {
            webSocketSession.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnectBadApi(String reason) {
        LOG.errorConsole(nickname + " is hacker! Ip: " + getWebSocketSession().getRemoteAddress() + ". Reason: " + reason);
        disconnect(DISCONNECT_REASON_API_HACKER, reason);
    }

    @Override
    public int hashCode() {
        return nickname.hashCode();
    }

    public int getBeginX() {
        return beginX;
    }

    public void setBeginX(int beginX) {
        this.beginX = beginX;
    }

    public int getBeginY() {
        return beginY;
    }

    public void setBeginY(int beginY) {
        this.beginY = beginY;
    }

    public Node getMainNode() {
        return mainNode;
    }

    public void setMainNode(Node mainNode) {
        this.mainNode = mainNode;
    }

    public HashMap<Node, HashSet<Node>> getNodesMap() {
        return nodesMap;
    }

    public Vector<Node> getNodes() {
        Vector<Node> vector = new Vector<>();
        nodesMap.forEach((node, nodes) -> {
            vector.add(node);
        });
        return vector;
    }

    public Vector<RNode> getReducedNodes() {
        Vector<RNode> vector = new Vector<>();
        nodesMap.forEach((node, nodes) -> {
            vector.add(node.getReduced());
        });
        return vector;
    }

    public void addToUnits(int count) {
        units += count;
    }

    public int towersCount() {
        return nodesMap.size();
    }
}
