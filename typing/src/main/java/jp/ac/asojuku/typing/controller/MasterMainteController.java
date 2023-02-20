package jp.ac.asojuku.typing.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jp.ac.asojuku.typing.exception.SystemErrorException;

@Controller
public class MasterMainteController {
	Logger logger = LoggerFactory.getLogger(MasterMainteController.class);


	@RequestMapping(value= {"/mastermainte"}, method=RequestMethod.GET)
    public ModelAndView signin(
    		ModelAndView mv
    		) throws SystemErrorException {
		
        
        mv.setViewName("mastermainte");
        
		return mv;
	}
}
