package sample;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javafx.util.Pair;

class PairKeyFactory implements Callback<TableColumn.CellDataFeatures<Pair<String, Object>, String>, ObservableValue<String>>
{
    @Override
    public ObservableValue<String> call(TableColumn.CellDataFeatures<Pair<String, Object>, String> data) {
        return new ReadOnlyObjectWrapper<>(data.getValue().getKey());
    }
}

class PairValueFactory implements Callback<TableColumn.CellDataFeatures<Pair<String, Object>, Object>, ObservableValue<Object>>
{
    @SuppressWarnings("unchecked")
    @Override
    public ObservableValue<Object> call(TableColumn.CellDataFeatures<Pair<String, Object>, Object> data) {
        Object value = data.getValue().getValue();
        return (value instanceof ObservableValue)
                ? (ObservableValue) value
                : new ReadOnlyObjectWrapper<>(value);
    }
}

class PairValueCell extends TableCell<Pair<String, Object>, Object>
{


    @Override
    protected void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null)
        {
            if (item instanceof String)
            {
                TextField textField = new TextField();
                textField.setText((String)item);

                textField.setOnKeyReleased(new EventHandler<KeyEvent>()
                {
                    @Override
                    public void handle(KeyEvent event) {
                        Main.OptionTableItemEdited(getIndex(), textField.getText());
                    }
                });

                setGraphic(textField);
            }
            else if (item instanceof Integer)
            {
                setText(Integer.toString((Integer) item));
                setGraphic(null);
            }
            else if (item instanceof Boolean)
            {
                CheckBox checkBox = new CheckBox();
                checkBox.setSelected((boolean) item);
                setGraphic(checkBox);

                checkBox.setOnAction(new EventHandler<ActionEvent>()
                {
                    @Override
                    public void handle(ActionEvent event) {
                        Main.OptionTableItemEdited(getIndex(), checkBox.isSelected());
                    }
                });

            }
            else
            {
                setText("N/A");
                setGraphic(null);
            }
        }
        else
        {
            setText(null);
            setGraphic(null);
        }
    }
}

/*
*  checkBox.setOnAction(new EventHandler<ActionEvent>()
                {
                    @Override
                    public void handle(ActionEvent event) {
                        BuildOption i = (BuildOption)getTableView().getItems().get(getIndex());
                        i.setParticularValue((Object)item);
                    }
                });
                */