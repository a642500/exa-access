package me.toxz.exp.rbac;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Carlos on 2016/1/16.
 */
@DatabaseTable
public class Permission {
    @DatabaseField(generatedId = true) private int _id;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true) private Object object;
    @DatabaseField(canBeNull = false) private Type type;

    public int get_id() {
        return _id;
    }

    public Object getObject() {

        return object;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        WRITE, READ, CONTROL, ADD, DELETE
    }
}
