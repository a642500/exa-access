package me.toxz.exp.rbac.ua;

import me.toxz.exp.rbac.Role;
import me.toxz.exp.rbac.User;
import me.toxz.exp.rbac.data.DatabaseHelper;
import me.toxz.exp.rbac.rh.RH;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Carlos on 2016/1/17.
 */
public class UA {
    public static Set<Role> getAllExplicitRole(User user) throws SQLException {
        return DatabaseHelper.getRoleRecordDao().queryForMatching(new RoleRecord(null, user)).stream().map(RoleRecord::getRole).collect(Collectors.toSet());
    }

    public static Set<Role> getAllRole(User user) throws SQLException {
        Set<Role> roles = new HashSet<>();
        List<RoleRecord> records = DatabaseHelper.getRoleRecordDao().queryForMatching(new RoleRecord(null, user));
        for (RoleRecord record : records) {
            Role role = record.getRole();
            roles.add(role);
            roles.addAll(RH.getAllChildren(role));
        }
        return roles;
    }
}
