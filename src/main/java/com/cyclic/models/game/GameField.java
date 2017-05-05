package com.cyclic.models.game;

import com.cyclic.models.game.net.NewBonusBroadcast;
import com.cyclic.models.game.utils.AcceptMoveResult;

import java.awt.*;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

import static com.cyclic.configs.Constants.NODE_BONUS;
import static com.cyclic.configs.Constants.NODE_TOWER;

/**
 * Created by serych on 03.04.17.
 */
public class GameField {
    private transient int height, width;
    private transient Node[][] world;
    private transient Room room;


    public GameField(Room room, int height, int width) {
        this.room = room;
        world = new Node[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                world[i][j] = null;
            }
        }
        this.height = height;
        this.width  = width;

    }

    public Node getByPosition(int x, int y) {
        return world[y][x];
    }

    public void setNodeToPosition(int beginX, int beginY, Node node) {
        if (beginX < 0 || beginX >= width || beginY < 0 || beginY >= height)
            return;
        world[beginY][beginX] = node;
    }

    public void addAndBroadcastRandomBonuses(int count) {
        Vector<Bonus> bonuses = new Vector<>();
        for (int i = 0; i < count; i++) {
            Point point = findRandomNullPoint();
            if (point == null)
                return;
            Node node = new Node(null,
                    0,
                    ThreadLocalRandom.current().nextInt(room.getBonusMinValue(), room.getBonusMaxValue() + 1),
                    NODE_BONUS, point.x, point.y
            );
            world[point.y][point.x] = node;
            bonuses.add(new Bonus(point.x, point.y, node.getValue()));
        }
        room.broadcast(room.getGson().toJson(new NewBonusBroadcast(bonuses)));
    }

    public Point findRandomNullPoint() {
        int x = 0, y = 0;
        // try to find random point 10 times
        for (int i = 0; i < 10; i++) {
            x = ThreadLocalRandom.current().nextInt(0, width);
            y = ThreadLocalRandom.current().nextInt(0, height);
            if (world[y][x] == null) {
                return new Point(x, y);
            }
        }
        // go layer by layer to find free point
        for (int i = 0; i < width * height; i++) {
            x++;
            if (x == width) {
                x = 0;
                y++;
            }
            if (y == height) {
                y = 0;
            }
            if (world[y][x] == null) {
                return new Point(x, y);
            }
        }
        // reaches if there is NO free point. If during production this code will go, tell it to @SCaptainCAP. I'll buy you shaurma
        return null;
    }

    public void stopGame() {
        room = null;
    }

    private boolean checkPlayerTurn(Node player) {
        return player.getPlayerID() == room.getPid();
    }

    private boolean checkPassUnits(Node node, int passUnits) {
        return passUnits >= 1 && node.getValue() < passUnits;
    }

    private void addNewTower(Node parentNode, Long pid, int countNodes, int x, int y) {
        setNodeToPosition(x, y, new Node(
                parentNode,
                pid,
                countNodes,
                NODE_TOWER,
                x, y));
    }

    private void addNewTower(Node parentNode, Long pid, int countNodes, Move move) {
        addNewTower(parentNode, pid, countNodes, move.getXto(), move.getXto());
    }

    private AcceptMoveResult playerMoveFree(Node fromNode, Move move) {
        int newUnits = move.getUnitsCount();
        fromNode.addToValue(-newUnits);
        addNewTower(fromNode, fromNode.getPlayerID(), newUnits, move);
        return AcceptMoveResult.ACCEPT_OK;
    }

    private AcceptMoveResult playerMoveBonus(Node currentPlayer, Node bonus, int passUnits) {
        int newUnits = passUnits;
        currentPlayer.setValue(currentPlayer.getValue() - newUnits);

        newUnits += bonus.getValue();

        /* rewrite bonus */
        addNewTower(currentPlayer, currentPlayer.getPlayerID(),
                newUnits, bonus.getX(), bonus.getY());

        return AcceptMoveResult.ACCEPT_OK;
    }

    private final static Random randomDelta = new Random(); // TODO check
    private AcceptMoveResult playerMoveEnemy(Node currentPlayer, Node enemy, int passUnits) {
        int delta = enemy.getValue() - passUnits;
        /**
         *  delta < 0 ~ current Player win
         *  delta = 0 ~ Random
         *  delta > 0 ~ enemy win
         */

        if(delta == 0)
            delta = (randomDelta.nextInt(1)) == 0 ? 1 : -1;

        if(delta > 0) {
            currentPlayer.addToValue(-passUnits);
            enemy.addToValue(passUnits);
        } else {
            
        }
    }

    public AcceptMoveResult acceptMove(Move move) {
        Node my = getByPosition(move.getXfrom(), move.getYfrom());
        Node target = getByPosition(move.getXto(), move.getYto());

        AcceptMoveResult result = AcceptMoveResult.ACCEPT_FAIL;

        if (my != null && checkPlayerTurn(my)) {
            if(checkPassUnits(my, move.getUnitsCount()))
                return AcceptMoveResult.ACCEPT_FAIL;

            if(target == null)
                result = playerMoveFree(my, move);
            else {
                switch (target.getType()) {
                    case NODE_TOWER:
                        result = playerMoveEnemy(my, target, move.getUnitsCount());
                        break;
                    case NODE_BONUS:
                        result = playerMoveBonus(my, target, move.getUnitsCount());
                        break;
                }
            }

            move.setParentUnitsCount(my.getValue());
        }

        return result;
    }
}
