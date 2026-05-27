# Queues Management Application 

## Overview

A multithreaded Java simulation application that assigns clients to queues so that waiting time is minimized. The simulation generates N random clients and distributes them across Q queues in real time, with each queue running on its own thread. Results are logged to a `.txt` file and displayed live in a Java Swing GUI.

## Project Structure

```
src/main/java/
├── Model/
│   ├── Task.java          # Client with ID, arrival time, service time
│   └── Server.java        # Queue thread (one per queue)
├── BusinessLogic/
│   ├── SimulationManager.java       # Main simulation loop / client dispatcher
│   ├── SimulationTimer.java         # Simulation clock thread
│   ├── Scheduler.java               # Assigns clients to the best queue
│   ├── Strategy.java                # Queue selection strategy interface
│   ├── SelectionPolicy.java         # Enum: SHORTEST_QUEUE / SHORTEST_TIME
│   ├── ShortestQueueStrategy.java   # Assigns to queue with fewest clients
│   └── TimeStrategy.java            # Assigns to queue with shortest wait time
├── GUI/
│   └── SimulationFrame.java         # Swing GUI (setup + real-time view)
└── Main.java
```

## Features

- Random client generation with configurable N, arrival range, and service range
- One thread per queue using `AtomicInteger` for thread-safe service time tracking
- Minimum-wait-time queue assignment strategy (Strategy design pattern)
- Real-time queue state display in the GUI
- Simulation statistics: average waiting time, average service time, peak hour

## Technologies

- Java 23
- Java Swing (GUI)
- Maven (build tool)
- `java.util.concurrent` / `AtomicInteger` (thread safety)

## How to Run

**Prerequisites:** Java 23+, Maven

```bash
mvn compile
mvn exec:java -Dexec.mainClass="Main"
```

Or open in IntelliJ IDEA and run `Main.java` directly.

## Input Parameters

| Parameter | Description |
|---|---|
| N | Number of clients |
| Q | Number of queues |
| t_max_simulation | Total simulation duration (seconds) |
| t_min / t_max arrival | Arrival time range for clients |
| t_min / t_max service | Service time range for clients |


- `PackageDiagram.drawio.xml`
- `UseCaseDiagram.drawio.xml`
