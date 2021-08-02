package chico.support;

import chico.Chico;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.TagSupport;


public class AuthenticatedTag extends TagSupport {

    @Override
    public int doStartTag() throws JspException {

        try {
            JspWriter out = pageContext.getOut();

            HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
            HttpSession session = req.getSession(false);

            if(session != null) {

                if(Chico.isAuthenticated()){
                    return TagSupport.EVAL_BODY_INCLUDE;
                }else{
                    return TagSupport.SKIP_BODY;
                }

            }else{
                return TagSupport.SKIP_BODY;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return TagSupport.SKIP_BODY;
    }
}
