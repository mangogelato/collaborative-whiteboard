# Java Collaborative Whiteboard

Collaborative whiteboard program

Steps to run locally:

1. Open terminal in directory

2. java -jar CreateWhiteBoard.jar <port> <username>
   - e.g. java -jar CreateWhiteBoard.jar 700 Jeff
   - This will create a server and a manager version of the client

3. Open another terminal in directory
4. java -jar JoinWhiteBoard.jar <port> <username>
   - e.g. java -jar JoinWhiteBoard.jar 700 Steve
   - This will create a user version of the client

## Features
- Simultaneous drawing on whiteboard by multiple users
- Text
- Clear whiteboard/save whiteboard to file (manager only)
- List of connected users

## Wishlist
- User chat
- Kick a user (manager only)


