package me.toxz.exp.rbac.pra;

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
public class CanAssignp {
    @DatabaseField(generatedId = true) private int _id;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Role operator;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Conditionp conditionp;

    @DatabaseField(persisterClass = RoleRangeType.class)
    private Range<Role> range;

    private CanAssignp() {
    }

    public CanAssignp(Conditionp conditionp, Role operator, Range<Role> range) {
        this.conditionp = conditionp;
        this.operator = operator;
    }

    public Role getOperator() {
        return operator;
    }

    public Conditionp getConditionp() {
        return conditionp;
    }

    public Range<Role> getRange() {
        return range;
    }
}
