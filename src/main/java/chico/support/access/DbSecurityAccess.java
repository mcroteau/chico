package chico.support.access;

import java.util.Set;

public interface DbSecurityAccess {

    public String getPassword(String user);

    public Set<String> getRoles(String user);

    public Set<String> getPermissions(String user);

}
