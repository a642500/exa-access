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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.toxz.exp.dac.data.DatabaseHelper;
import me.toxz.exp.dac.data.model.AccessType;
import me.toxz.exp.dac.data.model.MObject;
import me.toxz.exp.dac.data.model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by Carlos on 1/6/16.
 */
public class GrantDialogController implements Initializable {
    private static Stage mStage;
    @FXML ComboBox<User> userComboBox;
    @FXML ComboBox<MObject> objectComboBox;
    @FXML ChoiceBox<AccessType> permissionChoiceBox;

    public static void show() throws IOException {
        mStage = new Stage();
        mStage.initStyle(StageStyle.UTILITY);

        Parent parent = FXMLLoader.load(GrantDialogController.class.getClassLoader().getResource("GrantDialog.fxml"));
        Scene scene = new Scene(parent, 400, 300);
        mStage.setScene(scene);
        mStage.setTitle("Grant");
        mStage.show();
    }

    public static void dismiss() {
        mStage.close();
        mStage = null;
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            userComboBox.getItems().addAll(DatabaseHelper.open(User.class).queryForAll());
            userComboBox.getSelectionModel().select(0);
            objectComboBox.getItems().addAll(DatabaseHelper.open(MObject.class).queryForAll());
            objectComboBox.getSelectionModel().select(0);
            permissionChoiceBox.getItems().addAll(AccessType.values());
            permissionChoiceBox.getSelectionModel().select(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void cancel(ActionEvent actionEvent) {
        dismiss();
    }

    public void ok(ActionEvent actionEvent) {

    }
}
