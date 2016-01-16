package me.toxz.exp.rbac.rh;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import me.toxz.exp.rbac.Role;

/**
 * Created by Carlos on 2016/1/17.
 */
@DatabaseTable
public class ExtendRecord {
    @DatabaseField(generatedId = true) private int id;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 1)
    private Role roleParent;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 1)
    private Role roleChild;

    public ExtendRecord(Role roleParent, Role roleChild) {
        this.roleParent = roleParent;
        this.roleChild = roleChild;
    }

    public Role getRoleParent() {

        return roleParent;
    }

    public Role getRoleChild() {
        return roleChild;
    }
}
