package Server;

import Client.WhiteboardUser;
import Tools.Constants;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class WhiteboardServer{
    public static void main(String[] args){
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
        catch (NumberFormatException e){
            System.err.println("Port argument must be a number");
            System.exit(0);
        }


        try {
            IRemoteWhiteBoard remoteWhiteBoard = new RemoteWhiteBoard();

            Registry registry = LocateRegistry.createRegistry(port);
            registry.bind(Constants.REGISTRY_NAME, remoteWhiteBoard);
        } catch (RemoteException e) {
            System.err.println("Error occurred when trying to create registry.");
            System.exit(0);
        } catch (AlreadyBoundException e) {
            System.err.println("Registry already bound...");
            System.exit(0);

        }
        System.out.println("Server initialised!");

        WhiteboardUser.main(args);


    }
}
