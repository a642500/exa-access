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

package me.toxz.exp.dac.fx.bean;

import javafx.beans.property.SimpleStringProperty;
import me.toxz.exp.dac.data.DatabaseHelper;
import me.toxz.exp.dac.data.model.AccessRecord;
import me.toxz.exp.dac.data.model.AccessType;
import me.toxz.exp.dac.data.model.BlackToken;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Carlos on 1/5/16.
 */
public class Access {
    private List<AccessRecord> mRecordList;
    private SimpleStringProperty username;
    private SimpleStringProperty objectPath;
    private SimpleStringProperty permissions;
    private List<Grant> grants;

    public Access(List<AccessRecord> mRecordList) {
        AccessRecord record = mRecordList.get(0);
        username = new SimpleStringProperty(record.getSubject().getUsername());
        objectPath = new SimpleStringProperty(record.getObject().getPath());
        final Map<AccessType, List<BlackToken>> blockList = new HashMap<>();
        try {
            List<BlackToken> tokens = DatabaseHelper.getBlackTokenDao().queryForMatching(new BlackToken(record.getSubject(), record.getObject(), null));
            blockList.putAll(tokens.stream().collect(Collectors.groupingBy(BlackToken::getAccessType)));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Map<AccessType, List<AccessRecord>> grantList = mRecordList.stream().collect(Collectors.groupingBy(AccessRecord::getAccessType));
        permissions = new SimpleStringProperty(grantList.keySet().stream().map(accessType -> {
            String name = accessType.name();
            List<BlackToken> those = blockList.get(accessType);
            if (those != null && those.size() > 0) {
                name = name + "(Invalid)";
            }
            return name;
        }).collect(Collectors.joining(", ")));
        grants = grantList.entrySet().stream().map(accessTypeListEntry -> new Grant(accessTypeListEntry.getKey(), accessTypeListEntry.getValue())).collect(Collectors.toList());
    }

    public List<AccessRecord> getmRecordList() {
        return mRecordList;
    }

    public void setmRecordList(List<AccessRecord> mRecordList) {
        this.mRecordList = mRecordList;
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public String getObjectPath() {
        return objectPath.get();
    }

    public void setObjectPath(String objectPath) {
        this.objectPath.set(objectPath);
    }

    public SimpleStringProperty objectPathProperty() {
        return objectPath;
    }

    public String getPermissions() {
        return permissions.get();
    }

    public SimpleStringProperty permissionsProperty() {
        return permissions;
    }

    public List<Grant> getGrants() {
        return grants;
    }
}
