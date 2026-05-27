import GUI.SimulationFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new SimulationFrame("simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}