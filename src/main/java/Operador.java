import java.util.List;

public class Operador<T>{

    private T element;

    public <T extends Comparable<T>> T getMinElement(List<T> array){

        T elementoAComparar = array.get(0);

        for(int i = 1; i < array.size(); i++) {

            if (elementoAComparar.compareTo(array.get(i)) > 0){
                elementoAComparar = array.get(i);
            }
        }
        return elementoAComparar;
    }

    public <T extends Comparable<T>> T getMaxElement(List<T> array) {

        T elementoAComparar = array.get(0);

        for (int i = 1; i < array.size(); i++) {

            if (elementoAComparar.compareTo(array.get(i)) < 0) {
                elementoAComparar = array.get(i);
            }
        }
        return elementoAComparar;
    }

    public <T extends Comparable<T>> int getIndexByElement(List<T> array, T elementoAComparar) {

        for (int i = 0; i < array.size(); i++) {

            if (elementoAComparar.equals(array.get(i))) {
                return i;
            }
        }

        return -1;
    }


}
