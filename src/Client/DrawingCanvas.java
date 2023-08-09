package Client;

import Server.IRemoteWhiteBoard;
import Tools.DrawTools;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.Buffer;
import java.rmi.RemoteException;

import static Tools.Constants.CANVAS_HEIGHT;
import static Tools.Constants.CANVAS_WIDTH;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public class DrawingCanvas extends JPanel implements MouseListener{

    private final DrawTools drawTools;
    private Point startingPoint;
    private final BufferedImage whiteboardPicture;
    IRemoteWhiteBoard remoteWhiteBoard;

    public DrawingCanvas(DrawTools drawTools, IRemoteWhiteBoard remoteWhiteBoard){
        setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        addMouseListener(this);
        this.drawTools = drawTools;
        this.remoteWhiteBoard = remoteWhiteBoard;
        whiteboardPicture = new BufferedImage(CANVAS_WIDTH, CANVAS_HEIGHT, TYPE_INT_ARGB);
        try {
            // Turning array of RGB values into the image
            int[] whiteboardPictureRGB = remoteWhiteBoard.getCanvas();
            whiteboardPicture.setRGB(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT, whiteboardPictureRGB, 0, 600);
        } catch (RemoteException e) {
            System.err.println("Sending of canvas failed.");
        }
        drawTools.setGraphics(whiteboardPicture.createGraphics());
        drawTools.setColor(Color.BLACK);

    }

    public DrawTools getDrawTools() {return drawTools;}

    public void saveCanvasToFile() {
        File output = new File ("canvas.png");
        try {
            ImageIO.write(whiteboardPicture, "png", output);
        } catch (IOException e) {
            System.err.println("Error: Image could not be saved.");
        }
    }

    // click (use to input text)
    @Override
    public void mouseClicked(MouseEvent e) {
        if (drawTools.getCurrentToolName().equals("Text")){
            try {
                remoteWhiteBoard.writeText(e.getPoint(), drawTools.getCurrentText(), drawTools.getColor());
            } catch (RemoteException ex) {
                System.err.println("Failed to input text. Please try again.");
            }
        }
        repaint();
    }



    // Start drawing of shape
    @Override
    public void mousePressed(MouseEvent e) {
        // Ignore if text mode is selected
        if (drawTools.getCurrentToolName().equals("Text")){
            return ;
        }

        startingPoint = e.getPoint();
    }

    // End drawing of shape
    @Override
    public void mouseReleased(MouseEvent e) {
        if (drawTools.getCurrentToolName().equals("Text")){
            return ;
        }
        Point endingPoint = e.getPoint();
        try {
            remoteWhiteBoard.drawShape(startingPoint, endingPoint, drawTools.getCurrentToolName(), drawTools.getColor());
        } catch (RemoteException ex) {
            System.err.println("Failed to draw shape.");
        }
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    // Paint canvas onto screen
    @Override
    public void paintComponent (Graphics gr) {
        super.paintComponent(gr);
        Graphics2D gr2d = (Graphics2D)gr;
        gr2d.setBackground(Color.white);
        gr2d.drawImage(whiteboardPicture, null, 0, 0);

    }
}
