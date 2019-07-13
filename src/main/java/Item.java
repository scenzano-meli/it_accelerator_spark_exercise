import java.io.Serializable;
import java.util.Arrays;

public class Item implements Serializable {

    private String id;
    private String site_id;
    private String title;
    private double price;
    private String currency_id;
    private String listing_type_id;
    private String stop_time;
    private String thumbnail;
    private String[] tags;

    public String getId() {
        return id;
    }

    public void setId(String id) {this.id = id;}

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(String currency_id) {
        this.currency_id = currency_id;
    }

    public String getListing_type_id() {
        return listing_type_id;
    }

    public void setListing_type_id(String listing_type_id) {
        this.listing_type_id = listing_type_id;
    }

    public String getStop_time() {
        return stop_time;
    }

    public void setStop_time(String stop_time) {
        this.stop_time = stop_time;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    /*
    @Override
    public int compareTo(Item elementoComparado) {
        int comparationValue = 0;

        switch(criterioParaOrdernar) {
            case LISTING_TYPE:
                if (this.id < elementoComparado.id){
                    comparationValue = -1;
                }
                else if(this.id > elementoComparado.id){
                    comparationValue = 1;
                }
                else{
                    comparationValue = 0;
                }
                break;
        }

        return 0;
    }
    */

    public boolean equals(Item elementoAComparar){
        boolean areEqual = false;

        if((this.id == elementoAComparar.id) &&
                (this.id.compareTo(elementoAComparar.id) == 0) &&
                (this.site_id.compareTo(elementoAComparar.site_id) == 0) &&
                (this.title.compareTo(elementoAComparar.title) == 0) &&
                (this.price == elementoAComparar.price) &&
                (this.currency_id.compareTo(elementoAComparar.currency_id) == 0) &&
                (this.listing_type_id.compareTo(elementoAComparar.listing_type_id) == 0) &&
                (this.stop_time.compareTo(elementoAComparar.stop_time) == 0) &&
                (this.thumbnail.compareTo(elementoAComparar.thumbnail) == 0) &&
                (Arrays.equals(this.tags, elementoAComparar.tags)))
            areEqual = true;
        else{
            areEqual = false;
        }

        return areEqual;
    }
}
