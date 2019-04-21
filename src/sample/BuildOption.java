package sample;

import javafx.beans.property.*;

public class BuildOption {

    private final StringProperty FirstName;
    private final ObjectProperty ParticularValue;

    public BuildOption( String id, Object val ) {
        this.FirstName = new SimpleStringProperty(id);
        this.ParticularValue = new SimpleObjectProperty(val);
    }

    public String getFirstName() {
        return FirstName.get();
    }

    public void setFirstName(String firstName) {
        this.FirstName.set(firstName);
    }

    public StringProperty firstNameProperty() {
        return FirstName;
    }

    public Object getParticularValue() {
        return ParticularValue.get();
    }

    public void setParticularValue(Object particularValue) {
        this.ParticularValue.set(particularValue);
    }

    public ObjectProperty particularValueProperty() {
        return ParticularValue;
    }

}