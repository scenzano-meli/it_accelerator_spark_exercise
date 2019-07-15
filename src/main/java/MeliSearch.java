import static spark.Spark.*;
import com.google.gson.Gson;
import java.util.Arrays;
import java.util.List;

public class MeliSearch {

    public static void main(String[] args) {

        final ItemServiceConsumer itemServiceConsumer = new ItemServiceConsumer();


        /**
         * Lo referente a una busqueda
         */
        path("/search", () -> {


            /**
             * Lista los items de una busqueda
             *
             * @param search Item para realizar la busqueda de articulos.
             * @param filter opcional, para filtrar los resultados.
             *               "title": devuelve solo titulo de artículos
             *               "thumbnail": Devuelve items que contienen "good_quality_thumbnail"
             *               dentro del objeto "tags"
             *               "price-range": Devuelve artículos dentro de un rango de precios definido
             *               por el parametro "order"="min-max"
             *               "price": Ordena artículos por precios. Orden ascendente o descendente definido
             *               por parametro "order"=[asc | desc]
             *               "listing": Ordena artículos por atributo "listing_type". Orden ascendente o
             *               descendente definido por parametro "order"=[asc | desc]
             * @param order ordena resultados. Ver parametro filtro para mas detalles.
             * @return Devuelve los items de la busqueda.
             */
            get("/:search", (request, response) -> {
                response.type("application/json");
                String itemToSearch = "q=" +  request.params(":search");
                String searchFilter = request.queryParams("filter");
                String ordering = request.queryParams("order");
                return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS,
                        itemServiceConsumer.getElementsFromApi(itemToSearch, searchFilter, ordering)));
            });

            /**
             * Lista un item específico dentro de una busqueda
             *
             * @param search Item para realizar la busqueda de articulos.
             * @param id para buscar un item especifico dentro de la busqueda
             *
             * @return Devuelve el item de la busqueda
             */
            get("/:search/:id", (request, response) -> {
               response.type("application/json");
               String itemToSearch = "q=" +  request.params(":search");
               String idToSearch = request.params(":id");
               return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS,
                       itemServiceConsumer.getAElementFromApi(itemToSearch, idToSearch)));
            });

            /**
             * Inserta la lista resultado de una busqueda
             *
             * @param search Item sobre el cual se realiza la busqueda.
             * @param body Lista para insertar.
             *
             * @return Devuelve el resultado de la inserción
             */
            post("/:search", (request, response) -> {
                response.type("application/json");
                String searchItemToAdd= "q=" + request.params(":search");
                Item[] itemsArrays = new Gson().fromJson(request.body(), Item[].class);
                List<Item> itemsList = Arrays.asList(itemsArrays);
                itemServiceConsumer.addElementsToApi(searchItemToAdd, itemsList);
                return new Gson().toJson(new StandardResponse(
                        (StatusResponse.SUCCESS)));
            });


            /**
             * Inserta un item específico en una lista resultado de busqueda
             *
             * @param search Item sobre el cual se realiza la busqueda.
             * @param body item para insertar.
             *
             * @return Devuelve el resultado de la inserción
             */
            post("/:search/:id",  (request, response) -> {
                response.type("application/json");
                String searchKeyToAdd = "q=" + request.params(":search");
                String idItemToAdd = request.params(":id");
                Item itemToAdd = new Gson().fromJson(request.body(), Item.class);
                itemToAdd.setId(idItemToAdd);
                itemServiceConsumer.addElementToApi(searchKeyToAdd, itemToAdd);
                return new Gson().toJson(new StandardResponse(
                        (StatusResponse.SUCCESS)));
            });

            /**
             * Edita un item específico en una lista resultado de busqueda
             *
             * @param search Item sobre el cual se realiza la busqueda.
             * @param body item para editar.
             *
             * @return Devuelve el resultado de la edición
             */
            put("/:search/:id", (request, response) -> {
                response.type("application/json");
                String searchKeyToAdd = "q=" + request.params(":search");
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

            /**
             * Elimina una item específico de una lista resultado de busqueda
             *
             * @param search Item sobre el cual se realiza la busqueda.
             * @param body item para eliminar.
             *
             * @return Devuelve el resultado de la eliminación
             */
            delete("/:search", (request, response) -> {
                response.type("application/json");
                String searchKeyToDelete = "q=" + request.params(":search");
                Item itemToDelete = new Gson().fromJson(request.body(), Item.class);
                itemServiceConsumer.deleteElementFromApi(searchKeyToDelete, itemToDelete);

                return new Gson().toJson(new StandardResponse(
                        (StatusResponse.SUCCESS)));
            });

        });
    }


}
