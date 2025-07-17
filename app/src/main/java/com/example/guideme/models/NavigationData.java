package com.example.guideme.models;

public class NavigationData {
    private String obstacleDescription;
    private String navigationInstruction;
    private double distance;
    private String direction;

    public NavigationData(String obstacleDescription, String navigationInstruction,
                          double distance, String direction) {
        this.obstacleDescription = obstacleDescription;
        this.navigationInstruction = navigationInstruction;
        this.distance = distance;
        this.direction = direction;
    }

    // Getters and setters
    public String getObstacleDescription() {
        return obstacleDescription;
    }

    public String getNavigationInstruction() {
        return navigationInstruction;
    }

    public double getDistance() {
        return distance;
    }

    public String getDirection() {
        return direction;
    }
}