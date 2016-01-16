package me.toxz.exp.rbac;

/**
 * Created by Carlos on 2016/1/16.
 */
public class Session {
    private User user;
    private Role role;

    public Session(Role role, User user) {
        this.role = role;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Role getRole() {
        return role;
    }
}
