package me.toxz.exp.rbac.data;

import com.google.common.collect.Range;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.StringType;
import me.toxz.exp.rbac.Role;

import java.sql.SQLException;

import static com.google.common.collect.BoundType.CLOSED;
import static com.google.common.collect.BoundType.OPEN;

/**
 * Created by Carlos on 2016/1/16.
 */
public class RoleRangeType extends StringType {

    private static final StringType singleTon = new RoleRangeType();

    private RoleRangeType() {
        super(SqlType.STRING, new Class<?>[]{Range.class});
    }

    protected RoleRangeType(SqlType sqlType, Class<?>[] classes) {
        super(sqlType, classes);
    }

    public static StringType getSingleton() {
        return singleTon;
    }

    @Override
    public Object resultStringToJava(FieldType fieldType, String stringValue, int columnPos) throws SQLException {
        return JSONRange.buildRangeFromJson(stringValue);
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) throws SQLException {
        return new JSONRange((Range<Role>) javaObject).toString();
    }

    private static class JSONRange {
        private static Gson mGson = new Gson();
        private boolean lowerOpen;
        private int lowerId;
        private boolean upperOpen;
        private int upperId;

        private JSONRange(Range<Role> roleRange) {
            this.lowerOpen = roleRange.lowerBoundType() == OPEN;
            this.upperOpen = roleRange.upperBoundType() == OPEN;
            this.lowerId = roleRange.lowerEndpoint().getId();
            this.upperId = roleRange.upperEndpoint().getId();
        }

        private static Range<Role> buildRangeFromJson(String json) throws SQLException {
            JSONRange range = mGson.fromJson(json, JSONRange.class);

            Dao<Role, Integer> dao = DatabaseHelper.getRoleDao();
            Role lower = dao.queryForId(range.lowerId);
            Role upper = dao.queryForId(range.upperId);

            return Range.range(lower, range.lowerOpen ? OPEN : CLOSED, upper, range.upperOpen ? OPEN : CLOSED);
        }

        @Override
        public String toString() {
            return mGson.toJson(this);
        }
    }
}