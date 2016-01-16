package me.toxz.exp.rbac.ura;

import com.google.common.collect.Range;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import me.toxz.exp.rbac.Role;
import me.toxz.exp.rbac.data.RoleRangeType;

/**
 * URA97
 * <p>
 * Created by Carlos on 2016/1/16.
 */
@DatabaseTable
public class CanAssign {
    @DatabaseField(generatedId = true) private int _id;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Role operator;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Condition condition;

    @DatabaseField(persisterClass = RoleRangeType.class)
    private Range<Role> range;

    public CanAssign(Condition condition, Role operator, Range<Role> range) {
        this.condition = condition;
        this.operator = operator;
    }

    public Role getOperator() {
        return operator;
    }

    public Condition getCondition() {
        return condition;
    }

    public Range<Role> getRange() {
        return range;
    }
}
