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

import com.j256.ormlite.db.MysqlDatabaseType;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import me.toxz.exp.dac.data.DatabaseHelper;
import me.toxz.exp.dac.data.model.AccessRecord;
import me.toxz.exp.dac.data.model.MObject;
import me.toxz.exp.dac.data.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.SQLException;

/**
 * Created by Carlos on 1/6/16.
 */
@RunWith(value = JUnit4.class)
public class CleanDatabase {
    public static final String TEST_USER_PREFIX = "test_user_";
    public static final String TEST_USRE_PASSWORD_PREFIX = TEST_USER_PREFIX + "password_";
    public static final int TEST_USER_NUM = 4;

    @Test
    public void clean() throws SQLException {
        final ConnectionSource mConnectionSource = new JdbcConnectionSource(ModelTest.URL, new MysqlDatabaseType());

        TableUtils.dropTable(mConnectionSource, User.class, false);
        TableUtils.dropTable(mConnectionSource, MObject.class, false);
        TableUtils.dropTable(mConnectionSource, AccessRecord.class, false);
        mConnectionSource.close();
    }

    @Test
    public void createTestEnvironment() throws SQLException {
        final ConnectionSource mConnectionSource = new JdbcConnectionSource(ModelTest.URL, new MysqlDatabaseType());

        TableUtils.createTableIfNotExists(mConnectionSource, User.class);
        TableUtils.createTableIfNotExists(mConnectionSource, MObject.class);
        TableUtils.createTableIfNotExists(mConnectionSource, AccessRecord.class);


        User user = new User("admin", "admin");
        DatabaseHelper.getUserDao().createIfNotExists(user);

//        for (int i = 0; i < TEST_USER_NUM; i++) {
//            DatabaseHelper.getUserDao().create(new User(TEST_USER_PREFIX + i, TEST_USRE_PASSWORD_PREFIX + i));
//        }
    }
}
