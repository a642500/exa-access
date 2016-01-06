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

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.sun.istack.internal.NotNull;

/**
 * Created by Carlos on 1/4/16.
 */
@DatabaseTable
public class MObject implements Ject {
    @DatabaseField(generatedId = true)
    private int _id;
    @DatabaseField(unique = true, canBeNull = false)
    private String path;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 2) private User owner;

    private MObject() {
        // keep for ORMLite
    }

    public MObject(@NotNull String path, User owner) {
        this.path = path;
        this.owner = owner;
    }

    public User getOwner() {
        return owner;
    }

    public String getPath() {
        return path;
    }

    public void setPath(@NotNull String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MObject && this._id == ((MObject) obj)._id;
    }

    @Override
    public String toString() {
        return path;
    }
}
