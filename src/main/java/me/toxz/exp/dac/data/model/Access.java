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

package me.toxz.exp.dac.data.model;

import javafx.beans.property.SimpleStringProperty;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Carlos on 1/5/16.
 */
public class Access {
    private List<AccessRecord> mRecordList;
    private Map<AccessType, List<AccessRecord>> mGrantList;
    private SimpleStringProperty username;
    private SimpleStringProperty objectPath;
    private SimpleStringProperty permissions;

    public Access(List<AccessRecord> mRecordList) {
        AccessRecord record = mRecordList.get(0);
        username = new SimpleStringProperty(record.getSubject().getUsername());
        objectPath = new SimpleStringProperty(record.getObject().getPath());
        mGrantList = mRecordList.stream().collect(Collectors.groupingBy(AccessRecord::getAccessType));
        permissions = new SimpleStringProperty(mGrantList.keySet().stream().map(Enum::toString).collect(Collectors.joining(",")));
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

    public Map<AccessType, List<AccessRecord>> getmGrantList() {
        return mGrantList;
    }

    public String getPermissions() {
        return permissions.get();
    }

    public SimpleStringProperty permissionsProperty() {
        return permissions;
    }
}
