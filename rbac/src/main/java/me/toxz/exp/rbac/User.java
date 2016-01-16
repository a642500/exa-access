package me.toxz.exp.rbac;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Carlos on 2016/1/16.
 */
@DatabaseTable
public class User implements UITreeItem {
    @DatabaseField(generatedId = true) private int _id;
    @DatabaseField(canBeNull = false, unique = true) private String username;
    @DatabaseField(canBeNull = false) private String password;

    private User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public int hashCode() {
        return ("User" + _id).hashCode();
    }

    @Override
    public boolean equals(java.lang.Object obj) {
        return obj instanceof User && this._id == ((User) obj)._id;
    }

    @Override
    public String toString() {
        return username;
    }
}
