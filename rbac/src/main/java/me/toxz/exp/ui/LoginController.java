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

package me.toxz.exp.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import me.toxz.exp.rbac.Role;
import me.toxz.exp.rbac.data.DatabaseHelper;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Carlos on 1/4/16.
 */
public class LoginController extends AnchorPane implements Initializable {
    @FXML TextField userId;
    @FXML PasswordField password;
    @FXML Button login;
    @FXML Label errorMessage;
    @FXML ImageView imageView;

    private void goToMainScene() throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("CenterScene.fxml"));
        Main.mPrimaryStage.setScene(new Scene(root, 1200, 800));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void processLogin(ActionEvent event) throws Exception {
        final String username = userId.getText();
        final String pass = password.getText();

        final Role role = DatabaseHelper.getUserDao().queryBuilder().where().eq("username", username).queryForFirst();

        if (role != null) {
            if (role.isPasswordValidate(pass)) {
                errorMessage.setText("Login success.");
                Main.setLoginUser(role);
                goToMainScene();
            } else {
                errorMessage.setText("Account or password is incorrect");
            }
        } else {
            errorMessage.setText("Account doesn't exist!");
        }
    }

    public void processSignUp(ActionEvent actionEvent) {
        Dialog<Role> signUpDialog = new Dialog<>();

        DialogPane pane = signUpDialog.getDialogPane();


    }
}
