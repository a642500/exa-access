package me.toxz.exp.rbac.ura;

import com.google.common.collect.Range;
import me.toxz.exp.rbac.Role;

/**
 * URA97
 * <p>
 * Created by Carlos on 2016/1/16.
 */
public class CanAssign {
    private Role operator;
    private Condition condition;
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
