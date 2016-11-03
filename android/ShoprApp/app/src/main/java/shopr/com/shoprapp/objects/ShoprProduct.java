package shopr.com.shoprapp.objects;

import java.sql.Date;

/**
 * Created by Neil on 10/19/2016.
 *
 * @author Neil Allison
 */
@SuppressWarnings("unused")
public class ShoprProduct {
    private String upc;
    private String name;
    private Double regularPrice;
    private Double salePrice;
    private String image;
    private String thumbnailImage;
    private String shortDescription;
    private String longDescription;
    private Long customerReviewCount;
    private String customerReviewAverage;
    private Date ds;
    private String vendor;
    private String categoryPath;
    private Integer quantity;

    public String getUpc() {
        return upc;
    }

    public void setUpc(String sku) {
        this.upc = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRegularPrice() {
        return regularPrice;
    }

    public void setRegularPrice(Double regularPrice) {
        this.regularPrice = regularPrice;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public Long getCustomerReviewCount() {
        return customerReviewCount;
    }

    public void setCustomerReviewCount(Long customerReviewCount) {
        this.customerReviewCount = customerReviewCount;
    }

    public String getCustomerReviewAverage() {
        return customerReviewAverage;
    }

    public void setCustomerReviewAverage(String customerReviewAverage) {
        this.customerReviewAverage = customerReviewAverage;
    }

    public Date getDs() {
        return ds;
    }

    public void setDs(Date ds) {
        this.ds = ds;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getCategoryPath() {
        return categoryPath;
    }

    public void setCategoryPath(String categoryPath) {
        this.categoryPath = categoryPath;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
