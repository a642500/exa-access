package me.toxz.exp.rbac.ura;

import me.toxz.exp.rbac.Role;
import me.toxz.exp.rbac.User;

/**
 * Created by Carlos on 2016/1/16.
 */
public class RoleBeingCondition implements Condition {
    private Role role;

    @Override
    public boolean can(User user) {

        return false;
    }
}
