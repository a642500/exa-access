package me.toxz.exp.rbac.pa;

import me.toxz.exp.rbac.Permission;
import me.toxz.exp.rbac.Role;
import me.toxz.exp.rbac.data.DatabaseHelper;
import me.toxz.exp.rbac.rh.RH;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Carlos on 2016/1/17.
 */
public class PA {
    public static Set<Permission> getAllExplicitPermission(Role role) throws SQLException {
        return DatabaseHelper.getAccessRecordDao().queryForMatching(new AccessRecord(role, null)).stream().map(AccessRecord::getPermission).collect(Collectors.toSet());
    }

    public static Set<Permission> getAllPermission(Role role) throws SQLException {
        Set<Permission> permissions = new HashSet<>();
        permissions.addAll(DatabaseHelper.getAccessRecordDao().queryForMatching(new AccessRecord(role, null)).stream().map(AccessRecord::getPermission).collect(Collectors.toSet()));

        Set<Role> roles = RH.getAllChildren(role);
        for (Role childRole : roles) {
            permissions.addAll(getAllPermission(childRole));
        }
        return permissions;
    }
}
