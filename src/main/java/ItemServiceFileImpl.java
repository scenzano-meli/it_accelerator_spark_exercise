import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class ItemServiceFileImpl implements ItemService {

    private FileOutputStream fileOut;
    private ObjectOutputStream out;
    private FileInputStream fileIn;
    private ObjectInputStream in;
    private String filePath = "/Users/scenzano/Desktop/Cenzano/" +
            "Cenzano-Work/IT Accelerator/Ejercicios/JAVA/Practico1-Spark/FilePersistence.ser";

    public ItemServiceFileImpl(){

        HashMap<String, List<Item>> itemSearchMap = new HashMap<String, List<Item>>();

        this.serializeObjectToFile (itemSearchMap);
    }

    private void serializeObjectToFile(HashMap<String, List<Item>> itemSearchMap){
        try {
            this.fileOut = new FileOutputStream(this.filePath);
            this.out = new ObjectOutputStream(fileOut);
            this.out.writeObject (itemSearchMap);
            out.close ();
            fileOut.close ();
        System.out.printf ("Serialized data is saved in /tmp/employee.ser");
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }

    private HashMap<String, List<Item>> getSerializedObjectFromFile() {

        HashMap<String, List<Item>> e = null;
        try {
            this.fileIn = new FileInputStream (this.filePath);
            this.in = new ObjectInputStream(this.fileIn);
            e = (HashMap<String, List<Item>>) this.in.readObject();
            this.in.close();
            this.fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("HashMap not found");
            c.printStackTrace();
        }

        return e;
    }

    @Override
    public void addItemsList(String key, List<Item> itemsList) {
        HashMap<String, List<Item>> itemSearchMap = this.getSerializedObjectFromFile();

        itemSearchMap.put(key, itemsList);

        this.serializeObjectToFile (itemSearchMap);
    }

    @Override
    public void addItem(String key, Item item) {
        HashMap<String, List<Item>> itemSearchMap = this.getSerializedObjectFromFile();
        List<Item> itemsList = itemSearchMap.get(key);

        if(itemsList == null)
        {
            itemsList = new ArrayList<Item> ();
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

        this.serializeObjectToFile (itemSearchMap);

    }

    @Override
    public Collection<Item> getItems(String key) {
        HashMap<String, List<Item>> itemSearchMap = this.getSerializedObjectFromFile();

        return itemSearchMap.get(key);
    }

    @Override
    public Item getItem(String key, String itemId) {
        HashMap<String, List<Item>> itemSearchMap = this.getSerializedObjectFromFile();

        return itemSearchMap.get(key)
                .stream()
                .filter(item -> itemId.equals(item.getId()))
                .findAny()
                .orElse(null);
    }

    @Override
    public Item editItem(String key, Item item) throws ItemException {
        HashMap<String, List<Item>> itemSearchMap = this.getSerializedObjectFromFile();
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
        HashMap<String, List<Item>> itemSearchMap = this.getSerializedObjectFromFile();
        List<Item> itemsList = itemSearchMap.get(key);

        if(itemsList != null){
            itemsList.remove(item);
        }

        itemSearchMap.put(key, itemsList);

        this.serializeObjectToFile (itemSearchMap);
    }

    @Override
    public boolean searchExists(String key) {
        HashMap<String, List<Item>> e = this.getSerializedObjectFromFile();
        return e.containsKey(key);
    }

    @Override
    public boolean itemSearchExists(String key, String itemId) {
        HashMap<String, List<Item>> itemSearchMap = this.getSerializedObjectFromFile();
        return itemSearchMap.get(key)
                .stream()
                .map(Item::getId)
                .anyMatch(itemId::equals);
    }
}
