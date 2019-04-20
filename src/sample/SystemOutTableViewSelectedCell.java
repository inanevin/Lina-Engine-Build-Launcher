package sample;


import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;

public final class SystemOutTableViewSelectedCell {
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void set(TableView tableView) {

        tableView.getSelectionModel().setCellSelectionEnabled(true);

        ObservableList selectedCells = tableView.getSelectionModel().getSelectedCells();

        selectedCells.addListener(new ListChangeListener() {
            @Override
            public void onChanged(ListChangeListener.Change c) {
                TablePosition tablePosition = (TablePosition) selectedCells.get(0);
                Object val = tablePosition.getTableColumn().getCellData(tablePosition.getRow());
                System.out.println("Selected Cell (Row: " + tablePosition.getRow() + " / Col: "
                        + tablePosition.getColumn() + ") Value: " + val + " / " + val.getClass());
            }
        });
    }
}