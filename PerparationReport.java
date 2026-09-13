package model;

public class PerparationReport {
    private String reportId;
    private String orderId;
    private String bakerName;
    private String status;
    private String flour;
    private String sugar;
    private String eggs;
    private String extra;
    private String prepTime;
    private String quality;

    public PerparationReport(String reportId, String orderId, String bakerName, String status, 
                             String flour, String sugar, String eggs, String extra, 
                             String prepTime, String quality) {
        this.reportId = reportId;
        this.orderId = orderId;
        this.bakerName = bakerName;
        this.status = status;
        this.flour = flour;
        this.sugar = sugar;
        this.eggs = eggs;
        this.extra = extra;
        this.prepTime = prepTime;
        this.quality = quality;
    }

    
    public String getReportId() { return reportId; }
    public void setReportId(String reportId) { this.reportId = reportId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getBakerName() { return bakerName; }
    public void setBakerName(String bakerName) { this.bakerName = bakerName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getFlour() { return flour; }
    public void setFlour(String flour) { this.flour = flour; }

    public String getSugar() { return sugar; }
    public void setSugar(String sugar) { this.sugar = sugar; }

    public String getEggs() { return eggs; }
    public void setEggs(String eggs) { this.eggs = eggs; }

    public String getExtra() { return extra; }
    public void setExtra(String extra) { this.extra = extra; }

    public String getPrepTime() { return prepTime; }
    public void setPrepTime(String prepTime) { this.prepTime = prepTime; }

    public String getQuality() { return quality; }
    public void setQuality(String quality) { this.quality = quality; }

    public String toFileString() {
        return reportId + "," + orderId + "," + bakerName + "," + status + "," + 
               flour + "," + sugar + "," + eggs + "," + extra + "," + prepTime + "," + quality;
    }

    public static PerparationReport fromFileString(String fileLine) {
        String[] parts = fileLine.split(",");
        if (parts.length == 10) {
            return new PerparationReport(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(),
                                         parts[4].trim(), parts[5].trim(), parts[6].trim(), parts[7].trim(),
                                         parts[8].trim(), parts[9].trim());
        }
        return null;
    }

    @Override
    public String toString() {
        return "Report ID: " + reportId + " | Order ID: " + orderId + " | Status: " + status;
    }
}