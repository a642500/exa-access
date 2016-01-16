package me.toxz.exp.rbac.ura;

import com.sun.istack.internal.NotNull;
import me.toxz.exp.rbac.Role;
import me.toxz.exp.rbac.Session;
import me.toxz.exp.rbac.User;
import me.toxz.exp.rbac.data.DatabaseHelper;
import me.toxz.exp.rbac.rh.RH;
import me.toxz.exp.rbac.ua.UA;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Carlos on 2016/1/16.
 */
public class URA {
    public static boolean canAssgin(Session session, User target, Role role) throws SQLException {
        return queryAllCanAssign(session.getRole()).stream().anyMatch(canAssign -> canAssign.getCondition().can(target) && canAssign.getRange().contains(role));
    }

    @NotNull
    public static Set<CanAssign> queryAllCanAssign(Role role) throws SQLException {
        // get all can assign by this role and this role's sub
        final Set<CanAssign> canAssigns = new HashSet<>();
        final Set<Role> children = RH.getAllChildren(role);
        children.add(role);
        for (Role child : children) {
            canAssigns.addAll(DatabaseHelper.getCanAssignDao().queryForMatching(new CanAssign(null, child, null)));
        }
        return canAssigns;
    }

    public static boolean canWeakRevoke(Session session, User target, Role role) throws SQLException {
        // judge whether targeted user is explicit member of the role, if not, return false.
        Set<Role> explicitRole = UA.getAllExplicitRole(target);
        return explicitRole.contains(role) && canRevoke(session, role);
    }

    public static boolean canStrongRevoke(Session session, User target, Role role) throws SQLException {
        // if can revoke all those roles, then return true
        Set<Role> roleParents = RH.getAllParents(role);
        // get target those roles which >= role
        roleParents.add(role);
        return roleParents.stream().allMatch(toRevoke -> {
            try {
                return canRevoke(session, toRevoke);
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    private static boolean canRevoke(Session session, Role role) throws SQLException {
        return queryAllCanRevoke(session.getRole()).stream().anyMatch(canRevoke -> canRevoke.getRange().contains(role));
    }

    @NotNull
    public static Set<CanRevoke> queryAllCanRevoke(Role operator) throws SQLException {
        // get all can revoke by this role and this role's sub
        final Set<CanRevoke> canRevokes = new HashSet<>();
        final Set<Role> children = RH.getAllChildren(operator);
        children.add(operator);
        for (Role child : children) {
            canRevokes.addAll(DatabaseHelper.getCanRevokeDao().queryForMatching(new CanRevoke(operator, null)));
        }
        return canRevokes;
    }
}
