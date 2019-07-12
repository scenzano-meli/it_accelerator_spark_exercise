import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.*;

public class ItemServiceConsumer {

    UrlConsumer urlConsumer = new UrlConsumer();
    final ItemService itemServiceMapImpl = new ItemServiceMapImpl();

    private List<Item> getItemsSearch(String itemToSearch) {
        List<Item> itemsList;

        if(itemServiceMapImpl.searchExists(itemToSearch)){
            itemsList = (List<Item>)itemServiceMapImpl.getItems(itemToSearch);
        }
        else{
            itemsList = urlConsumer.getListFromJsonUrl(itemToSearch);
            itemServiceMapImpl.addItemsList(itemToSearch, itemsList);
        }

        return itemsList;
    }

    public JsonElement getElementsFromApi(String itemToSearch, String filter, String ordering){
        JsonElement data;
        List<Item> itemsList = getItemsSearch(itemToSearch);

        data = new Gson().toJsonTree(itemsList);

        if(filter != null && filter.equals("titles")){

            List<String> titles = new ArrayList<String>();
            for(Item item : itemsList){
                titles.add(item.gettitle());
            }
            data = new Gson().toJsonTree(titles);
        }
        else if(filter != null && filter.equals("price")){
            if(ordering == null || ordering.equals("asc")){
                Collections.sort(itemsList, Comparator.comparingDouble(Item::getPrice));
            }
            else if(ordering.equals("desc")){
                Collections.sort(itemsList, Comparator.comparingDouble(Item::getPrice).reversed());
            }
            data = new Gson().toJsonTree(itemsList);
        }
        else if(filter != null && filter.equals("listing")){
            if(ordering == null || ordering.equals("asc")){
                Collections.sort(itemsList, Comparator.comparing(Item::getListing_type_id));
            }
            else if(ordering.equals("desc")){
                Collections.sort(itemsList, Comparator.comparing(Item::getListing_type_id).reversed());
            }
            data = new Gson().toJsonTree(itemsList);
        }

        return data;
    }

    public JsonElement getAElementFromApi(String searchParam, String itemIdToSearch){
        Item item = itemServiceMapImpl.getItem(searchParam, itemIdToSearch);

        return new Gson().toJsonTree(item);
    }

    public void addElementsToApi(String searchItem, List<Item> itemsList){
        itemServiceMapImpl.addItemsList(searchItem, itemsList);
    }

    public void addElementToApi(String searchItem, Item item){
        itemServiceMapImpl.addItem(searchItem, item);
    }

    public void editElementFromApi(String searchItem, Item item) throws ItemException {
        itemServiceMapImpl.editItem(searchItem, item);
    }


}
