package me.toxz.exp.rbac.ura;

import me.toxz.exp.rbac.User;

/**
 * Created by Carlos on 2016/1/16.
 */
public interface Condition {
    boolean can(User user);
}
