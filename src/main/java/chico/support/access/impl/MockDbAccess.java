package chico.support.access.impl;

import chico.support.access.DbSecurityAccess;
import chico.Chico;

import java.util.HashSet;
import java.util.Set;

public class MockDbAccess implements DbSecurityAccess {

    private static String MOCK_PASS  = "thought";
    private static String MOCK_SUPER = "SUPER_ROLE";
    private static String MOCK_USER  = "USER_ROLE";

    private static String MOCK_USER_PERMISSION = "users:maintenance:1";
    private static String MOCK_TODO_PERMISSION = "todos:maintenance:3";

    @Override
    public String getPassword(String user) {
        return Chico.dirty(MOCK_PASS);
    }

    @Override
    public Set<String> getRoles(String user) {
        Set<String> roles = new HashSet<>();
        roles.add(MOCK_SUPER);
        roles.add(MOCK_USER);
        return roles;
    }

    @Override
    public Set<String> getPermissions(String user) {
        Set<String> permissions = new HashSet<>();
        permissions.add(MOCK_USER_PERMISSION);
        permissions.add(MOCK_TODO_PERMISSION);
        return permissions;
    }
}
