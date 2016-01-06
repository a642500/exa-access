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

import com.j256.ormlite.dao.Dao;
import com.sun.istack.internal.NotNull;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import me.toxz.exp.dac.data.DatabaseHelper;
import me.toxz.exp.dac.data.model.*;
import me.toxz.exp.dac.fx.animation.ShakeTransition;
import org.controlsfx.control.CheckListView;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static me.toxz.exp.dac.data.DatabaseHelper.*;
import static me.toxz.exp.dac.data.model.User.admin;

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
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            mCurrentJect = newValue.getValue();
            refreshTable(newValue.getValue());
        });
    }

    private void refreshTable(@NotNull final Ject ject) {
        AccessRecord match;
        if (ject instanceof User) match = new AccessRecord(((User) ject), null, null, null);
        else if (ject instanceof MObject) match = new AccessRecord(null, ((MObject) ject), null, null);
        else throw new IllegalArgumentException();

        List<Access> accesses = null;
        try {
            accesses = DatabaseHelper.getAccessRecordDao().queryForMatching(match).stream().collect(Collectors.groupingBy(accessRecord -> ject instanceof User ? accessRecord.getObject() : accessRecord.getSubject())).values().stream().map(Access::new).collect(Collectors.toList());
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

    public void onRevoke(ActionEvent actionEvent) throws SQLException {
        Dialog<List<AccessRecord>> revokeDialog = new Dialog<>();
        revokeDialog.setTitle("Revoke");
        revokeDialog.setHeaderText("Please check permission you want to revoke");

        ButtonType revokeButtonType = new ButtonType("Revoke", ButtonBar.ButtonData.OK_DONE);
        ButtonType revokeAllButtonType = new ButtonType("Revoke All", ButtonBar.ButtonData.OK_DONE);
        revokeDialog.getDialogPane().getButtonTypes().addAll(revokeButtonType, revokeAllButtonType, ButtonType.CANCEL);


        final List<AccessRecord> accessRecordList = DatabaseHelper.getAccessRecordDao().queryForMatching(new AccessRecord(null, null, null, Main.getLoginUser()));
        final CheckListView<AccessRecord> listView = new CheckListView<AccessRecord>(new ObservableListWrapper<>(accessRecordList));

        revokeDialog.getDialogPane().setContent(listView);
        revokeDialog.setResultConverter(param -> {
            if (param == revokeAllButtonType) {
                return accessRecordList;
            } else if (param == revokeButtonType) {
                return listView.getCheckModel().getCheckedItems();
            }
            return null;
        });

        Optional<List<AccessRecord>> result = revokeDialog.showAndWait();

        result.ifPresent(this::deleteAccessRecords);
    }

    private void revoke(AccessRecord record) {
        try {

            User user = record.getSubject();
            final MObject object = record.getObject();
            Dao<AccessRecord, Integer> dao = DatabaseHelper.getAccessRecordDao();
            dao.delete(record);

            List<AccessRecord> accessRecords = dao.queryForMatching(new AccessRecord(null, object, null, user));
            for (AccessRecord accessRecord : accessRecords) {
                AccessRecord match = new AccessRecord(user, object, accessRecord.getAccessType(), null);
                if (dao.queryForMatching(match).size() == 0 || accessRecord.getAccessType() != AccessType.CONTROL && dao.queryForMatching(new AccessRecord(user, object, AccessType.CONTROL, null)).size() == 0) {
                    revoke(accessRecord);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteAccessRecords(List<AccessRecord> toDeletes) {
        if (toDeletes == null) {
            return;
        }
        toDeletes.forEach(this::revoke);
        refreshTable(mCurrentJect);
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
                    User user = Main.getLoginUser();
                    final MObject o = new MObject(s, user);
                    getMObjectDao().create(o);
                    List<AccessRecord> accessRecords = Arrays.stream(AccessType.values()).map(accessType -> new AccessRecord(user, o, accessType, admin())).collect(Collectors.toList());
                    for (AccessRecord accessRecord : accessRecords) {
                        getAccessRecordDao().create(accessRecord);
                    }
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
