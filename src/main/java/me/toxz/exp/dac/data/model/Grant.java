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
import java.util.stream.Collectors;

/**
 * Created by Carlos on 1/7/16.
 */
public class Grant {
    private AccessType accessType;
    private List<AccessRecord> granted;
    private SimpleStringProperty grantedUsersName;

    public Grant(AccessType accessType, List<AccessRecord> granted) {
        this.accessType = accessType;
        this.granted = granted;
        this.grantedUsersName = new SimpleStringProperty(granted.stream().map(AccessRecord::getGrantedUser).map(User::getUsername).collect(Collectors.joining(" ")));
    }

    public AccessType getAccessType() {
        return accessType;
    }

    public List<AccessRecord> getGranted() {
        return granted;
    }

    public String getGrantedUsersName() {
        return grantedUsersName.get();
    }

    public SimpleStringProperty grantedUsersNameProperty() {
        return grantedUsersName;
    }
}
