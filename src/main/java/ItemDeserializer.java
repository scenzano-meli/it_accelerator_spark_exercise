import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.List;

public class ItemDeserializer implements JsonDeserializer<Item> {

    private List<Currency> currencysList;

    public ItemDeserializer(){
        super();
    }

    public ItemDeserializer(List<Currency> currencysList){
        super();
        this.currencysList = currencysList;
    }

    @Override
    public Item deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        final JsonObject jsonObject = jsonElement.getAsJsonObject ();

        final JsonElement jsonId = jsonObject.get("id");
        final String id = jsonId.getAsString();

        final JsonElement jsonSiteId = jsonObject.get("site_id");
        final JsonElement jsonTitle = jsonObject.get("title");
        final JsonElement jsonPrice = jsonObject.get("price");
        final JsonElement jsonCurrency = jsonObject.get("currency_id");
        final JsonElement jsonListingTypeId = jsonObject.get("listing_type_id");
        final JsonElement jsonStopTime = jsonObject.get("stop_time");
        final JsonElement jsonThumbnail = jsonObject.get("thumbnail");
        final JsonArray jsonTagsArray = jsonObject.get("tags").getAsJsonArray();
        final String[] tags = new String[jsonTagsArray.size ()];
        for(int i = 0; i < tags.length; i++){
            final JsonElement jsonTag = jsonTagsArray.get (i);
            tags[i] = jsonTag.getAsString ();
        }

        Currency currency = this.currencysList
                                .stream()
                                .filter (x -> x.getId ().equals (jsonObject.get("currency_id").getAsString ()))
                                .findFirst()
                                .orElse(null);

        final Item item = new Item(
                jsonId.getAsString(),
                jsonSiteId.getAsString(),
                jsonTitle.getAsString(),
                jsonPrice.getAsDouble(),
                currency,
                jsonListingTypeId.getAsString(),
                jsonStopTime.getAsString(),
                jsonThumbnail.getAsString(),
                tags);

        return item;
    }

}
