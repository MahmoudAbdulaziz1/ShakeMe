package shake.android.mahmoud.learnshake;

public class Item {

    private String title;
    private String description;

    public Item(String title, String description) {
        super();
        this.title = title;
        this.description = description;
    }
    // getters and setters...

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {

        return description;
    }

    public String getTitle() {
        return title;
    }
}