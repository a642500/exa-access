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

package me.toxz.exp.rbac;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Carlos on 1/4/16.
 */
@DatabaseTable
public class Role implements UITreeItem, Comparable<Role> {
    @DatabaseField(generatedId = true) private int _id;
    @DatabaseField(canBeNull = false, unique = true) private String rolename;

    private Role() {
        // keep for ORMLite
    }

    public Role(String rolename) {
        this.rolename = rolename;
    }

    public int getId() {
        return _id;
    }

    @Override
    public boolean equals(java.lang.Object obj) {
        return obj instanceof Role && this._id == ((Role) obj)._id;
    }

    @Override
    public int hashCode() {
        return ("Role" + _id).hashCode();
    }

    @Override
    public String toString() {
        return rolename;
    }

    @Override
    public int compareTo(Role o) {
        if (o == null)
            throw new IncomparableException();
        return 0;//TODO implement compare
    }
}
