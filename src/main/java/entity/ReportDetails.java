package com.indiapost.brsrplatform.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "report_details")
public class ReportDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "report_id", nullable = false)
    private Long reportId;

    @Column(name = "energy_consumption")
    private Double energyConsumption;

    @Column(name = "water_consumption")
    private Double waterConsumption;

    @Column(name = "waste_generated")
    private Double wasteGenerated;

    @Column(name = "waste_recycled")
    private Double wasteRecycled;

    @Column(name = "total_employees")
    private Integer totalEmployees;

    @Column(name = "employee_training_hours")
    private Double employeeTrainingHours;

    @Column(name = "governance_meetings")
    private Integer governanceMeetings;

    @Column(name = "carbon_emission")
    private Double carbonEmission;

    @Column(name = "renewable_energy_percent")
    private Double renewableEnergyPercent;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    public ReportDetails() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public Double getEnergyConsumption() {
        return energyConsumption;
    }

    public void setEnergyConsumption(Double energyConsumption) {
        this.energyConsumption = energyConsumption;
    }

    public Double getWaterConsumption() {
        return waterConsumption;
    }

    public void setWaterConsumption(Double waterConsumption) {
        this.waterConsumption = waterConsumption;
    }

    public Double getWasteGenerated() {
        return wasteGenerated;
    }

    public void setWasteGenerated(Double wasteGenerated) {
        this.wasteGenerated = wasteGenerated;
    }

    public Double getWasteRecycled() {
        return wasteRecycled;
    }

    public void setWasteRecycled(Double wasteRecycled) {
        this.wasteRecycled = wasteRecycled;
    }

    public Integer getTotalEmployees() {
        return totalEmployees;
    }

    public void setTotalEmployees(Integer totalEmployees) {
        this.totalEmployees = totalEmployees;
    }

    public Double getEmployeeTrainingHours() {
        return employeeTrainingHours;
    }

    public void setEmployeeTrainingHours(Double employeeTrainingHours) {
        this.employeeTrainingHours = employeeTrainingHours;
    }

    public Integer getGovernanceMeetings() {
        return governanceMeetings;
    }

    public void setGovernanceMeetings(Integer governanceMeetings) {
        this.governanceMeetings = governanceMeetings;
    }

    public Double getCarbonEmission() {
        return carbonEmission;
    }

    public void setCarbonEmission(Double carbonEmission) {
        this.carbonEmission = carbonEmission;
    }

    public Double getRenewableEnergyPercent() {
        return renewableEnergyPercent;
    }

    public void setRenewableEnergyPercent(Double renewableEnergyPercent) {
        this.renewableEnergyPercent = renewableEnergyPercent;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}