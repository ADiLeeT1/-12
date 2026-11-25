// Model: an id + a text. Used in the drop-down lists (students, subjects).
public class Item {

    private final int id;
    private final String text;

    public Item(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    // JComboBox shows the result of toString()
    @Override
    public String toString() {
        return text;
    }
}
