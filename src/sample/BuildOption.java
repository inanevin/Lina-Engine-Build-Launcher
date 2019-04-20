package sample;

import javafx.beans.property.*;

public class BuildOption<T> {

    private final StringProperty firstName;
    private final ObjectProperty<T> particularValue;

    public BuildOption( String id, T val ) {
        this.firstName = new SimpleStringProperty(id);
        this.particularValue = new SimpleObjectProperty<T>(val);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public T getParticularValue() {
        return particularValue.get();
    }

    public void setParticularValue(T particularValue) {
        this.particularValue.set(particularValue);
    }

    public ObjectProperty<T> particularValueProperty() {
        return particularValue;
    }

}