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
import com.j256.ormlite.table.TableUtils;
import com.sun.istack.internal.NotNull;
import me.toxz.exp.rbac.Object;
import me.toxz.exp.rbac.Permission;
import me.toxz.exp.rbac.Role;
import me.toxz.exp.rbac.data.DatabaseHelper;
import me.toxz.exp.rbac.pa.AccessRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.SQLException;
import java.util.List;
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
    private ConnectionSource mConnectionSource;
    private DatabaseType mDatabaseType;
    private Dao<Role, Integer> daoUser;
    private Dao<Object, Integer> daoObject;
    private Dao<AccessRecord, Integer> daoAccess;

    private void init() throws SQLException {
        TableUtils.createTableIfNotExists(mConnectionSource, Role.class);
        TableUtils.createTableIfNotExists(mConnectionSource, Object.class);
        TableUtils.createTableIfNotExists(mConnectionSource, AccessRecord.class);
        createInitAccount();
    }

    @Before
    public void setUp() throws SQLException {
        mDatabaseType = new MysqlDatabaseType();
        mConnectionSource = new JdbcConnectionSource(URL, mDatabaseType);
        try {
            init();
        } catch (SQLException ignored) {
        }
        daoUser = DatabaseHelper.getRoleDao();
        daoObject = DatabaseHelper.getObjectDao();
        daoAccess = DatabaseHelper.getAccessRecordDao();
    }

    void createInitAccount() throws SQLException {
        Role role = new Role("admin");
        DatabaseHelper.getRoleDao().createIfNotExists(role);
    }

    Role testUser() {
        return new Role(TEST_ROLE_NAME);
    }

    Role findUserInDatabase(Role role) throws SQLException {
        return DatabaseHelper.getRoleDao().queryForMatching(testUser()).get(0);
    }

    @Test
    public void testCreateAndDeleteUser() throws SQLException {
        final Role role = testUser();
        createUser(role);

        final Role created = findUserInDatabase(role);
        deleteUser(created);
    }

    void createUser(Role role) throws SQLException {
        assertEquals(1, daoUser.create(role));
    }

    void deleteUser(Role role) throws SQLException {
        assertEquals(1, daoUser.delete(role));
    }

    @Test
    public void createAndDeleteObject() throws SQLException {
        createUser(testUser());
        final Role role = findUserInDatabase(testUser());

        final Object object = testObject(role);
        createObject(object);
        final Object objectCreated = findObjectInDataBase(object);
        deleteObject(objectCreated);
    }

    Object testObject(@NotNull Role own) {
        return new Object(TEST_OBJECT_PATH, own);
    }

    void createObject(Object object) throws SQLException {
        assertEquals(1, daoObject.create(object));
    }

    Object findObjectInDataBase(Object object) throws SQLException {
        return daoObject.queryForMatching(object).get(0);
    }

    void deleteObject(Object object) throws SQLException {
        assertEquals(1, daoObject.delete(object));
    }

    @Test
    public void createAccessRecord() throws SQLException {
        TransactionManager.callInTransaction(mConnectionSource, (Callable<Void>) () -> {
            createUser(testUser());
            final Role role = findUserInDatabase(testUser());

            createObject(testObject(role));
            final Object object = findObjectInDataBase(testObject(role));

            AccessRecord access = new AccessRecord(role, new Permission(object, Permission.Type.WRITE));

            assertEquals(1, daoAccess.create(access));
            List<AccessRecord> accessRecords = daoAccess.queryForMatching(access);
            assertEquals(1, accessRecords.size());

            assertEquals(1, daoAccess.delete(accessRecords));

            deleteObject(object);
            deleteUser(role);
            return null;
        });


    }
}
