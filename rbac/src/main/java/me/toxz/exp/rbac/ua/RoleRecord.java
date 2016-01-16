package me.toxz.exp.rbac.ua;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import me.toxz.exp.rbac.Role;
import me.toxz.exp.rbac.User;

/**
 * Created by Carlos on 2016/1/17.
 */
@DatabaseTable
public class RoleRecord {
    @DatabaseField(generatedId = true) private int id;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 1)
    private Role role;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 1)
    private User user;

    public RoleRecord(Role role, User user) {
        this.role = role;
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public User getUser() {
        return user;
    }
}
