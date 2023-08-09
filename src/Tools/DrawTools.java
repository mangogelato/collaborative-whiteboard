package Tools;


import java.awt.*;

import static java.lang.Math.abs;

public class DrawTools {
    private String currentToolName = "Circle";
    private String currentText = "";
    private Graphics2D g;

    private Color color = Color.BLACK;

    public DrawTools() {
    }

    public void setCurrentToolName(String currentToolName) {
        this.currentToolName = currentToolName;
    }

    public String getCurrentToolName() {
        return currentToolName;
    }

    public void setCurrentText(String currentText) {
        this.currentText = currentText;
    }

    public String getCurrentText() {
        return currentText;
    }


    public void setGraphics(Graphics2D g) {
        this.g = g;
    }

    public void setColor(Color color) {
        this.color = color;
        g.setColor(color);
    }

    public Color getColor(){
        return color;
    }

    // Writes chosen string onto canvas
    public synchronized void writeText(Point startingPoint){
        g.drawString(currentText, startingPoint.x, startingPoint.y);
    }

    // Remote call version of writeText
    public synchronized void writeText(Point startingPoint, String text, Color color){
        g.setColor(color);
        g.drawString(text, startingPoint.x, startingPoint.y);
        g.setColor(this.color);
    }

    // Draws chosen shape onto the canvas
    public synchronized void drawShape(Point startingPoint, Point endingPoint){
        // x and y refer to the coordinates of the upper left corner
        // of the rectangle containing the shape
        int x, y, width, height;

        // Calculation of dimensions of shape
        width = abs(startingPoint.x - endingPoint.x);
        height = abs(startingPoint.y - endingPoint.y);
        if (startingPoint.x < endingPoint.x){
            if (startingPoint.y < endingPoint.y){
                x = startingPoint.x;
                y = startingPoint.y;
            }
            else {
                x = startingPoint.x;
                y = endingPoint.y;
            }
        }
        else {
            if (startingPoint.y < endingPoint.y){
                x = endingPoint.x;
                y = startingPoint.y;
            }
            else {
                x = endingPoint.x;
                y = endingPoint.y;
            }
        }
        switch (currentToolName) {
            case "Circle":
                if (width < height){
                    g.drawOval(x, y, width, width);
                }
                else {
                    g.drawOval(x, y, height, height);
                }
                break;
            case "Filled Circle":
                if (width < height){
                    g.fillOval(x, y, width, width);
                }
                else {
                    g.fillOval(x, y, height, height);
                }
                break;
            case "Line":
                int x1 = startingPoint.x;
                int y1 = startingPoint.y;
                int x2 = endingPoint.x;
                int y2 = endingPoint.y;
                g.drawLine(x1, y1, x2, y2);
                break;
            case "Oval":
                g.drawOval(x, y, width, height);
                break;
            case "Filled Oval":
                g.fillOval(x, y, width, height);
                break;
            case "Rectangle":
                g.drawRect(x, y, width, height);
                break;
            case "Filled Rectangle":
                g.fillRect(x, y, width, height);
                break;
        }

    }

    // Remote call version of drawShape
    public synchronized void drawShape(Point startingPoint, Point endingPoint, String toolName, Color color) {
        String tempToolName = currentToolName;
        currentToolName = toolName;
        g.setColor(color);
        drawShape(startingPoint, endingPoint);
        currentToolName = tempToolName;
        g.setColor(this.color);
    }
}



