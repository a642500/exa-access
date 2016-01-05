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

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import me.toxz.exp.dac.data.DatabaseHelper;
import me.toxz.exp.dac.data.model.MObject;
import me.toxz.exp.dac.data.model.User;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by Carlos on 1/5/16.
 */
public class CenterSceneController implements Initializable {
    @FXML TreeItem<String> subjectTreeItem;
    @FXML TreeItem<String> objectTreeItem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            setUpTree();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setUpTree() throws SQLException {
        DatabaseHelper.open(User.class).queryForAll().stream().map(user -> new TreeItem<>(user.getUsername())).forEach(subjectTreeItem.getChildren()::add);
        DatabaseHelper.open(MObject.class).queryForAll().stream().map(obj -> new TreeItem<>(obj.getPath())).forEach(objectTreeItem.getChildren()::add);
    }
}