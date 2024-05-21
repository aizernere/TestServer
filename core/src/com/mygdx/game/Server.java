package com.mygdx.game;

import java.io.IOException;
import java.sql.*;

public class Server {
    public static void run() {
        try(Connection c = MySQLConnection.getDatabase(); Statement statement = c.createStatement()) {
            String query = "CREATE TABLE IF NOT EXISTS tbluser (" +
                    "userID INT PRIMARY KEY AUTO_INCREMENT," +
                    "x FLOAT NOT NULL," +
                    "y FLOAT NOT NULL)";
            statement.execute(query);
            System.out.println("Table [user] created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getPos(int userID) {
        try(Connection c = MySQLConnection.getDatabase();
            Statement statement = c.createStatement()){
            String query = "SELECT x,y FROM tbluser where userID='"+userID+"'";
            ResultSet res = statement.executeQuery(query);

            if(res.next()){
                float x = res.getFloat(0);
                float y = res.getFloat(1);
            }
        }catch ( SQLException e){
            e.printStackTrace();
        }
    }

}
