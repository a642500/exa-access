package me.toxz.exp.rbac.pra;

import com.sun.istack.internal.NotNull;
import me.toxz.exp.rbac.Permission;
import me.toxz.exp.rbac.Role;
import me.toxz.exp.rbac.Session;
import me.toxz.exp.rbac.data.DatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carlos on 2016/1/16.
 */
public class PRA {
    public static boolean canAssgin(Session session, Role target, Permission permission) throws SQLException {
        return queryAllCanAssign(session.getRole()).stream().anyMatch(canAssign -> canAssign.getConditionp().can(permission) && canAssign.getRange().contains(target));
    }

    @NotNull
    private static List<CanAssignp> queryAllCanAssign(Role role) throws SQLException {
        //TODO get all can assign by this role and this role's sub
        List<CanAssignp> list = new ArrayList<>();
        list.addAll(DatabaseHelper.getCanAssignpDao().queryForMatching(new CanAssignp(null, role, null)));
        return list;
    }

    public static boolean canWeakRevokep(Session session, Role target) throws SQLException {
        //TODO judge whether targeted user is explicit member of the role, if not, return false.
        return canRevokep(session, target);
    }

    public static boolean canStrongRevokep(Session session, Role target) throws SQLException {
        //TODO get target those roles which >= role
        // if can revoke all those roles, then return true
        List<Role> rolesAbove = new ArrayList<>();
        return rolesAbove.stream().allMatch(toRevoke -> {
            try {
                return canRevokep(session, toRevoke);
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    private static boolean canRevokep(Session session, Role role) throws SQLException {
        return queryAllCanRevoke(session.getRole()).stream().anyMatch(canRevoke -> canRevoke.getRange().contains(role));
    }

    @NotNull
    private static List<CanRevokep> queryAllCanRevoke(Role operator) throws SQLException {
        //TODO get all can revoke by this role and this role's sub
        List<CanRevokep> list = new ArrayList<>();
        list.addAll(DatabaseHelper.getCanRevokepDao().queryForMatching(new CanRevokep(operator, null)));
        return list;
    }
}
