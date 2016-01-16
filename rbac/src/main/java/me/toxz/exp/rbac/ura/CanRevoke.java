package me.toxz.exp.rbac.ura;

import com.google.common.collect.Range;
import me.toxz.exp.rbac.Role;

/**
 * Created by Carlos on 2016/1/16.
 */
public class CanRevoke {
    private Role operator;
    private Range<Role> range;

    public CanRevoke(Role operator, Range<Role> range) {
        this.operator = operator;
        this.range = range;
    }

    public Role getOperator() {
        return operator;
    }

    public Range<Role> getRange() {
        return range;
    }
}
