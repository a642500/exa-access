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

    @Test
    public void clean() throws SQLException {
        final ConnectionSource mConnectionSource = new JdbcConnectionSource(ModelTest.URL, new MysqlDatabaseType());

        TableUtils.dropTable(mConnectionSource, User.class, false);
        TableUtils.dropTable(mConnectionSource, MObject.class, false);
        TableUtils.dropTable(mConnectionSource, AccessRecord.class, false);
    }
}
