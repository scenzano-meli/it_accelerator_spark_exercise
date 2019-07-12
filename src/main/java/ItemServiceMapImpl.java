import java.util.*;

public class ItemServiceMapImpl implements ItemService {

    private HashMap<String, List<Item>> itemSearchMap;

    public ItemServiceMapImpl(){

        itemSearchMap = new HashMap<String, List<Item>>();
    }

    @Override
    public void addItemsList(String key, List<Item> itemsList) {

        itemSearchMap.put(key, itemsList);
    }

    @Override
    public void addItem(String key, Item item) {

        List<Item> itemsList = itemSearchMap.get(key);

        if(itemsList == null)
        {
            itemsList = new ArrayList<Item>();
        }

        itemsList.add(item);

        itemSearchMap.put(key, itemsList);
    }

    @Override
    public Collection<Item> getItems(String key) {

        return itemSearchMap.get(key);
    }

    @Override
    public Item getItem(String searchId, String itemId) {
        return itemSearchMap.get(searchId)
                .stream()
                .filter(item -> itemId.equals(item.getId()))
                .findAny()
                .orElse(null);
    }

    @Override
    public Item editItem(String searchId, Item item) throws ItemException {
        Item itemToReturn = getItem(searchId, item.getId());

        if(itemToReturn != null){
            itemSearchMap.get(searchId)
                    .stream()
                    .map(o -> o.getId().equals(item.getId()) ? item : o)
                    .findAny()
                    .orElse(null);
        }
        else
        {
            throw new ItemException("No existe un item con ese ID");
        }

        return itemToReturn;
    }

    @Override
    public void deleteItem(String id) {

    }

    @Override
    public boolean searchExists(String id) {
        return itemSearchMap.containsKey(id);
    }

    @Override
    public boolean itemSearchExists(String searchId, String itemId) {
        return itemSearchMap.get(searchId)
                .stream()
                .map(Item::getId)
                .anyMatch(itemId::equals);
    }

}
