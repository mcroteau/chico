<img src="https://static.thenounproject.com/png/6246-200.png" width="120px"/>

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

On start up you need to configure Chico with the new 
data access layer.

```
    Chico.configure(new ChicoAccess(userRepo));
```

### Example AuthService

An example AuthService leveraging Qio for dependency 
management, request handling and data access.


```
package service;

import chico.Chico;
import Spirit;
import model.User;
import repo.UserRepo;
import qio.annotate.Inject;
import qio.annotate.Service;
import qio.model.web.ResponseData;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthService {

    @Inject
    private UserRepo userRepo;

    public boolean signin(String username, String password){
        User user = userRepo.getByUsername(username);
        if(user == null) {
            return false;
        }
        return Chico.signin(username, password);
    }

    public boolean signout(){
        return Chico.signout();
    }

    public boolean isAuthenticated(){
        return Chico.isAuthenticated();
    }

    public boolean hasRole(String role){
        return Chico.hasRole(role);
    }

    public boolean hasPermission(String permission){
        return Chico.hasPermission(permission);
    }

    public User getUser(){
        String username = Chico.getUser();
        User user = userRepo.getUsername(username);
        return user;
    }

    public String authenticate(ResponseData data, HttpServletRequest req) {

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        if(!Chico.signin(username, password)){
            data.put("message", "Wrong username and password");
            return "[redirect]/signin";
        }

        User authdUser = userRepo.getByUsername(username);
        req.getSession().setAttribute("userId", authdUser.getId());

        return "[redirect]/home";
    }

    public String deAuthenticate(ResponseData data, HttpServletRequest req) {
        Chico.signout();
        data.put("message", "Successfully signed out");
        return "[redirect]/";
    }
}

```

Chico is now ready to be used! If you need something more, 
we recommend ApacheShiro, it is a real Bull Dog!

Thank you!




