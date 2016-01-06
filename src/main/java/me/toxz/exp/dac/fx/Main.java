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
import me.toxz.exp.dac.data.DatabaseHelper;
import me.toxz.exp.dac.data.model.AccessRecord;
import me.toxz.exp.dac.data.model.AccessType;
import me.toxz.exp.dac.data.model.MObject;
import me.toxz.exp.dac.data.model.User;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Carlos on 1/4/16.
 */
public class Main extends Application {
    protected static Stage mPrimaryStage;
    public static void main(String[] args) {
        launch(args);
    }



    private void authenticate(User subject, AccessType type, MObject object) throws SQLException {
        AccessRecord accessRecord = new AccessRecord(subject, object, type);
        DatabaseHelper.getAccessRecordDao().create(accessRecord);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        mPrimaryStage = primaryStage;
        primaryStage.setTitle("Login");
        primaryStage.setScene(getFirstScene());
        primaryStage.show();
    }

    private Scene getFirstScene() throws IOException {
        //        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Login.fxml"));
        //        return new Scene(root, 400, 300);
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("CenterScene.fxml"));
        return new Scene(root, 1200, 800);
    }

    private void revoke(AccessRecord record) throws SQLException {
        DatabaseHelper.getAccessRecordDao().delete(record);
    }
}

