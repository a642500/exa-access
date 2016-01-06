/*
 *     Copyright (C) 2016 Carlos
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License along
 *     with this program; if not, write to the Free Software Foundation, Inc.,
 *     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package me.toxz.exp.dac.fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import me.toxz.exp.dac.data.DatabaseHelper;
import me.toxz.exp.dac.data.model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Carlos on 1/5/16.
 */
public class CenterSceneController implements Initializable {
    @FXML TreeItem<Ject> subjectTreeItem;
    @FXML TreeItem<Ject> objectTreeItem;
    @FXML TableView<Access> tableView;
    @FXML TableColumn<Access, User> subjectColumn;
    @FXML TreeView<Ject> treeView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            setUpTree();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setUpTree() throws SQLException {
        DatabaseHelper.open(User.class).queryForAll().stream().map((Function<User, TreeItem<Ject>>) TreeItem::new).forEach(subjectTreeItem.getChildren()::add);
        DatabaseHelper.open(MObject.class).queryForAll().stream().map((Function<MObject, TreeItem<Ject>>) TreeItem::new).forEach(objectTreeItem.getChildren()::add);

        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateTable(newValue.getValue()));

        updateTable(DatabaseHelper.open(User.class).queryForAll().get(0));
    }

    private void updateTable(Ject ject) {

        AccessRecord match;
        if (ject instanceof User) match = new AccessRecord(((User) ject), null, null);
        else if (ject instanceof MObject) match = new AccessRecord(null, ((MObject) ject), null);
        else throw new IllegalArgumentException();

        List<Access> accesses = null;
        try {
            //TODO not merge
            accesses = DatabaseHelper.open(AccessRecord.class).queryForMatching(match).stream().collect(Collectors.groupingBy(AccessRecord::getObject)).values().stream().map(Access::new).collect(Collectors.toList());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableView.getItems().setAll(accesses);
    }

    public void onGrant(ActionEvent actionEvent) throws IOException {
        GrantDialogController.show();
    }

    public void onRevoke(ActionEvent actionEvent) {

    }

    public void onCreateObject(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New object");
        dialog.setContentText("Object path");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(s -> {
            
        });
    }
}
