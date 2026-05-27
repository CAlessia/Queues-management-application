package GUI;

import BusinessLogic.*;
import Model.Server;
import Model.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SimulationFrame extends JFrame {
    private JPanel panel;
    private JLabel nrClients;
    private JLabel nrQueues;
    private JLabel simulationTime;
    private JLabel minArrivalTime;
    private JLabel maxArrivalTime;
    private JLabel minServiceTime;
    private JLabel maxServiceTime;
    private JButton simulateButton;
    private JComboBox simPolComboBox;
    private SimulationManager simulationManager;
    private JTextArea logArea;

    public SimulationFrame(String title) {
        super(title);
        this.prepareGUI();
    }

    private void prepareGUI() {
        this.setSize(700, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.panel = new JPanel();
        this.setContentPane(this.panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        simulationManager = new SimulationManager();

        logArea = new JTextArea(30, 50);
        logArea.setEditable(false);
        panel.add(new JScrollPane(logArea));
        simulationManager.setFrame(SimulationFrame.this);

        nrClients = new JLabel("Nr Clients:");
        nrQueues = new JLabel("Nr Queues:");
        simulationTime = new JLabel("Simulation Time:");
        minArrivalTime = new JLabel("Min Arrival Time:");
        maxArrivalTime = new JLabel("Max Arrival Time:");
        minServiceTime = new JLabel("Min Service Time:");
        maxServiceTime = new JLabel("Max Service Time:");
        simulateButton = new JButton("Simulate");

        JTextField nrClientsF = new JTextField(10);
        JTextField nrQueuesF = new JTextField(10);
        JTextField simulationTimeF = new JTextField(10);
        JTextField minArrivalTimeF = new JTextField(10);
        JTextField maxArrivalTimeF = new JTextField(10);
        JTextField minServiceTimeF = new JTextField(10);
        JTextField maxServiceTimeF = new JTextField(10);

        String[] policy ={"Time strategy", "Shortest Queue"};
        simPolComboBox = new JComboBox(policy);

        panel.add(nrClients);
        panel.add(nrClientsF);
        panel.add(nrQueues);
        panel.add(nrQueuesF);
        panel.add(simulationTime);
        panel.add(simulationTimeF);
        panel.add(minArrivalTime);
        panel.add(minArrivalTimeF);
        panel.add(maxArrivalTime);
        panel.add(maxArrivalTimeF);
        panel.add(minServiceTime);
        panel.add(minServiceTimeF);
        panel.add(maxServiceTime);
        panel.add(maxServiceTimeF);
        panel.add(simPolComboBox);
        panel.add(simulateButton);

        simulateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{

                simulationManager.numberOfClients = Integer.parseInt(nrClientsF.getText());
                simulationManager.numberOfServers = Integer.parseInt(nrQueuesF.getText());
                simulationManager.timeLimit = Integer.parseInt(simulationTimeF.getText());
                simulationManager.minProcessingTime = Integer.parseInt(minServiceTimeF.getText());
                simulationManager.maxProcessingTime = Integer.parseInt(maxServiceTimeF.getText());
                simulationManager.minArrivalTime = Integer.parseInt(minArrivalTimeF.getText());
                simulationManager.maxArrivalTime = Integer.parseInt(maxArrivalTimeF.getText());

                String selectedPolicy = (String) simPolComboBox.getSelectedItem();
                    if ("Time strategy".equals(selectedPolicy)) {
                        SimulationManager.selectionPolicy = SelectionPolicy.SHORTEST_TIME;
                    } else {
                        SimulationManager.selectionPolicy = SelectionPolicy.SHORTEST_QUEUE;
                    }

                    List<Server> servers = new ArrayList<>();
                    for (int i = 0; i < simulationManager.numberOfServers; i++) {
                        servers.add(new Server());
                    }

                    Scheduler scheduler = new Scheduler(servers, 100, null, simulationManager.numberOfServers);
                    scheduler.changeStrategy(SimulationManager.selectionPolicy);
                    simulationManager.scheduler = scheduler;

                    List<Task> tasks = simulationManager.generateRandomTasks();

                    Thread simulationThread = new Thread(simulationManager);
                    simulationThread.start();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(SimulationFrame.this,
                            "Error: " + ex.getMessage(),
                            "Input Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }
    public void appendLog(String text) {
        logArea.append(text + "\n");
    }

}


