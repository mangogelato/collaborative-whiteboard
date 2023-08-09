package Client;

import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteClient extends Remote {
    public void writeText(Point startingPoint, String text, Color color) throws RemoteException;

    public void drawShape(Point startingPoint, Point endingPoint, String toolName, Color color) throws RemoteException;

    public void managerEndedSession() throws RemoteException;


}
