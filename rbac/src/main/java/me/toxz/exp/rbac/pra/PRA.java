package me.toxz.exp.rbac.pra;

import com.sun.istack.internal.NotNull;
import me.toxz.exp.rbac.Permission;
import me.toxz.exp.rbac.Role;
import me.toxz.exp.rbac.Session;
import me.toxz.exp.rbac.data.DatabaseHelper;
import me.toxz.exp.rbac.pa.PA;
import me.toxz.exp.rbac.rh.RH;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Carlos on 2016/1/16.
 */
public class PRA {
    public static boolean canAssginp(Session session, Role target, Permission permission) throws SQLException {
        return queryAllCanAssignp(session.getRole()).stream().anyMatch(canAssign -> canAssign.getConditionp().can(permission) && canAssign.getRange().contains(target));
    }

    @NotNull
    private static Set<CanAssignp> queryAllCanAssignp(Role role) throws SQLException {
        // get all can assign by this role and this role's sub
        final Set<CanAssignp> canAssignps = new HashSet<>();
        final Set<Role> children = RH.getAllChildren(role);
        children.add(role);
        for (Role child : children) {
            canAssignps.addAll(DatabaseHelper.getCanAssignpDao().queryForMatching(new CanAssignp(null, child, null)));
        }
        return canAssignps;
    }

    public static boolean canWeakRevokep(Session session, Role target, Permission permission) throws SQLException {
        // judge whether targeted role explicitly has the permission, if not, return false.
        Set<Permission> explicitPermission = PA.getAllExplicitPermission(target);
        return explicitPermission.contains(permission) && canRevokep(session, target);
    }

    public static boolean canStrongRevokep(Session session, Role target) throws SQLException {
        // if can revoke all those roles, then return true
        Set<Role> roleParents = RH.getAllParents(target);
        // get target those roles which >= role
        roleParents.add(target);
        return roleParents.stream().allMatch(toRevoke -> {
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
    private static Set<CanRevokep> queryAllCanRevoke(Role operator) throws SQLException {
        // get all can revoke by this role and this role's sub
        final Set<CanRevokep> canRevokeps = new HashSet<>();
        final Set<Role> children = RH.getAllChildren(operator);
        children.add(operator);

        for (Role child : children) {
            canRevokeps.addAll(DatabaseHelper.getCanRevokepDao().queryForMatching(new CanRevokep(child, null)));
        }
        return canRevokeps;
    }
}
