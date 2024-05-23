package com.mygdx.game;

import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 3303;
    private int playerId;
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ConcurrentHashMap<Integer, Player> players = new ConcurrentHashMap<>();

    public NetworkClient(int playerId) {
        this.playerId = playerId;
    }

    public void start() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());


            output.writeInt(playerId);
            output.flush();

            // Start a thread to handle receiving updates from the server
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(this::receiveUpdates);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void receiveUpdates() {
        try {
            while (true) {
                int id = input.readInt();
                double x = input.readDouble();
                double y = input.readDouble();
                players.put(id, new Player(id, x, y));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                input.close();
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void sendPlayerPosition(double x, double y) throws IOException {
        output.writeDouble(x);
        output.writeDouble(y);
        output.flush();
    }

    public ConcurrentHashMap<Integer, Player> getPlayers() {
        return players;
    }

    public void stop() {
        try {
            output.close();
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static class Player {
        public int id;
        public double x;
        public double y;

        public Player(int id, double x, double y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }
    }
}
