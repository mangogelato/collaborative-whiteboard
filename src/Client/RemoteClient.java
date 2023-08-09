

package Client;

import Tools.DrawTools;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteClient extends UnicastRemoteObject implements IRemoteClient {
    WhiteboardGUI userInterface;
    DrawTools drawTools;
    DrawingCanvas drawingCanvas;

    protected RemoteClient(DrawingCanvas drawingCanvas) throws RemoteException {
        this.drawingCanvas = drawingCanvas;
        drawTools = drawingCanvas.getDrawTools();
    }

    @Override
    public void writeText(Point startingPoint, String text, Color color) throws RemoteException {
        drawTools.writeText(startingPoint, text, color);
        drawingCanvas.repaint();
    }

    @Override
    public void drawShape(Point startingPoint, Point endingPoint, String toolName, Color color) throws RemoteException {
        drawTools.drawShape(startingPoint, endingPoint, toolName, color);
        drawingCanvas.repaint();
    }

    @Override
    public void managerEndedSession() throws RemoteException {
        JOptionPane.showMessageDialog(
                drawingCanvas,
                "Session has been ended by the manager. Please close.",
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}