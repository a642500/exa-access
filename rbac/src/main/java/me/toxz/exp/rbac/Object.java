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
import com.sun.istack.internal.NotNull;

/**
 * Created by Carlos on 1/4/16.
 */
@DatabaseTable
public class Object {
    @DatabaseField(generatedId = true) private int _id;
    @DatabaseField(unique = true, canBeNull = false) private String path;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 2)
    private Role owner;

    private Object() {
        // keep for ORMLite
    }

    public Object(@NotNull String path, Role owner) {
        this.path = path;
        this.owner = owner;
    }

    public Role getOwner() {
        return owner;
    }

    public String getPath() {
        return path;
    }

    public void setPath(@NotNull String path) {
        this.path = path;
    }

    @Override
    public boolean equals(java.lang.Object obj) {
        return obj instanceof Object && this._id == ((Object) obj)._id;
    }

    @Override
    public String toString() {
        return path;
    }

    @Override
    public int hashCode() {
        return ("Object" + _id).hashCode();
    }
}
