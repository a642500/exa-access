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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.toxz.exp.dac.data.DatabaseHelper;
import me.toxz.exp.dac.data.model.*;
import me.toxz.exp.dac.fx.animation.ShakeTransition;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by Carlos on 1/6/16.
 */
public class BlackTokenDialogController implements Initializable {
    private static Stage mStage;
    private static Consumer<BlackToken> mResultCallback;
    @FXML ComboBox<User> userComboBox;
    @FXML ComboBox<MObject> objectComboBox;
    @FXML ChoiceBox<AccessType> permissionChoiceBox;
    @FXML Label headLabel;

    public static void show(Consumer<BlackToken> resultCallback) throws IOException {
        mResultCallback = resultCallback;
        mStage = new Stage();
        mStage.initStyle(StageStyle.UTILITY);

        Parent parent = FXMLLoader.load(BlackTokenDialogController.class.getClassLoader().getResource("BlackTokenDialog.fxml"));
        Scene scene = new Scene(parent, 400, 300);
        mStage.setScene(scene);
        mStage.setTitle("Give black token");
        mStage.show();
    }

    public static void dismiss(BlackToken token) {
        mStage.close();
        mStage = null;
        if (mResultCallback != null) {
            mResultCallback.accept(token);
        }
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            User user = Main.getLoginUser();
            List<User> blackable = DatabaseHelper.getUserDao().queryForAll();
            blackable.remove(User.admin());
            blackable.remove(user);
            userComboBox.getItems().addAll(blackable);
            userComboBox.getSelectionModel().select(0);

            final Dao<AccessRecord, Integer> accessRecordDao = DatabaseHelper.getAccessRecordDao();
            List<AccessRecord> controllable = accessRecordDao.queryForMatching(new AccessRecord(user, null, AccessType.CONTROL, null));
            objectComboBox.getItems().addAll(controllable.stream().map(AccessRecord::getObject).collect(Collectors.toList()));

            permissionChoiceBox.getItems().addAll(AccessType.values());
            permissionChoiceBox.getSelectionModel().select(0);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void cancel(ActionEvent actionEvent) {
        dismiss(null);
    }

    public void ok(ActionEvent actionEvent) {
        User user = userComboBox.getSelectionModel().getSelectedItem();
        MObject object = objectComboBox.getSelectionModel().getSelectedItem();
        AccessType type = permissionChoiceBox.getSelectionModel().getSelectedItem();

        BlackToken token = new BlackToken(user, object, type);

        try {
            List<BlackToken> tokens = DatabaseHelper.getBlackTokenDao().queryForMatching(token);

            if (tokens.size() > 0) {
                headLabel.setText("Repeat give!");
                new ShakeTransition(mStage.getScene().getRoot(), event -> {
                }).playFromStart();
            } else {
                DatabaseHelper.getBlackTokenDao().create(token);
                dismiss(token);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
