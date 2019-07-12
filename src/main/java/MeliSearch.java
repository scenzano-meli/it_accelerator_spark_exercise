import static spark.Spark.*;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class MeliSearch {

    public static void main(String[] args) {

        final ItemServiceConsumer itemServiceConsumer = new ItemServiceConsumer();

        path("/api", () -> {

            get("/search/:search", (request, response) -> {
                response.type("application/json");
                String itemToSearch = "q=" +  request.params(":search");
                String searchFilter = request.queryParams("filter");
                String ordering = request.queryParams("order");
                return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS,
                        itemServiceConsumer.getElementsFromApi(itemToSearch, searchFilter, ordering)));
            });


            get("/search/:search/:id", (request, response) -> {
               response.type("application/json");
               String itemToSearch = "q=" +  request.params(":search");
               String idToSearch = request.params(":id");
               return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS,
                       itemServiceConsumer.getAElementFromApi(itemToSearch, idToSearch)));
            });

            post("/search/:search", (request, response) -> {
                response.type("application/json");
                String searchItemToAdd= "q=" + request.params(":search");
                Item[] itemsArrays = new Gson().fromJson(request.body(), Item[].class);
                List<Item> itemsList = Arrays.asList(itemsArrays);
                itemServiceConsumer.addElementsToApi(searchItemToAdd, itemsList);
                return new Gson().toJson(new StandardResponse(
                        (StatusResponse.SUCCESS)));
            });

            post("/search/:search/:id", (request, response) -> {
                response.type("application/json");
                String searchKeyToAdd = "q=" + request.params(":search");
                String idItemToAdd = request.params(":id");
                Item itemToAdd = new Gson().fromJson(request.body(), Item.class);
                itemToAdd.setId(idItemToAdd);
                itemServiceConsumer.addElementToApi(searchKeyToAdd, itemToAdd);
                return new Gson().toJson(new StandardResponse(
                        (StatusResponse.SUCCESS)));
            });

            put("/search/:search/:id", (request, response) -> {
                response.type("application/json");
                String searchKeyToAdd= "q=" + request.params(":search");
                String idItemToEdit = request.params(":id");
                Item itemToAdd = new Gson().fromJson(request.body(), Item.class);
                itemToAdd.setId(idItemToEdit);
                StandardResponse standardResponse;
                try{
                    itemServiceConsumer.editElementFromApi(searchKeyToAdd, itemToAdd);
                    standardResponse = new StandardResponse((StatusResponse.SUCCESS));
                }
                catch (ItemException e) {
                    standardResponse = new StandardResponse((StatusResponse.ERROR));
                }

                return new Gson().toJson(standardResponse);
            });

        });
    }


}
