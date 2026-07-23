package com.library;

public class Fine {

    private int fineId;
    private int issueId;
    private int studentId;
    private int daysLate;
    private double amount;
    private String paid;

    public Fine() {}

    public Fine(int fineId, int issueId, int studentId,
                int daysLate, double amount, String paid) {
        this.fineId = fineId;
        this.issueId = issueId;
        this.studentId = studentId;
        this.daysLate = daysLate;
        this.amount = amount;
        this.paid = paid;
    }

    public int getFineId() {
        return fineId;
    }

    public void setFineId(int fineId) {
        this.fineId = fineId;
    }

    public int getIssueId() {
        return issueId;
    }

    public void setIssueId(int issueId) {
        this.issueId = issueId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getDaysLate() {
        return daysLate;
    }

    public void setDaysLate(int daysLate) {
        this.daysLate = daysLate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    @Override
    public String toString() {
        return "Fine{" +
                "fineId=" + fineId +
                ", issueId=" + issueId +
                ", studentId=" + studentId +
                ", daysLate=" + daysLate +
                ", amount=" + amount +
                ", paid='" + paid + '\'' +
                '}';
    }
}