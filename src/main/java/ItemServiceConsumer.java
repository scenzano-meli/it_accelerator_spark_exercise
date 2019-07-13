import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.*;
import java.util.stream.Collectors;

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

    private List<Item> orderList(List<Item> itemsList, String filter, String ordering){

        switch (filter){
            case "price":
                if(ordering == null || ordering.equals("asc")){
                    Collections.sort(itemsList, Comparator.comparingDouble(Item::getPrice));
                }
                else if(ordering.equals("desc")){
                    Collections.sort(itemsList, Comparator.comparingDouble(Item::getPrice).reversed());
                }
                break;
            case "listing":
                if(ordering == null || ordering.equals("asc")){
                    Collections.sort(itemsList, Comparator.comparing(Item::getListing_type_id));
                }
                else if(ordering.equals("desc")){
                    Collections.sort(itemsList, Comparator.comparing(Item::getListing_type_id).reversed());
                }
                break;


            default: break;
        };

        return itemsList;
    }

    public JsonElement getElementsFromApi(String itemToSearch, String filter, String ordering){
        JsonElement data;
        List<Item> itemsList = getItemsSearch(itemToSearch);

        data = new Gson().toJsonTree(itemsList);

        if(filter != null)
        {
            if(filter.equals("titles")){
                List<String> titles = new ArrayList<String>();
                for(Item item : itemsList){
                    titles.add(item.gettitle());
                }
                data = new Gson().toJsonTree(titles);
            }
            else if(filter.equals("thumbnail")){
                List<Item> itemsWithThumbnail  = new ArrayList<Item>();
                for(Item item : itemsList){
                    if(String.join(" ", item.getTags()).contains("good_quality_thumbnail")){
                        itemsWithThumbnail.add(item);
                    }
                }
                data = new Gson().toJsonTree(itemsWithThumbnail);
            }
            else if(filter.equals("price-range")){
                String[]rangeValues = (ordering == null ? "0-" + Integer.MAX_VALUE : ordering).split("-");
                List<Item> itemListFiltered = itemsList
                        .stream()
                        .filter(it -> (it.getPrice() > Integer.parseInt(rangeValues[0]) && it.getPrice() < Integer.parseInt(rangeValues[1])))
                        .collect(Collectors.toList());

                data = new Gson().toJsonTree(itemListFiltered);
            }
            else {
                orderList(itemsList, (filter == null ? "": filter), (ordering == null ? "": ordering));
                data = new Gson().toJsonTree(itemsList);
            }
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

    public void deleteElementFromApi(String searchItem, Item item) {
        itemServiceMapImpl.deleteItem(searchItem, item);
    }

}
