package Client;

import Server.IRemoteWhiteBoard;
import Tools.Constants;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class WhiteboardUser {
    private static IRemoteWhiteBoard remoteWhiteBoard;

    public static void main(String[] args) {
        // Arg parsing
        // Arg 1: Registry port
        // Arg 2: Chosen username
        if (args.length != 2){
            System.err.println("Argument format: <port> <username>");
            System.exit(0);
        }
        int port = 0;
        try {
            port = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException e) {
            System.err.println("Server port argument must be a number");
            System.exit(0);
        }
        String username = args[1];


        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(port);
            remoteWhiteBoard = (IRemoteWhiteBoard) registry.lookup(Constants.REGISTRY_NAME);
        } catch (ConnectException e) {
            System.err.println("Cannot connect to registry.");
            System.exit(0);
        } catch (NotBoundException e) {
            System.err.println("Remote whiteboard not found in registry.");
            System.exit(0);
        } catch (RemoteException e) {
            System.err.println("Unknown error accessing registry.");
            System.exit(0);
        }


        WhiteboardGUI userInterface = new WhiteboardGUI(remoteWhiteBoard);


        DrawingCanvas drawingCanvas = userInterface.getDrawingCanvas();
        IRemoteClient remoteClient = null;
        try {
            remoteClient = new RemoteClient(drawingCanvas);
            if (!remoteWhiteBoard.addUser(username, remoteClient)){
                System.err.println("Username taken! Please try again with another username.");
                System.exit(0);
            }

            // Unlock manager menu for manager
            if (remoteWhiteBoard.isManager(username)) {
                userInterface.setManager();
                userInterface.setTitle("Shared Whiteboard - " + username + " (Manager)");
            }
            else {
                userInterface.setTitle("Shared Whiteboard - " + username);
            }
        } catch (RemoteException e) {
            System.err.println("Error initialising client remote object.");
            System.exit(0);
        }

        // Thread for updating user list
        // Updates user list every 3 seconds
        UserListThread userListThread = new UserListThread(remoteWhiteBoard, userInterface);
        ScheduledExecutorService timer = Executors.newScheduledThreadPool(1);
        timer.scheduleAtFixedRate(userListThread, 0, 3, TimeUnit.SECONDS);

        // Tell remote registry when the user has left
        WindowListener windowListener = new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}

            @Override
            public void windowClosed(WindowEvent e) {
                try {
                    remoteWhiteBoard.removeUser(username);
                    System.exit(0);
                } catch (RemoteException ex) {
                    System.err.println("Error: User was not properly removed. A server restart may be required.");
                    System.exit(0);
                }
            }

            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        };
        userInterface.addWindowListener(windowListener);

    }
}
