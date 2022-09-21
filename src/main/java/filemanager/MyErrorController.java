package filemanager;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyErrorController implements ErrorController {

	@RequestMapping("/error")
	public String handleError(Model model, HttpServletRequest request) {
		String errorMsg = request.getAttribute(RequestDispatcher.ERROR_MESSAGE).toString();

		model.addAttribute("errorMsg", errorMsg + ".");
		return "error";
	}

	@Override
	public String getErrorPath() {
		return null;
	}
}
