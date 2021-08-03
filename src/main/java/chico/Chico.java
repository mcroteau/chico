package chico;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import chico.support.DbSecurityAccess;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class Chico {

    public static final String COOKIE   = "JSESSIONID";
    public static final String HASH_256 = "SHA-256";
    public static final String USER_KEY = "user";

    static DbSecurityAccess dbSecurityAccess;
    static Map<String, HttpSession> sessions = new ConcurrentHashMap<String, HttpSession>();

    //////// Thank you Apache Shiro! ////////
    private static ThreadLocal<HttpServletRequest> requestInstance = new InheritableThreadLocal<>();
    private static ThreadLocal<HttpServletResponse> responseInstance = new InheritableThreadLocal<>();

    public static void cache(HttpServletRequest request){
        requestInstance.set(request);
    }
    public static void cache(HttpServletResponse response){
        responseInstance.set(response);
    }

    public static HttpServletRequest getHttpRequest(){
        return requestInstance.get();
    }
    public static HttpServletResponse getHttpResponse(){
        return responseInstance.get();
    }


    public static String getUser(){
        HttpServletRequest req = Chico.getHttpRequest();
        HttpSession session = req.getSession();

        if(session != null){
            return (String) session.getAttribute(Chico.USER_KEY);
        }
        return "";
    }

    public static boolean signin(String username, String password){
        String hashedPassword = hash(password);
        String storedPassword = dbSecurityAccess.getPassword(username);

        if(!isAuthenticated() &&
                storedPassword.equals(hashedPassword)){

            HttpServletRequest req = Chico.getHttpRequest();

            HttpSession oldSession = req.getSession(false);
            if(oldSession != null){
                oldSession.invalidate();
            }

            HttpSession httpSession = req.getSession(true);
            httpSession.setAttribute(Chico.USER_KEY, username);
            sessions.put(httpSession.getId(), httpSession);

            return true;

        }

        return false;
    }



    public static boolean signout(){
        HttpServletRequest req = Chico.getHttpRequest();
        HttpServletResponse resp = Chico.getHttpResponse();
        HttpSession session = req.getSession();

        if(session != null){
            session.removeAttribute(Chico.USER_KEY);
            if(sessions.containsKey(session.getId())){
                sessions.remove(session.getId());
            }
        }

        expireCookie(req, resp);
        return true;
    }

    public static boolean isAuthenticated(){
        HttpServletRequest req = Chico.getHttpRequest();
        if(req != null) {
            HttpSession session = req.getSession(false);
            if (session != null && sessions.containsKey(session.getId())) {
                return true;
            }
        }
        return false;
    }

    private static void expireCookie(HttpServletRequest req, HttpServletResponse resp){
        Cookie cookie = new Cookie(Chico.COOKIE, "");
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
    }

    public static boolean containsCookie(HttpServletRequest req){
        Cookie[] cookies = req.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(Chico.COOKIE)) {
                    return true;
                }
            }
        }
        return false;
    }


    public static boolean hasRole(String role){
        String user = getUser();
        if(user != null) {
            Set<String> roles = dbSecurityAccess.getRoles(user);
            if(roles.contains(role)){
                return true;
            }
        }
        return false;
    }


    public static boolean hasPermission(String permission){
        String user = getUser();
        if(user != null) {
            Set<String> permissions = dbSecurityAccess.getPermissions(user);
            if(permissions.contains(permission)){
                return true;
            }
        }
        return false;
    }

    /////////////// Thank you ///////////////
    public static String hash(String password){
        MessageDigest md = null;
        StringBuffer passwordHashed = new StringBuffer();

        try {
            md = MessageDigest.getInstance(Chico.HASH_256);
            md.update(password.getBytes());

            byte byteData[] = md.digest();

            for (int i = 0; i < byteData.length; i++) {
                passwordHashed.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return passwordHashed.toString();
    }

    public static String dirty(String password){
        MessageDigest md = null;
        StringBuffer passwordHashed = new StringBuffer();

        try {
            md = MessageDigest.getInstance(Chico.HASH_256);
            md.update(password.getBytes());

            byte byteData[] = md.digest();

            for (int i = 0; i < byteData.length; i++) {
                passwordHashed.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return passwordHashed.toString();
    }

    public static boolean configure(DbSecurityAccess dbSecurityAccess){
        Chico.dbSecurityAccess = dbSecurityAccess;
        return true;
    }

    public static boolean createit = false;

    public static void manualCookie(boolean create) {
        createit = create;
    }

    public static boolean createCookie(){
        return createit;
    }

}
