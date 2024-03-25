package javaTask;

public class Box <T> {

    private T value;
    // Method to set the value of the object
    public void setValue(T value) {
        this.value = value;
    }
    // Method to get the value of the object
    public T getValue() {
        return value;
    }
    public static void main(String[] args) {
        // Example usage
        Box<Integer> integerBox = new Box<>();
        integerBox.setValue(10);
        System.out.println("Integer value: " + integerBox.getValue());
        Box<String> stringBox = new Box<>();
        stringBox.setValue("Hello, World!");
        System.out.println("String value: " + stringBox.getValue());
    }
}
//    This Box<T> class is generic and can store objects of any type. It includes methods to set and get the value of the object. The type parameter T is used to specify the type of the object being stored.