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
        else{
            /**
             * List returned by Arrays.asList()
             * doesn't support add method
             */
            itemsList = new ArrayList<>(itemsList);
        }

        if(!itemsList.contains(item)){
            itemsList.add(item);
        }

        itemSearchMap.replace(key, itemsList);
    }

    @Override
    public Collection<Item> getItems(String key) {

        return itemSearchMap.get(key);
    }

    @Override
    public Item getItem(String key, String itemId) {
        return itemSearchMap.get(key)
                .stream()
                .filter(item -> itemId.equals(item.getId()))
                .findAny()
                .orElse(null);
    }

    @Override
    public Item editItem(String key, Item item) throws ItemException {
        Item itemToReturn = getItem(key, item.getId());

        if(itemToReturn != null){
            itemSearchMap.get(key)
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
    public void deleteItem(String key, Item item) {
        List<Item> itemsList = itemSearchMap.get(key);

        if(itemsList != null){
            itemsList.remove(item);
        }

        itemSearchMap.put(key, itemsList);
    }

    @Override
    public boolean searchExists(String key) {
        return itemSearchMap.containsKey(key);
    }

    @Override
    public boolean itemSearchExists(String key, String itemId) {
        return itemSearchMap.get(key)
                .stream()
                .map(Item::getId)
                .anyMatch(itemId::equals);
    }

}
