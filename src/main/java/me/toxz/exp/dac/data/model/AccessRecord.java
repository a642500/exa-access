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

/**
 * Created by Carlos on 1/4/16.
 */
@DatabaseTable
public class AccessRecord {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, foreign = true)
    private User subject;
    @DatabaseField(canBeNull = false, foreign = true)
    private MObject object;
    @DatabaseField(canBeNull = false)
    private AccessType accessType;

    private AccessRecord() {
        // keep for ORMLite
    }

    public AccessRecord(User subject, MObject object, AccessType accessType) {
        this.subject = subject;
        this.object = object;
        this.accessType = accessType;
    }
}
