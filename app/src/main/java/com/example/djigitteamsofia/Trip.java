package com.example.djigitteamsofia;

import androidx.navigation.ActivityNavigator;

import java.util.Calendar;
import java.util.Date;

public class Trip {
    private Long id;
    private String start_date;
    private String destination_date;
    private Date StartDate;
    private Date DestinationDate;
    private String Start, Destination;
    private Double MaxSpeed=0.0, AverageSpeed=0.0;
    private Double TotalDistance=0.0;
    private Integer BurnedCalories=0;
    private Integer is_over=0;


    public Trip() {
    }

    public String getStart() {
        return Start;
    }

    public void setStart(String start) {
        Start = start;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public Double getMaxSpeed() {
        return MaxSpeed;
    }

    public void setCurrentSpeed(Double maxSpeed) {
        MaxSpeed = maxSpeed;
    }

    public Double getAverageSpeed() {
        return AverageSpeed;
    }

    public void setAverageSpeed(Double averageSpeed) {
        AverageSpeed = averageSpeed;
    }

    public Double getTotalDistance() {
        return TotalDistance;
    }

    public void setTotalDistance(Double totalDistance) {
        TotalDistance = totalDistance;
    }

    public Integer getBurnedCalories() {
        return BurnedCalories;
    }

    public void setBurnedCalories(Integer burnedCalories) {
        BurnedCalories = burnedCalories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIs_over() {
        return is_over;
    }

    public void setIs_over(Integer is_over) {
        this.is_over = is_over;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getDestination_date() {
        return destination_date;
    }

    public void setDestination_date(String destination_date) { this.destination_date = destination_date; }

    public String toString(){ return this.id+". "+this.Start+" - "+this.Destination; }

    public Date getStartDate() {
        return StartDate;
    }

    public void setStartDate(Date startDate) {
        StartDate = startDate;
    }

    public Date getDestinationDate() {
        return DestinationDate;
    }

    public void setDestinationDate(Date destinationDate) {
        DestinationDate = destinationDate;
    }
}

