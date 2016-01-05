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
import me.toxz.exp.dac.data.DatabaseHelper;
import me.toxz.exp.dac.data.model.AccessRecord;
import me.toxz.exp.dac.data.model.AccessType;
import me.toxz.exp.dac.data.model.MObject;
import me.toxz.exp.dac.data.model.User;
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
    public static final String URL = "jdbc:mysql:///access_exp?user=root";
    private ConnectionSource mConnectionSource;
    private DatabaseType mDatabaseType;
    private Dao<User, Integer> daoUser;
    private Dao<MObject, Integer> daoObject;
    private Dao<AccessRecord, Integer> daoAccess;

    @Before
    public void setUp() throws SQLException {
        mDatabaseType = new MysqlDatabaseType();
        mConnectionSource = new JdbcConnectionSource(URL, mDatabaseType);
        try {
            init();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        daoUser = DatabaseHelper.open(User.class);
        daoObject = DatabaseHelper.open(MObject.class);
        daoAccess = DatabaseHelper.open(AccessRecord.class);
    }

    @Test
    public void init() throws SQLException {
        TableUtils.createTableIfNotExists(mConnectionSource, User.class);
        TableUtils.createTableIfNotExists(mConnectionSource, MObject.class);
        TableUtils.createTableIfNotExists(mConnectionSource, AccessRecord.class);
        createInitAccount();
    }

    private void createInitAccount() throws SQLException {
        User user = new User("admin", "admin", "admin");
        DatabaseHelper.open(User.class).createIfNotExists(user);
    }

    @Test
    public void clean() throws SQLException {
        TableUtils.dropTable(mConnectionSource, User.class, false);
        TableUtils.dropTable(mConnectionSource, MObject.class, false);
        TableUtils.dropTable(mConnectionSource, AccessRecord.class, false);
    }

    @Test
    public void testCreateAndDeleteUser() throws SQLException {
        User user = new User("test", "test_account", "test_password");

        assertEquals(1, daoUser.create(user));
        List<User> toDeletes = daoUser.queryForMatching(user);
        assertEquals(1, toDeletes.size());
        assertEquals(1, daoUser.delete(toDeletes.get(0)));
    }

    @Test
    public void createAndDeleteObject() throws SQLException {
        MObject object = new MObject("test_object");

        assertEquals(1, daoObject.create(object));
        List<MObject> toDel = daoObject.queryForMatching(object);
        assertEquals(1, toDel.size());
        assertEquals(1, daoObject.delete(toDel.get(0)));
    }

    @Test
    public void createAccessRecord() throws SQLException {
        User user = new User("test", "test_account", "test_password");
        MObject object = new MObject("test_object2");


        TransactionManager.callInTransaction(mConnectionSource, (Callable<Void>) () -> {
            daoUser.create(user);
            User userCreated = daoUser.queryForMatching(user).get(0);
            daoObject.create(object);
            MObject objectCreated = daoObject.queryForMatching(object).get(0);

            AccessRecord access = new AccessRecord(userCreated, objectCreated, AccessType.WRITE);

            assertEquals(1, daoAccess.create(access));
            List<AccessRecord> accessRecords = daoAccess.queryForMatching(access);
            assertEquals(1, accessRecords.size());

            return null;
        });
    }
}
