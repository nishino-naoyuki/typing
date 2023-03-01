package jp.ac.asojuku.typing.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jp.ac.asojuku.typing.dto.LoginInfoDto;
import jp.ac.asojuku.typing.dto.QuestionOutlineDto;
import jp.ac.asojuku.typing.exception.SystemErrorException;
import jp.ac.asojuku.typing.param.SessionConst;
import jp.ac.asojuku.typing.service.EventService;

@Controller
@RequestMapping("/event")
public class EventController {
	Logger logger = LoggerFactory.getLogger(EventController.class);
	@Autowired
	HttpSession session;
	@Autowired
	EventService eventService;

	@RequestMapping(value= {"/list"}, method=RequestMethod.GET)
    public ModelAndView list(
    		ModelAndView mv
    		) throws SystemErrorException {

		//ログイン情報を取得する
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		
		//List<QuestionOutlineDto> qList = eventService.list(loginInfo.getUid(), loginInfo.getRole());
        
		//mv.addObject("qList", qList);
		mv.setViewName("elist");
		
		return mv;
	}
}
