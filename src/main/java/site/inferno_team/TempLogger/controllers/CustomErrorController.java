package site.inferno_team.TempLogger.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object msg = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        model.addAttribute("msg", msg.toString());
        model.addAttribute("status_code", statusCode.toString());
        
        if (statusCode.toString().equals("403")) 
            return "403";
        
        return "404";
    }

}
