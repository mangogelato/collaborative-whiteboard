package Server;

import Client.*;
import Tools.DrawTools;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import static Tools.Constants.CANVAS_HEIGHT;
import static Tools.Constants.CANVAS_WIDTH;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public class RemoteWhiteBoard extends UnicastRemoteObject implements IRemoteWhiteBoard{

    private final DrawTools drawTools;
    private final BufferedImage canvas;
    private final HashMap<String, IRemoteClient> users;
    private String manager;
    protected RemoteWhiteBoard() throws RemoteException {
        drawTools = new DrawTools();
        canvas = new BufferedImage(CANVAS_WIDTH, CANVAS_HEIGHT, TYPE_INT_ARGB);
        drawTools.setGraphics(canvas.createGraphics());
        users = new HashMap<>();

        // Make the canvas white instead of transparent
        clearWhiteboard();
    }

    // Write string on the remote canvas
    public void writeText(Point startingPoint, String text, Color color) throws RemoteException {
        drawTools.writeText(startingPoint, text, color);
        for (Map.Entry<String, IRemoteClient> user : users.entrySet()) {
            IRemoteClient client = user.getValue();
            try {
                client.writeText(startingPoint, text, color);
            }
            catch (ConnectException e) {
                removeUser(user.getKey());
            }
        }
    }

    // Draw a shape on the remote canvas
    public void drawShape(Point startingPoint, Point endingPoint, String toolName, Color color) throws RemoteException {
        drawTools.drawShape(startingPoint, endingPoint, toolName, color);
        for (Map.Entry<String, IRemoteClient> user : users.entrySet()) {
            IRemoteClient client = user.getValue();
            try {
                client.drawShape(startingPoint, endingPoint, toolName, color);
            }
            catch (ConnectException e) {
                removeUser(user.getKey());
            }
        }

    }

    // Add a new user to the broadcast
    // Returns true if successful
    // User format: username, remote client object
    @Override
    public boolean addUser(String username, IRemoteClient client) throws RemoteException {
        if (users.isEmpty()) {
            manager = username;
        }
        System.out.println(username + " joined the whiteboard.");
        if (users.get(username) == null) {
            users.put(username, client);
            return true;
        }
        return false;

    }

    // Remove a user from the broadcast
    @Override
    public void removeUser(String username) throws RemoteException {
        users.remove(username);
        System.out.println(username + " left the whiteboard.");

        // Close broadcast if user removed is the manager
        if (isManager(username)){
            for (Map.Entry<String, IRemoteClient> user : users.entrySet()) {
                IRemoteClient client = user.getValue();
                client.managerEndedSession();

            }
            System.exit(0);
        }
    }

    @Override
    public boolean isManager(String username) throws RemoteException {
        return username.equals(manager);
    }


    // Send the canvas to a user
    @Override
    public int[] getCanvas() throws RemoteException {
        // Turning the canvas into an array of RGB values
        return canvas.getRGB(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT, null, 0, 600);
    }

    // Get all usernames in broadcast
    public String[] getUsernames() throws RemoteException {
        return users.keySet().toArray(new String[0]);
    }

    // Make the whiteboard canvas blank
    public void clearWhiteboard() throws RemoteException {
        Point startingPoint = new Point(0,0);
        Point endingPoint = new Point(CANVAS_WIDTH, CANVAS_HEIGHT);
        drawShape(startingPoint, endingPoint, "Filled Rectangle", Color.WHITE);
        System.out.println("Whiteboard was cleared.");

    }
}
