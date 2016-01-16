package me.toxz.exp.rbac.rh;

import me.toxz.exp.rbac.Role;
import me.toxz.exp.rbac.data.DatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carlos on 2016/1/17.
 */
public class RH {
    public static List<Role> getAllChildren(Role role) throws SQLException {
        final List<Role> children = new ArrayList<>();
        List<ExtendRecord> parentList = DatabaseHelper.getExtendRecordDao().queryForMatching(new ExtendRecord(role, null));
        for (ExtendRecord extendRecord : parentList) {
            Role child = extendRecord.getRoleChild();
            children.add(child);
            children.addAll(getAllChildren(child));
        }
        return children;
    }
}
