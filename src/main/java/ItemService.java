import java.util.Collection;
import java.util.List;

public interface ItemService {

    public void addItemsList(String key, List<Item> itemsList);
    public void addItem(String key, Item item);
    public Collection<Item> getItems(String key);
    public Item getItem(String searchId, String itemId);
    public Item editItem(String searchId, Item item)
        throws ItemException;
    public void deleteItem(String id);
    public boolean searchExists(String id);
    public boolean itemSearchExists(String searchId, String itemId);
}
