package me.toxz.exp.dac.data.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Carlos on 2016/1/17.
 */
@DatabaseTable
public class BlackToken {
    @DatabaseField(generatedId = true) private int id;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 1)
    private User subject;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 1)
    private MObject object;
    @DatabaseField(canBeNull = false) private AccessType accessType;

    public BlackToken(User subject, MObject object, AccessType accessType) {
        this.subject = subject;
        this.object = object;
        this.accessType = accessType;
    }

    private BlackToken() {
    }

    public User getSubject() {
        return subject;
    }

    public MObject getObject() {
        return object;
    }

    public AccessType getAccessType() {
        return accessType;
    }

    @Override
    public String toString() {
        return String.format("%s can't be granted to %s %s", subject, accessType, object);
    }
}
