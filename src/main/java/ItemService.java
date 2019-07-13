import java.util.Collection;
import java.util.List;

public interface ItemService {

    public void addItemsList(String key, List<Item> itemsList);
    public void addItem(String key, Item item);
    public Collection<Item> getItems(String key);
    public Item getItem(String key, String itemId);
    public Item editItem(String key, Item item)
        throws ItemException;
    public void deleteItem(String key, Item item);
    public boolean searchExists(String key);
    public boolean itemSearchExists(String key, String itemId);
}
