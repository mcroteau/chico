#  Chico

Chico is an Authentication and Authorization plugin 
built for the Jakarta EE specification. It is very simple 
to implement. 

### Installation 
```
<dependency>
    <groupId>dev.j3ee</groupId>
    <artifactId>chico</artifactId>
    <version>0.1</version>
</dependency>
```

### Configuration

In order to leverage Chico, you need to initialize 
a data layer by implementing the **DbSecurityAccess**

Here is a sample DbSecurityAccess implementation: 

```
package db;

import chico.support.DbSecurityAccess;
import model.User;
import repo.UserRepo;
import java.util.Set;

public class ChicoAccess implements DbSecurityAccess {
    
    public ChicoAccess(UserRepo userRepo){
        this.userRepo = userRepo;
    }    

    private UserRepo userRepo;
 
    public String getPassword(String username){
        String password = userRepo.getUserPassword(username);
        return password;
    }

    public Set<String> getRoles(String username){
        User user = userRepo.getByUsername(username);
        Set<String> roles = userRepo.getUserRoles(user.getId());
        return roles;
    }

    public Set<String> getPermissions(String username){
        User user = userRepo.getByUsername(username);
        Set<String> permissions = userRepo.getUserPermissions(user.getId());
        return permissions;
    }

}
```

On startup you need to configure Chico with the new 
data access layer.

```
    Chico.configure(new ChicoAccess(userRepo));
```

Chico is now ready to be used! If you need something more, 
we recommend ApacheShiro, it is a real Bull Dog!




