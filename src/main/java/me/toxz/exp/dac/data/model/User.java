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
public class User extends MSubject {
    @DatabaseField(canBeNull = false)
    private String username;
    @DatabaseField(unique = true, canBeNull = false)
    private String account;
    @DatabaseField
    private String passwordHash;

    private User() {
        // keep for ORMLite
    }

    public User(@NotNull String username, @NotNull String account, String password) {
        this.username = username;
        this.account = account;
        updatePassword(password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(@NotNull String username) {
        this.username = username;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(@NotNull String account) {
        this.account = account;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean isPasswordValidate(String password) {
        return computePasswordHash(password).equals(this.passwordHash);
    }

    public void updatePassword(String password) {
        this.passwordHash = computePasswordHash(password);
    }

    private String computePasswordHash(String password) {
        return password;//TODO hash password
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof User && username.equals(((User) obj).username) && account.equals(((User) obj).account) && passwordHash.equals(((User) obj).passwordHash);
    }
}
