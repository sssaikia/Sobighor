package com.sstudio;

/**
 * Created by Alan on 9/7/2017.
 */

public class ImageUrlC {
    String url, price;
    long timestamp;

    public ImageUrlC() {

    }

    public ImageUrlC(String url, long timestamp, String price) {
        this.url = url;
        this.timestamp = timestamp;
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setUrl(String url1) {
        this.url = url1;
    }

    public void setTimestamp(long timestamp1) {
        this.timestamp = timestamp1;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
