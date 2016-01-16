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

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.db.MysqlDatabaseType;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;
import me.toxz.exp.rbac.Object;
import me.toxz.exp.rbac.Permission;
import me.toxz.exp.rbac.Role;
import me.toxz.exp.rbac.User;
import me.toxz.exp.rbac.data.DatabaseHelper;
import me.toxz.exp.rbac.pa.AccessRecord;
import me.toxz.exp.rbac.pra.CanAssignp;
import me.toxz.exp.rbac.pra.CanRevokep;
import me.toxz.exp.rbac.pra.Conditionp;
import me.toxz.exp.rbac.rh.ExtendRecord;
import me.toxz.exp.rbac.ua.RoleRecord;
import me.toxz.exp.rbac.ura.CanAssign;
import me.toxz.exp.rbac.ura.CanRevoke;
import me.toxz.exp.rbac.ura.Condition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.SQLException;
import java.util.concurrent.Callable;

import static org.junit.Assert.assertEquals;

/**
 * Created by Carlos on 1/4/16.
 */
@RunWith(value = JUnit4.class)
public class ModelTest {
    public static final String URL = DatabaseHelper.URL;
    public static final String TEST_USERNAME = "test";
    public static final String TEST_ROLE_NAME = "test_role";
    public static final String TEST_PASSWORD = "test_password";
    public static final String TEST_OBJECT_PATH = "test_object_path";
    private static Dao<me.toxz.exp.rbac.Object, Integer> mObjectDao;
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
    private ConnectionSource mConnectionSource;
    private DatabaseType mDatabaseType;

    @Before
    public void setUp() throws SQLException {
        mDatabaseType = new MysqlDatabaseType();
        mConnectionSource = new JdbcConnectionSource(URL, mDatabaseType);

        mObjectDao = DatabaseHelper.getObjectDao();
        mRoleDao = DatabaseHelper.getRoleDao();
        mUserDao = DatabaseHelper.getUserDao();
        mCanAssignDao = DatabaseHelper.getCanAssignDao();
        mCanRevokeDao = DatabaseHelper.getCanRevokeDao();
        mConditionDao = DatabaseHelper.getConditionDao();
        mCanAssignpDao = DatabaseHelper.getCanAssignpDao();
        mCanRevokepDao = DatabaseHelper.getCanRevokepDao();
        mConditionpDao = DatabaseHelper.getConditionpDao();
        mPermissionDao = DatabaseHelper.getPermissionDao();
        mAccessRecordDao = DatabaseHelper.getAccessRecordDao();
        mRoleRecordDao = DatabaseHelper.getRoleRecordDao();
        mExtendRecordDao = DatabaseHelper.getExtendRecordDao();
    }

    @Test
    public void createAndDeleteRole() throws SQLException {
        TransactionManager.callInTransaction(mConnectionSource, () -> {
            final Role role = new Role(TEST_ROLE_NAME);
            assertEquals(1, mRoleDao.create(role));
            assertEquals(1, mRoleDao.delete(role));
            return null;
        });
    }


    @Test
    public void createAndDeleteObject() throws SQLException {
        TransactionManager.callInTransaction(mConnectionSource, () -> {
            final Role role = new Role(TEST_ROLE_NAME);
            assertEquals(1, mRoleDao.create(role));
            final Object object = new Object(TEST_OBJECT_PATH, role);
            assertEquals(1, mObjectDao.create(object));

            assertEquals(1, mObjectDao.delete(object));
            assertEquals(1, mRoleDao.delete(role));
            return null;
        });
    }

    @Test
    public void createAccessRecord() throws SQLException {
        TransactionManager.callInTransaction(mConnectionSource, (Callable<Void>) () -> {
            final Role role = new Role(TEST_ROLE_NAME);
            assertEquals(1, mRoleDao.create(role));
            final Object object = new Object(TEST_OBJECT_PATH, role);
            assertEquals(1, mObjectDao.create(object));
            final Permission permission = new Permission(object, Permission.Type.WRITE);
            assertEquals(1, mPermissionDao.create(permission));

            final AccessRecord access = new AccessRecord(role, permission);
            assertEquals(1, mAccessRecordDao.create(access));


            assertEquals(1, mAccessRecordDao.delete(access));
            assertEquals(1, mObjectDao.delete(object));
            assertEquals(1, mRoleDao.delete(role));
            return null;
        });


    }
}
