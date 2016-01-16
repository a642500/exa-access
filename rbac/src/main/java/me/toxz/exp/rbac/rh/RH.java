package me.toxz.exp.rbac.rh;

import me.toxz.exp.rbac.Role;
import me.toxz.exp.rbac.data.DatabaseHelper;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Carlos on 2016/1/17.
 */
public class RH {
    public static Set<Role> getAllChildren(Role role) throws SQLException {
        final Set<Role> childs = new HashSet<>();
        List<ExtendRecord> chilrenExtendList = DatabaseHelper.getExtendRecordDao().queryForMatching(new ExtendRecord(role, null));
        for (ExtendRecord extendRecord : chilrenExtendList) {
            Role child = extendRecord.getRoleChild();
            childs.add(child);
            childs.addAll(getAllChildren(child));
        }
        return childs;
    }

    public static Set<Role> getAllParents(Role role) throws SQLException {
        final Set<Role> parents = new HashSet<>();
        List<ExtendRecord> parentExtendList = DatabaseHelper.getExtendRecordDao().queryForMatching(new ExtendRecord(null, role));
        for (ExtendRecord extendRecord : parentExtendList) {
            Role parent = extendRecord.getRoleParent();
            parents.add(parent);
            parents.addAll(getAllChildren(parent));
        }
        return parents;
    }
}
