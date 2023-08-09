package Client;

import Server.IRemoteWhiteBoard;
import Tools.DrawTools;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import static Tools.Constants.USER_PROGRAM_HEIGHT;
import static Tools.Constants.USER_PROGRAM_WIDTH;

public class WhiteboardGUI extends JFrame{
    private final DrawTools drawTools;

    private JButton circleButton;
    private JButton lineButton;
    private JButton ovalButton;
    private JButton rectangleButton;
    private JButton textButton;
    private JPanel drawingCanvas;
    private JPanel userList;
    private JPanel gui;
    private JTextField textField1;
    private JPanel menu;
    private JColorChooser colourPicker;
    private JTextPane userListTextPane;
    private JPanel managerMenu;
    private JButton resetCanvasButton;
    private JButton saveButton;
    private JButton filledCircleButton;
    private JButton filledOvalButton;
    private JButton filledRectangleButton;
    private final DrawingCanvas canvasReference;

    public WhiteboardGUI(IRemoteWhiteBoard remoteWhiteBoard){

        drawTools = new DrawTools();
        canvasReference = new DrawingCanvas(drawTools, remoteWhiteBoard);
        drawingCanvas = canvasReference;

        $$$setupUI$$$();

        // Hide manager menu
        managerMenu.setVisible(false);

        setContentPane(gui);
        setTitle("Shared Whiteboard");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(USER_PROGRAM_WIDTH, USER_PROGRAM_HEIGHT);
        setVisible(true);


        // Listeners
        ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawTools.setCurrentToolName(e.getActionCommand());
            }
        };
        ActionListener clearListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    remoteWhiteBoard.clearWhiteboard();
                } catch (RemoteException ex) {
                    JOptionPane.showMessageDialog(drawingCanvas, "Whiteboard clear failed :(", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        ActionListener saveListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canvasReference.saveCanvasToFile();
                JOptionPane.showMessageDialog(drawingCanvas, "Saved image to file!", "Save", JOptionPane.INFORMATION_MESSAGE);
            }
        };

        ChangeListener colourListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                drawTools.setColor(colourPicker.getColor());
            }
        };

        DocumentListener textListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                drawTools.setCurrentText(textField1.getText());
            }
        };
        circleButton.addActionListener(buttonListener);
        lineButton.addActionListener(buttonListener);
        ovalButton.addActionListener(buttonListener);
        rectangleButton.addActionListener(buttonListener);
        filledCircleButton.addActionListener(buttonListener);
        filledOvalButton.addActionListener(buttonListener);
        filledRectangleButton.addActionListener(buttonListener);
        textButton.addActionListener(buttonListener);
        textField1.getDocument().addDocumentListener(textListener);
        colourPicker.getSelectionModel().addChangeListener(colourListener);
        resetCanvasButton.addActionListener(clearListener);
        saveButton.addActionListener(saveListener);


    }

    // Used by IntelliJ for form setup
    private void $$$setupUI$$$() {
    }

    // Used by IntelliJ for form setup
    private void createUIComponents() {
        return;
    }

    public DrawingCanvas getDrawingCanvas() {
        return canvasReference;
    }

    // Update user list
    public void setUserList(String usernameString) {
        userListTextPane.setText(usernameString);
    }

    // Allow access to manager menu
    public void setManager() {
        managerMenu.setVisible(true);
    }

    public void managerEndedSession() {

    }
}
