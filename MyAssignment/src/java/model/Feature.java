package model;

public class Feature {
    private int featureID;
    private String featureName;
    private String url;

    public Feature() {
    }

    public Feature(int featureID, String featureName, String url) {
        this.featureID = featureID;
        this.featureName = featureName;
        this.url = url;
    }

    // Getters and Setters
    public int getFeatureID() {
        return featureID;
    }

    public void setFeatureID(int featureID) {
        this.featureID = featureID;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}