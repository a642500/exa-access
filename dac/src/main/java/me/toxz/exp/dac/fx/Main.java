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

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.toxz.exp.dac.data.model.User;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Carlos on 1/4/16.
 */
public class Main extends Application {
    protected static Stage mPrimaryStage;
    private static User mUser;

    public static void main(String[] args) {
        launch(args);
    }

    public static User getLoginUser() {
        return mUser;
    }

    static void setLoginUser(User user) {
        mUser = user;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        mPrimaryStage = primaryStage;
        goToLogin();
        primaryStage.show();
    }

    public static void goToLogin() throws IOException, SQLException {
        mPrimaryStage.setTitle("Login");
        mPrimaryStage.setScene(getFirstScene());
    }

    private static Scene getFirstScene() throws IOException, SQLException {
        Parent root = FXMLLoader.load(Main.class.getClassLoader().getResource("Login.fxml"));
        return new Scene(root, 400, 300);
        //TODO just for test, auto login by admin
        //        final User user = DatabaseHelper.getUserDao().queryBuilder().where().eq("username", "admin").queryForFirst();
        //
        //        setLoginUser(user);
        //        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("CenterScene.fxml"));
        //        return new Scene(root, 1200, 800);
    }
}

