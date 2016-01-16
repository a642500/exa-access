package me.toxz.exp.rbac.ura;

import com.sun.istack.internal.NotNull;
import me.toxz.exp.rbac.Role;
import me.toxz.exp.rbac.Session;
import me.toxz.exp.rbac.User;
import me.toxz.exp.rbac.data.DatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carlos on 2016/1/16.
 */
public class URA {
    public static boolean canAssgin(Session session, User target, Role role) throws SQLException {
        return queryAllCanAssign(session.getRole()).stream().anyMatch(canAssign -> canAssign.getCondition().can(target) && canAssign.getRange().contains(role));
    }

    @NotNull
    private static List<CanAssign> queryAllCanAssign(Role role) throws SQLException {
        //TODO get all can assign by this role and this role's sub
        List<CanAssign> list = new ArrayList<>();
        list.addAll(DatabaseHelper.getCanAssignDao().queryForMatching(new CanAssign(null, role, null)));
        return list;
    }

    public static boolean canWeakRevoke(Session session, User target, Role role) throws SQLException {
        //TODO judge whether targeted user is explicit member of the role, if not, return false.
        return canRevoke(session, role);
    }

    public static boolean canStrongRevoke(Session session, User target, Role role) throws SQLException {
        //TODO get target those roles which >= role
        // if can revoke all those roles, then return true
        List<Role> rolesAbove = new ArrayList<>();
        return rolesAbove.stream().allMatch(toRevoke -> {
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
    private static List<CanRevoke> queryAllCanRevoke(Role operator) throws SQLException {
        //TODO get all can revoke by this role and this role's sub
        List<CanRevoke> list = new ArrayList<>();
        list.addAll(DatabaseHelper.getCanRevokeDao().queryForMatching(new CanRevoke(operator, null)));
        return list;
    }
}
