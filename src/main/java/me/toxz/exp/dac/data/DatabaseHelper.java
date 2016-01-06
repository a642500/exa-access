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

package me.toxz.exp.dac.data;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.db.MysqlDatabaseType;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import me.toxz.exp.dac.data.model.AccessRecord;
import me.toxz.exp.dac.data.model.MObject;
import me.toxz.exp.dac.data.model.User;

import java.sql.SQLException;

/**
 * Created by Carlos on 1/4/16.
 */
public class DatabaseHelper {
    public static final String URL = "jdbc:mysql:///access_exp?user=root";
    public static DatabaseType mDatabaseType;
    private static ConnectionSource mConnectionSource;
    private static Dao<MObject, Integer> mMObjectDao;
    private static Dao<AccessRecord, Integer> mAccessRecordDao;
    private static Dao<User, Integer> mUserDao;

    private static void init() throws SQLException {
        mDatabaseType = new MysqlDatabaseType();
        mConnectionSource = new JdbcConnectionSource(URL, mDatabaseType);
    }

    public static Dao<AccessRecord, Integer> getAccessRecordDao() throws SQLException {
        if (mAccessRecordDao == null) {
            mAccessRecordDao = open(AccessRecord.class);
        }
        return mAccessRecordDao;
    }

    public static Dao<MObject, Integer> getMObjectDao() throws SQLException {
        if (mMObjectDao == null) {
            mMObjectDao = open(MObject.class);
        }
        return mMObjectDao;
    }

    public static Dao<User, Integer> getUserDao() throws SQLException {
        if (mUserDao == null) {
            mUserDao = open(User.class);
        }
        return mUserDao;
    }

    private static <T> Dao<T, Integer> open(Class<T> clazz) throws SQLException {
        init();
        return DaoManager.createDao(mConnectionSource, clazz);
    }
}
