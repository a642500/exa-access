/*
 *     Copyright (C) 2016 Carlos
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License along
 *     with this program; if not, write to the Free Software Foundation, Inc.,
 *     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package me.toxz.exp.rbac.data;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.db.MysqlDatabaseType;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;
import me.toxz.exp.rbac.Permission;
import me.toxz.exp.rbac.Role;
import me.toxz.exp.rbac.User;
import me.toxz.exp.rbac.pa.AccessRecord;
import me.toxz.exp.rbac.pra.CanAssignp;
import me.toxz.exp.rbac.pra.CanRevokep;
import me.toxz.exp.rbac.pra.Conditionp;
import me.toxz.exp.rbac.rh.ExtendRecord;
import me.toxz.exp.rbac.ua.RoleRecord;
import me.toxz.exp.rbac.ura.CanAssign;
import me.toxz.exp.rbac.ura.CanRevoke;
import me.toxz.exp.rbac.ura.Condition;

import java.sql.SQLException;
import java.util.concurrent.Callable;

/**
 * Created by Carlos on 1/4/16.
 */
public class DatabaseHelper {
    public static final String URL = "jdbc:mysql://10.111.213.121/access_exp_rbac?user=root&password=9072";
    public static DatabaseType mDatabaseType;
    private static ConnectionSource mConnectionSource;
    private static Dao<Object, Integer> mObjectDao;
    private static Dao<Role, Integer> mRoleDao;
    private static Dao<User, Integer> mUserDao;
    private static Dao<CanAssign, Integer> mCanAssignDao;
    private static Dao<CanRevoke, Integer> mCanRevokeDao;
    private static Dao<Condition, Integer> mConditionDao;
    private static Dao<CanAssignp, Integer> mCanAssignpDao;
    private static Dao<CanRevokep, Integer> mCanRevokepDao;
    private static Dao<Conditionp, Integer> mConditionpDao;
    private static Dao<Permission, Integer> mPermissionDao;
    private static Dao<AccessRecord, Integer> mAccessRecordDao;
    private static Dao<RoleRecord, Integer> mRoleRecordDao;
    private static Dao<ExtendRecord, Integer> mExtendRecordDao;


    public static <T> T callInTransaction(Callable<T> callable) throws SQLException {
        return TransactionManager.callInTransaction(mConnectionSource, callable);
    }

    private static void init() throws SQLException {
        mDatabaseType = new MysqlDatabaseType();
        mConnectionSource = new JdbcConnectionSource(URL, mDatabaseType);
    }

    public static Dao<Object, Integer> getObjectDao() throws SQLException {
        if (mObjectDao == null) {
            mObjectDao = open(Object.class);
        }
        return mObjectDao;
    }

    public static Dao<Role, Integer> getRoleDao() throws SQLException {
        if (mRoleDao == null) {
            mRoleDao = open(Role.class);
        }
        return mRoleDao;
    }


    public static Dao<User, Integer> getUserDao() throws SQLException {
        if (mUserDao == null) {
            mUserDao = open(User.class);
        }
        return mUserDao;
    }

    public static Dao<CanAssign, Integer> getCanAssignDao() throws SQLException {
        if (mCanAssignDao == null) {
            mCanAssignDao = open(CanAssign.class);
        }
        return mCanAssignDao;
    }

    public static Dao<CanRevoke, Integer> getCanRevokeDao() throws SQLException {
        if (mCanRevokeDao == null) {
            mCanRevokeDao = open(CanRevoke.class);
        }
        return mCanRevokeDao;
    }

    public static Dao<Condition, Integer> getConditionDao() throws SQLException {
        if (mConditionDao == null) {
            mConditionDao = open(Condition.class);
        }
        return mConditionDao;
    }

    public static Dao<CanAssignp, Integer> getCanAssignpDao() throws SQLException {
        if (mCanAssignpDao == null) {
            mCanAssignpDao = open(CanAssignp.class);
        }
        return mCanAssignpDao;
    }

    public static Dao<CanRevokep, Integer> getCanRevokepDao() throws SQLException {
        if (mCanRevokepDao == null) {
            mCanRevokepDao = open(CanRevokep.class);
        }
        return mCanRevokepDao;
    }

    public static Dao<Conditionp, Integer> getConditionpDao() throws SQLException {
        if (mConditionpDao == null) {
            mConditionpDao = open(Conditionp.class);
        }
        return mConditionpDao;
    }

    public static Dao<Permission, Integer> getPermissionDao() throws SQLException {
        if (mPermissionDao == null) {
            mPermissionDao = open(Permission.class);
        }
        return mPermissionDao;
    }

    public static Dao<AccessRecord, Integer> getAccessRecordDao() throws SQLException {
        if (mAccessRecordDao == null) {
            mAccessRecordDao = open(AccessRecord.class);
        }
        return mAccessRecordDao;
    }

    public static Dao<RoleRecord, Integer> getRoleRecordDao() throws SQLException {
        if (mRoleRecordDao == null) {
            mRoleRecordDao = open(RoleRecord.class);
        }
        return mRoleRecordDao;
    }

    public static Dao<ExtendRecord, Integer> getExtendRecordDao() throws SQLException {
        if (mExtendRecordDao == null) {
            mExtendRecordDao = open(ExtendRecord.class);
        }
        return mExtendRecordDao;
    }

    private static <T> Dao<T, Integer> open(Class<T> clazz) throws SQLException {
        init();
        return DaoManager.createDao(mConnectionSource, clazz);
    }
}
