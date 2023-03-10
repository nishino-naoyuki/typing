package jp.ac.asojuku.typing.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jp.ac.asojuku.typing.exception.SystemErrorException;

@Controller
public class DashboardController {
	Logger logger = LoggerFactory.getLogger(DashboardController.class);
	
	@RequestMapping(value= {"/dashboard"}, method=RequestMethod.GET)
    public ModelAndView dashboad(
    		ModelAndView mv
    		) throws SystemErrorException {
		
		logger.info("dashboard！");
		
        mv.setViewName("dashboard");
        
		return mv;
	}
}
