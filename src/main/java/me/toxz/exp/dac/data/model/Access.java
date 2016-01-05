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

/**
 * Created by Carlos on 1/5/16.
 */
public class Access {
    private List<AccessRecord> mRecordList;
    private SimpleStringProperty username;
    private SimpleStringProperty objectPath;
    private AccessType[] mPermissions;

    public Access(List<AccessRecord> mRecordList) {
        AccessRecord record = mRecordList.get(0);
        username = new SimpleStringProperty(record.getSubject().getUsername());
        objectPath = new SimpleStringProperty(record.getObject().getPath());
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

    public AccessType[] getmPermissions() {
        return mPermissions;
    }

    public void setmPermissions(AccessType[] mPermissions) {
        this.mPermissions = mPermissions;
    }
}
