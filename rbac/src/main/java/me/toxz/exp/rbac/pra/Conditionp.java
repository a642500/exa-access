package me.toxz.exp.rbac.pra;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import me.toxz.exp.rbac.Permission;
import me.toxz.exp.rbac.Role;

/**
 * Created by Carlos on 2016/1/16.
 */
@DatabaseTable
public class Conditionp {
    @DatabaseField(generatedId = true) private int id;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Role requireRole;

    public boolean can(Permission permission) {
        //TODO implement
        return false;
    }
}
