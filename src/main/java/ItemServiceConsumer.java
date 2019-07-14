import com.google.gson.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ItemServiceConsumer {

    UrlConsumer urlConsumer = new UrlConsumer();
    //Change to ItemServiceFileImpl to test file persistent version
    final ItemService itemServiceMapImpl = new ItemServiceMapImpl();
    List<Currency> currencysList;

    public ItemServiceConsumer (){

        this.currencysList = new ArrayList<Currency> ();

        Currency[] currencysArray;
        Gson gson = new Gson();
        String urlResponse= "";

        //need to work on catch
        try {
            urlResponse = urlConsumer.consumeUrl("https://api.mercadolibre.com/currencies", "");
        } catch (IOException e) {
            e.printStackTrace();
        }

        currencysArray = gson.fromJson(urlResponse, Currency[].class);
        this.currencysList = Arrays.asList(currencysArray);
    }

    public List<Item> getItemListFromJsonUrl(String searchValue){

        List<Item> itemsList = new ArrayList<Item>();
        Item[] itemsArray;
        Gson gson = new Gson();
        String urlResponse= "";

        //need to work on catch
        try {
            urlResponse = urlConsumer.consumeUrl("https://api.mercadolibre.com/sites/MLA/search?", searchValue);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * Here is De Deserialize we need to work on
         */

        GsonBuilder gsonBuilder = new GsonBuilder ();

        JsonDeserializer<Item> deserializer = new ItemDeserializer(this.currencysList);
        gsonBuilder.registerTypeAdapter(Item.class, deserializer);

        gson = gsonBuilder.create();

        JsonObject jobj = new Gson().fromJson(urlResponse, JsonObject.class);
        String resultsJsonAsText = jobj.get("results").toString();

        itemsArray = gson.fromJson(resultsJsonAsText, Item[].class);
        itemsList = Arrays.asList(itemsArray);

        return itemsList;
    }

    private List<Item> getItemsSearch(String itemToSearch) {
        List<Item> itemsList;

        if(itemServiceMapImpl.searchExists(itemToSearch)){
            itemsList = (List<Item>)itemServiceMapImpl.getItems(itemToSearch);
        }
        else{
            itemsList = this.getItemListFromJsonUrl(itemToSearch);
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
                    titles.add(item.getTitle());
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
