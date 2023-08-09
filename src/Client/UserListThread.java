package Client;

import Server.IRemoteWhiteBoard;

import java.rmi.RemoteException;
import java.util.Arrays;

public class UserListThread implements Runnable {


    private final IRemoteWhiteBoard remoteWhiteBoard;
    private final WhiteboardGUI userInterface;
    private String usernameString = "";
    public UserListThread(IRemoteWhiteBoard remoteWhiteBoard, WhiteboardGUI userInterface){
        this.remoteWhiteBoard = remoteWhiteBoard;
        this.userInterface = userInterface;
    }

    // Gets list of usernames connected to remote canvas
    @Override
    public void run() {
        String[] usernames = new String[0];
        try {
            usernames = remoteWhiteBoard.getUsernames();
        } catch (RemoteException e) {
            System.err.println("Failed to retrieve usernames.");
        }
        Arrays.sort(usernames);
        for (int i = 0; i < usernames.length; i++){
            usernameString += usernames[i] + "\n";
        }
        userInterface.setUserList(usernameString);
        usernameString = "";
    }
}
