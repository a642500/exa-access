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

package me.toxz.exp.rbac.pa;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import me.toxz.exp.rbac.Permission;
import me.toxz.exp.rbac.Role;

/**
 * Created by Carlos on 1/4/16.
 */
@DatabaseTable
public class AccessRecord {
    @DatabaseField(generatedId = true) private int id;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 1)
    private Role role;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 1)
    private Permission permission;


    private AccessRecord() {
        // keep for ORMLite
    }

    public AccessRecord(Role role, Permission permission) {
        this.role = role;
        this.permission = permission;
    }

    public Role getRole() {

        return role;
    }

    public Permission getPermission() {
        return permission;
    }
}
