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

import com.sun.istack.internal.NotNull;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import me.toxz.exp.dac.data.DatabaseHelper;
import me.toxz.exp.dac.data.model.*;
import me.toxz.exp.dac.fx.animation.ShakeTransition;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static me.toxz.exp.dac.data.DatabaseHelper.getMObjectDao;
import static me.toxz.exp.dac.data.DatabaseHelper.getUserDao;

/**
 * Created by Carlos on 1/5/16.
 */
public class CenterSceneController implements Initializable {
    @FXML TreeItem<Ject> subjectTreeItem;
    @FXML TreeItem<Ject> objectTreeItem;
    @FXML TableView<Access> tableView;
    @FXML TableColumn<Access, User> subjectColumn;
    @FXML TreeView<Ject> treeView;

    private Ject mCurrentJect;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            setUpTree();
            refreshTree();
            mCurrentJect = Main.getLoginUser();
            refreshTable(mCurrentJect);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshTree() throws SQLException {
        List<TreeItem<Ject>> userItems = getUserDao().queryForAll().stream().map((Function<User, TreeItem<Ject>>) TreeItem::new).collect(Collectors.toList());
        List<TreeItem<Ject>> objectItems = getMObjectDao().queryForAll().stream().map((Function<MObject, TreeItem<Ject>>) TreeItem::new).collect(Collectors.toList());

        subjectTreeItem.getChildren().setAll(userItems);
        objectTreeItem.getChildren().setAll(objectItems);
    }

    private void setUpTree() throws SQLException {
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> refreshTable(newValue.getValue()));
    }

    private void refreshTable(@NotNull final Ject ject) {
        AccessRecord match;
        if (ject instanceof User) match = new AccessRecord(((User) ject), null, null);
        else if (ject instanceof MObject) match = new AccessRecord(null, ((MObject) ject), null);
        else throw new IllegalArgumentException();

        List<Access> accesses = null;
        try {
            //TODO not merge
            accesses = DatabaseHelper.getAccessRecordDao().queryForMatching(match).stream().collect(Collectors.groupingBy(AccessRecord::getObject)).values().stream().map(Access::new).collect(Collectors.toList());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableView.getItems().setAll(accesses);
    }

    public void onGrant(ActionEvent actionEvent) throws IOException {
        GrantDialogController.show(accessRecord -> {
            if (accessRecord != null) {
                if (accessRecord.getSubject().equals(mCurrentJect) || accessRecord.getObject().equals(mCurrentJect))
                    refreshTable(mCurrentJect);
            }
        });
    }

    public void onRevoke(ActionEvent actionEvent) {

    }

    public void onCreateObject(ActionEvent actionEvent) {
        final TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New object");
        dialog.setContentText("Path");
        dialog.setHeaderText("Please input information:");

        final TextField textField = dialog.getEditor();

        final Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        final Consumer<Node> shake = node -> new ShakeTransition(node, event -> {
        }).playFromStart();
        okButton.addEventFilter(ActionEvent.ACTION, ae -> {
            String path = textField.getText();


            if (path == null || path.length() == 0) {
                shake.accept(textField);
                dialog.setHeaderText("Please input the path!");
                ae.consume();
            } else try {
                if (!getMObjectDao().queryForMatching(new MObject(path, null)).isEmpty()) {
                    shake.accept(textField);
                    dialog.setHeaderText("Object path exist! Try another one.");
                    ae.consume();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                dialog.setHeaderText("Error! " + e.getMessage());
            }
        });

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(s -> {
            try {
                DatabaseHelper.callInTransaction(() -> {
                    final MObject o = new MObject(s, Main.getLoginUser());
                    getMObjectDao().create(o);
                    //                    final MObject created = getMObjectDao().queryForMatching(o).get(0);
                    return null;
                });
                refreshTree();
                refreshTable(mCurrentJect);
                //TODO refresh table
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
