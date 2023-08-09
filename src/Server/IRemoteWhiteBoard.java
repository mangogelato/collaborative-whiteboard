package Server;

import Client.IRemoteClient;

import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteWhiteBoard extends Remote {


    public void writeText(Point startingPoint, String text, Color color) throws RemoteException;

    public void drawShape(Point startingPoint, Point endingPoint, String toolName, Color color) throws RemoteException;

    public boolean addUser(String username, IRemoteClient client) throws RemoteException;

    public void removeUser(String username) throws RemoteException;

    public boolean isManager(String username) throws RemoteException;

    public int[] getCanvas() throws RemoteException;

    public String[] getUsernames() throws RemoteException;

    public void clearWhiteboard() throws RemoteException;


}
