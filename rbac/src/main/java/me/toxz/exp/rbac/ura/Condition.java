package me.toxz.exp.rbac.ura;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import me.toxz.exp.rbac.Role;
import me.toxz.exp.rbac.User;

/**
 * Created by Carlos on 2016/1/16.
 */
@DatabaseTable
public class Condition {
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Role requireRole;

    public boolean can(User user) {
        //TODO implement
        return false;
    }
}
